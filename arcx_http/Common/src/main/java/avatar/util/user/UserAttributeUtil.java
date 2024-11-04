package avatar.util.user;

import avatar.data.user.attribute.UserAttributeMsg;
import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.user.attribute.UserAttributeConfigEntity;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.enumMsg.user.UserAttributeTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.attribute.UserAttributeConfigDao;
import avatar.module.user.attribute.UserAttributeLvListDao;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.online.UserOnlineExpMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.user.RefreshUserEnergyNoticeTask;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

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
    private static long loadEnergyRefreshTime(int userId, long maxNum, String chargingTime, int level) {
        long retTime = 0;
        if(SuperPlayerUtil.isSuperPlayer(userId)) {
            
            UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
            long refreshTime = entity.getChargingSecond();
            if (entity.getEnergyMax()<maxNum) {
                return Math.max(0, refreshTime - (TimeUtil.getNowTime() - TimeUtil.strToLong(chargingTime)) / 1000);
            }
        }
        return retTime;
    }


    /**

     */
    public static List<UserAttributeMsg> userAttributeMsg(UserAttributeMsgEntity userAttribute) {
        List<UserAttributeMsg> retList = new ArrayList<>();
        List<UserAttributeTypeEnum> typeList = UserAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            
            List<Integer> levelList = UserAttributeLvListDao.getInstance().loadMsg();
            
            typeList.forEach(enumMsg-> {
                int attributeType = enumMsg.getCode();
                retList.add(initUserAttributeMsg(attributeType, userAttribute, levelList));
            });
        }
        return retList;
    }

    /**

     */
    private static UserAttributeMsg initUserAttributeMsg(int attributeType, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList) {
        UserAttributeMsg msg = new UserAttributeMsg();
        msg.setAtbTp(attributeType);
        msg.setCsAmt(0);
        msg.setNxLvAmt("");
        boolean superPlayerFlag = SuperPlayerUtil.isSuperPlayer(userAttribute.getUserId());
        switch (attributeType){
            case 1:
                
                initExpAttribute(userAttribute, levelList, msg);
                break;
            case 2:
                
                initEnergyAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 3:
                
                initChargingAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 4:
                
                initAirdropAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 5:
                
                initLuckyAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 6:
                
                initCharmAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
        }
        return msg;
    }

    /**

     */
    private static void initExpAttribute(UserAttributeMsgEntity userAttribute, List<Integer> levelList, UserAttributeMsg msg) {
        
        int lv = userAttribute.getUserLevel();
        msg.setLv(lv);
        
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(lv+"");
        
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg(nxLv>userAttribute.getUserLevel()?YesOrNoEnum.NO.getCode():YesOrNoEnum.NO.getCode());
        msg.setCmdTp(CommodityUtil.exp());
        if(nxLv>0){
            msg.setCsAmt(configEntity.getLvExp());
            msg.setNxLvAmt(nxLv+"");
        }
        UserOnlineExpMsg expMsg = UserOnlineExpMsgDao.getInstance().loadByMsg(userAttribute.getUserId());
        msg.setSdNma(expMsg.getExpNum());
        msg.setSdDma(loadTotalExpNum(lv));
    }

    /**

     */
    private static long loadTotalExpNum(int level) {
        
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        return entity==null?0:entity.getLvExp();
    }

    /**

     */
    private static void initEnergyAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        
        int lv = userAttribute.getEnergyLevel();
        msg.setLv(lv);
        
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getEnergyMax()+"");
        
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        if(nxLv>0){
            
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getEnergyAxc());
            msg.setNxLvAmt(configEntity.getEnergyMax()+"");
        }
        msg.setLfTm(loadEnergyRefreshTime(userAttribute.getUserId(), configEntity.getEnergyMax(),
                userAttribute.getChargingTime(), userAttribute.getChargingLevel()));
        msg.setSdNma(lv);
        msg.setSdDma(userAttribute.getUserLevel());
    }

    /**

     */
    private static void initChargingAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        
        int lv = userAttribute.getChargingLevel();
        msg.setLv(lv);
        
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getChargingSecond()+"");
        
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        if(nxLv>0){
            
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getChargingAxc());
            msg.setNxLvAmt(configEntity.getChargingSecond()+"");
        }
        msg.setSdNma(lv);
        msg.setSdDma(userAttribute.getUserLevel());
    }

    /**

     */
    private static void initAirdropAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        
        int lv = userAttribute.getAirdropLevel();
        msg.setLv(lv);
        
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getAirdropCoin()+"");
        
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        if(nxLv>0){
            
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getAirdropAxc());
            msg.setNxLvAmt(configEntity.getAirdropCoin()+"");
        }
        msg.setSdNma(lv);
        msg.setSdDma(userAttribute.getUserLevel());
    }

    /**

     */
    private static void initLuckyAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute, List<Integer> levelList, UserAttributeMsg msg) {
        
        int lv = userAttribute.getLuckyLevel();
        msg.setLv(lv);
        
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getLuckyProbability()+"%");
        
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        if(nxLv>0){
            
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getLuckyAxc());
            msg.setNxLvAmt(configEntity.getLuckyProbability()+"%");
        }
        msg.setSdNma(lv);
        msg.setSdDma(userAttribute.getUserLevel());
    }

    /**

     */
    private static void initCharmAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        
        int lv = userAttribute.getCharmLevel();
        msg.setLv(lv);
        
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getCharmAddition()+"%");
        
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        if(nxLv>0){
            
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getCharmAxc());
            msg.setNxLvAmt(configEntity.getCharmAddition()+"%");
        }
        msg.setSdNma(lv);
        msg.setSdDma(userAttribute.getUserLevel());
    }

    /**

     */
    private static int loadNextLevel(int lv, List<Integer> levelList) {
        int retLv = 0;
        if(levelList.size()>0) {
            for (int nxLv : levelList) {
                if (nxLv > lv) {
                    retLv = nxLv;
                    break;
                }
            }
        }
        return retLv;
    }

    /**

     */
    public static int checkUpgradeAttribute(int userId, int attributeType) {
        int status = ClientCode.UPGRADE_CONDITION_NOT_FIT.getCode();
        if(attributeType!=UserAttributeTypeEnum.EXP_LEVEL.getCode()) {
            if(SuperPlayerUtil.isSuperPlayer(userId)) {
                
                UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                if (entity != null) {
                    int userLevel = entity.getUserLevel();
                    
                    long costAxc = attributeLevelCost(attributeType, userLevel, entity);
                    if (costAxc != -1) {
                        
                        boolean flag = UserCostLogUtil.costAttribute(userId, attributeType, costAxc);
                        if (flag) {
                            
                            UserAttributeMsgDao.getInstance().update(entity);
                            status = ClientCode.SUCCESS.getCode();
                        } else {
                            status = ClientCode.BALANCE_NO_ENOUGH.getCode();
                        }
                    }
                }
            }
        }
        return status;
    }

    /**

     */
    private static long attributeLevelCost(int attributeType, int userLevel, UserAttributeMsgEntity entity) {
        long costAxc = -1;
        int level = 1;
        if(attributeType==UserAttributeTypeEnum.ENERGY_LEVEL.getCode()){
            
            level = entity.getEnergyLevel();
        }else if(attributeType==UserAttributeTypeEnum.CHARGING_LEVEL.getCode()){
            
            level = entity.getChargingLevel();
        }else if(attributeType==UserAttributeTypeEnum.AIRDROP_LEVEL.getCode()){
            
            level = entity.getAirdropLevel();
        }else if(attributeType==UserAttributeTypeEnum.LUCKY_LEVEL.getCode()){
            
            level = entity.getLuckyLevel();
        }else if(attributeType==UserAttributeTypeEnum.CHARM_LEVEL.getCode()){
            
            level = entity.getCharmLevel();
        }
        if(userLevel>level){
            
            int nextLevel = loadNextLevel(level, UserAttributeLvListDao.getInstance().loadMsg());
            if(nextLevel>0){
                
                costAxc = loadLevelCost(attributeType, nextLevel);
                
                if(costAxc!=-1){
                    updateNextLevel(entity, attributeType, nextLevel);
                }
            }
        }
        return costAxc;
    }

    /**

     */
    private static void updateNextLevel(UserAttributeMsgEntity entity, int attributeType, int nextLevel) {
        if(attributeType==UserAttributeTypeEnum.ENERGY_LEVEL.getCode()){
            
            entity.setEnergyLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.CHARGING_LEVEL.getCode()){
            
            entity.setChargingLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.AIRDROP_LEVEL.getCode()){
            
            entity.setAirdropLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.LUCKY_LEVEL.getCode()){
            
            entity.setLuckyLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.CHARM_LEVEL.getCode()){
            
            entity.setCharmLevel(nextLevel);
        }
    }

    /**

     */
    private static long loadLevelCost(int attributeType, int level) {
        long costAxc = -1;
        
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        if(entity!=null){
            if(attributeType==UserAttributeTypeEnum.ENERGY_LEVEL.getCode()){
                
                costAxc = entity.getEnergyAxc();
            }else if(attributeType==UserAttributeTypeEnum.CHARGING_LEVEL.getCode()){
                
                costAxc = entity.getChargingAxc();
            }else if(attributeType==UserAttributeTypeEnum.AIRDROP_LEVEL.getCode()){
                
                costAxc = entity.getAirdropAxc();
            }else if(attributeType==UserAttributeTypeEnum.LUCKY_LEVEL.getCode()){
                
                costAxc = entity.getLuckyAxc();
            }else if(attributeType==UserAttributeTypeEnum.CHARM_LEVEL.getCode()){
                
                costAxc = entity.getCharmAxc();
            }
        }
        return costAxc;
    }

    /**

     */
    public static void updateChargingTime(int userId) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                2000);
        try {
            if (lock.lock()) {
                
                UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                entity.setChargingTime(TimeUtil.getNowTimeStr());
                UserAttributeMsgDao.getInstance().update(entity);
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**

     */
    public static void addUserEnergy(int userId, int num){
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                2000);
        try {
            if (lock.lock()) {
                
                UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                entity.setEnergyNum(entity.getEnergyNum()+num);
                boolean flag = UserAttributeMsgDao.getInstance().update(entity);
                if(flag){
                    if(UserOnlineUtil.isOnline(userId)){
                        
                        SchedulerSample.delayed(1, new RefreshUserEnergyNoticeTask(userId, num));
                    }
                }else{

                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }
}
