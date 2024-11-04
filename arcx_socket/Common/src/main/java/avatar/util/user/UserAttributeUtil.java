package avatar.util.user;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.user.attribute.UserEnergyMsg;
import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.product.energy.EnergyExchangeAwardEntity;
import avatar.entity.product.energy.EnergyExchangeUserAwardEntity;
import avatar.entity.product.energy.EnergyExchangeUserHistoryEntity;
import avatar.entity.user.attribute.UserAttributeConfigEntity;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.energy.EnergyExchangeAwardDao;
import avatar.module.product.energy.EnergyExchangeProductDao;
import avatar.module.product.energy.EnergyExchangeUserAwardDao;
import avatar.module.product.energy.EnergyExchangeUserHistoryDao;
import avatar.module.user.attribute.UserAttributeConfigDao;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.attribute.UserGameExpConfigDao;
import avatar.module.user.online.UserOnlineExpMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.AddEnergyAwardTask;
import avatar.util.LogUtil;
import avatar.util.basic.general.AwardUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class UserAttributeUtil {
    /**

     */
    public static UserAttributeMsgEntity initUserAttributeMsgEntity(int userId) {
        UserAttributeMsgEntity entity = new UserAttributeMsgEntity();
        entity.setUserId(userId);
        int defaultLevel = 1;
        entity.setUserLevel(defaultLevel);
        entity.setUserLevelExp(0);
        entity.setEnergyLevel(defaultLevel);
        entity.setEnergyNum(0);
        entity.setChargingLevel(defaultLevel);
        entity.setChargingTime(TimeUtil.getNowTimeStr());
        entity.setAirdropLevel(defaultLevel);
        entity.setLuckyLevel(defaultLevel);
        entity.setCharmLevel(defaultLevel);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static long loadExpNum(int userId) {
        
        UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
        return entity==null?0:entity.getUserLevelExp();
    }

    /**

     */
    public static void addUserGameExp(int userId, long coinNum){
        
        UserOnlineExpMsg expMsg = UserOnlineExpMsgDao.getInstance().loadByMsg(userId);
        
        long maxCoin = UserGameExpConfigDao.getInstance().loadMsg();
        if(expMsg!=null && maxCoin>0){
            long dealCoin = expMsg.getCoinNum()+coinNum;
            long addExp = dealCoin/maxCoin;
            if(addExp>0){
                dealCoin %= maxCoin;
                expMsg.setCoinNum(dealCoin);
                long dealExp = expMsg.getExpNum()+addExp;
                
                UserAttributeMsgEntity msgEntity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                long lvExp = loadUserLvExp(msgEntity);
                if (lvExp > 0 && dealExp >= lvExp) {
                    dealExp %= lvExp;
                    
                    userLevelUp(msgEntity);
                }
                expMsg.setExpNum(dealExp);
            }
            UserOnlineExpMsgDao.getInstance().setCache(userId, expMsg);
        }
    }

    /**

     */
    private static long loadUserLvExp(UserAttributeMsgEntity msgEntity) {
        int level = msgEntity.getUserLevel();
        
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        return entity==null?0:entity.getLvExp();
    }

    /**

     */
    private static void userLevelUp(UserAttributeMsgEntity entity) {
        entity.setUserLevel(entity.getUserLevel()+1);
        UserAttributeMsgDao.getInstance().update(entity);
    }

    /**

     */
    public static UserEnergyMsg userEnergyMsg(UserAttributeMsgEntity userAttribute) {
        UserEnergyMsg msg = new UserEnergyMsg();
        msg.setCnAmt(userAttribute.getEnergyNum());
        msg.setTtAmt(loadTotalEnergyNum(userAttribute.getEnergyLevel()));
        msg.setLfTm(loadEnergyRefreshTime(userAttribute.getChargingTime(), userAttribute.getChargingLevel()));
        return msg;
    }

    /**

     */
    private static long loadTotalEnergyNum(int level) {
        
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        return entity==null?0:entity.getEnergyMax();
    }

    /**

     */
    private static long loadEnergyRefreshTime(String chargingTime, int level) {
        
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        long refreshTime = entity.getChargingSecond();
        return Math.max(0,refreshTime-(TimeUtil.getNowTime()-TimeUtil.strToLong(chargingTime))/1000);
    }

    /**

     */
    public static void productAwardEnergyDeal(int userId, int productId, int getType,
            List<GeneralAwardMsg> resultAwardList) {
        List<GeneralAwardMsg> awardList = new ArrayList<>();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                2000);
        try {
            if (lock.lock()) {
                if(SuperPlayerUtil.isSuperPlayer(userId)) {
                    
                    UserAttributeMsgEntity msgEntity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                    if (msgEntity.getEnergyNum() > 0) {
                        
                        List<EnergyExchangeUserAwardEntity> awardHistoryList = new ArrayList<>();
                        
                        awardList = loadProductEnergyAward(productId, awardHistoryList);
                        if (awardList.size() > 0) {
                            
                            boolean flag = costUserEnergy(msgEntity);
                            if (flag) {
                                
                                SchedulerSample.delayed(10,
                                        new AddEnergyAwardTask(productId, userId, getType, awardList, awardHistoryList));
                            } else {

                                awardList = new ArrayList<>();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
        if(awardList.size()>0){
            resultAwardList.addAll(awardList);
        }
    }

    /**

     */
    private static List<GeneralAwardMsg> loadProductEnergyAward(int productId, List<EnergyExchangeUserAwardEntity> awardHistoryList) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        int configId = EnergyExchangeProductDao.getInstance().loadMsg(productId);
        if(configId>0) {
            
            List<EnergyExchangeAwardEntity> awardList = EnergyExchangeAwardDao.getInstance().loadMsg(configId);
            if(awardList.size()>0){
                awardList.forEach(entity->{
                    if(awardFlag(entity)){
                        int awardNum = StrUtil.loadInterValNum(entity.getMinNum(), entity.getMaxNum());
                        
                        retList.add(AwardUtil.initGeneralAwardMsg(entity.getAwardType(),
                                entity.getAwardId(), entity.getAwardImgId(), awardNum));
                        
                        awardHistoryList.add(initEnergyExchangeUserAwardEntity(entity, awardNum));
                    }
                });
            }
        }
        return retList;
    }

    /**

     */
    private static EnergyExchangeUserAwardEntity initEnergyExchangeUserAwardEntity(
            EnergyExchangeAwardEntity awardEntity, int awardNum) {
        EnergyExchangeUserAwardEntity entity = new EnergyExchangeUserAwardEntity();
        entity.setAwardType(awardEntity.getAwardType());
        entity.setAwardId(awardEntity.getAwardId());
        entity.setMinNum(awardEntity.getMinNum());
        entity.setMaxNum(awardEntity.getMaxNum());
        entity.setAwardProbability(awardEntity.getAwardProbability());
        entity.setTotalProbability(awardEntity.getTotalProbability());
        entity.setAwardNum(awardNum);
        return entity;
    }

    /**

     */
    private static boolean awardFlag(EnergyExchangeAwardEntity entity) {
        boolean flag = false;
        if(entity.getMaxTime()==0 || entity.getTriggerTime()<entity.getMaxTime()){
            if(StrUtil.isAward(entity.getAwardProbability(), entity.getTotalProbability())){
                flag = true;
            }
        }
        if(flag && entity.getMaxTime()>0){
            entity.setTriggerTime(entity.getTriggerTime()+1);
            EnergyExchangeAwardDao.getInstance().update(entity);
        }
        return flag;
    }

    /**

     */
    private static boolean costUserEnergy(UserAttributeMsgEntity entity) {
        entity.setEnergyNum(entity.getEnergyNum()-1);
        if(oriEnergyMax(entity.getEnergyNum(), entity.getEnergyLevel())){
            entity.setChargingTime(TimeUtil.getNowTimeStr());
        }
        boolean flag = UserAttributeMsgDao.getInstance().update(entity);
        int userId = entity.getUserId();
        if(flag && UserOnlineUtil.isOnline(userId)) {
            
            UserNoticePushUtil.userEnergyMsg(userId, -1);
        }
        return flag;
    }

    /**

     */
    private static boolean oriEnergyMax(long energyNum, int energyLevel) {
        
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(energyLevel);
        long maxNum = entity==null?0:entity.getEnergyMax();
        return (energyNum+1)==maxNum;
    }

    /**

     */
    public static void addEnergyExchangeHistory(int userId, int productId, int getType,
            List<EnergyExchangeUserAwardEntity> awardHistoryList) {
        
        long historyId = EnergyExchangeUserHistoryDao.getInstance().insert(
                initEnergyExchangeUserHistoryEntity(userId, productId, getType));
        if(historyId>0){
            
            for(EnergyExchangeUserAwardEntity entity : awardHistoryList){
                entity.setHistoryId(historyId);
            }
            
            EnergyExchangeUserAwardDao.getInstance().insert(awardHistoryList);
        }else{

        }
    }

    /**

     */
    private static EnergyExchangeUserHistoryEntity initEnergyExchangeUserHistoryEntity(int userId, int productId, int getType) {
        EnergyExchangeUserHistoryEntity entity = new EnergyExchangeUserHistoryEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setGetType(getType);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }
}
