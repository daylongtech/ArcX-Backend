package avatar.util.user;

import avatar.data.user.balance.UserOnlineScoreMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserPlatformBalanceEntity;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.info.UserOnlineScoreDao;
import avatar.module.user.info.UserPlatformBalanceDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.basic.general.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class UserBalanceUtil {
    /**

     */
    public static UserPlatformBalanceEntity initUserPlatformBalanceEntity(int userId, int commodityType) {
        UserPlatformBalanceEntity entity = new UserPlatformBalanceEntity();
        entity.setUserId(userId);
        entity.setCommodityType(commodityType);
        entity.setCommodityNum(0);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**


     */
    public static long getUserBalance(int userId, int commodityType) {
        
        UserPlatformBalanceEntity balanceEntity = UserPlatformBalanceDao.getInstance().loadByMsg(
                userId, commodityType);
        long totalNum = balanceEntity==null?0:balanceEntity.getCommodityNum();
        
        UserOnlineScoreMsg msg = UserOnlineScoreDao.getInstance().loadByMsg(userId, commodityType);
        totalNum += (msg.getAddNum()-msg.getCostNum());
        return totalNum;
    }

    /**

     */
    public static UserOnlineScoreMsg initUserOnlineScoreMsg(int userId, int commodityType) {
        UserOnlineScoreMsg msg = new UserOnlineScoreMsg();
        msg.setUserId(userId);
        msg.setCommodityType(commodityType);
        msg.setAddNum(0);
        msg.setCostNum(0);
        return msg;
    }

    /**

     */
    public static boolean addUserBalance(int userId, int commodityType, long num) {
        if(num>0) {
            boolean flag = false;
            
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_COST_DEAL_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    if (dealDataFlag(userId)) {
                        
                        UserPlatformBalanceEntity entity = UserPlatformBalanceDao.getInstance().loadByMsg(userId, commodityType);
                        if (entity != null) {
                            entity.setCommodityNum(entity.getCommodityNum() + num);
                            flag = UserPlatformBalanceDao.getInstance().update(entity);
                        } else {

                                    CommodityTypeEnum.getNameByCode(commodityType), num);
                        }
                    } else {
                        
                        addUserOnlineScore(userId, commodityType, num);
                        flag = true;
                    }
                    
                    UserNoticePushUtil.userBalancePush(userId);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
            return flag;
        }else{
            return true;
        }
    }

    /**


     */
    private static boolean dealDataFlag(int userId){
        return CheckUtil.isSystemMaintain() || !UserOnlineUtil.isOnline(userId);
    }

    /**

     */
    private static void addUserOnlineScore(int userId, int commodityType, long commodityNum){
        
        UserOnlineScoreMsg msg = UserOnlineScoreDao.getInstance().loadByMsg(userId, commodityType);
        msg.setAddNum(msg.getAddNum()+commodityNum);
        
        UserOnlineScoreDao.getInstance().setCache(userId, commodityType, msg);
    }

    /**

     */
    public static boolean costUserBalance(int userId, int commodityType, int costNum) {
        boolean flag = false;
        if(costNum==0){
            return true;
        }
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_COST_DEAL_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                if(getUserBalance(userId, commodityType)>=costNum) {
                    if (dealDataFlag(userId)) {
                        
                        UserPlatformBalanceEntity entity = UserPlatformBalanceDao.getInstance().loadByMsg(userId, commodityType);
                        if (entity != null) {
                            long commodityNum = entity.getCommodityNum();
                            entity.setCommodityNum(Math.max(0, (commodityNum - costNum)));
                            flag = UserPlatformBalanceDao.getInstance().update(entity);
                        } else {

                                    CommodityTypeEnum.getNameByCode(commodityType), costNum);
                        }
                    } else {
                        
                        costUserOnlineScore(userId, commodityType, costNum);
                        flag = true;
                    }
                    
                    UserNoticePushUtil.userBalancePush(userId);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }

    /**

     */
    private static void costUserOnlineScore(int userId, int commodityType, long commodityNum){
        
        UserOnlineScoreMsg msg = UserOnlineScoreDao.getInstance().loadByMsg(userId, commodityType);
        msg.setCostNum(msg.getCostNum()+commodityNum);
        
        UserOnlineScoreDao.getInstance().setCache(userId, commodityType, msg);
    }

    /**

     */
    public static boolean costStartGame(int productId, int userId, int cost) {
        boolean flag;
        if(cost==0){
            flag = true;
        }else {
            
            ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
            int commodityType = entity.getCostCommodityType();
            if(commodityType==0){
                commodityType = CommodityTypeEnum.GOLD_COIN.getCode();
            }
            flag = costUserBalance(userId, commodityType, cost);
            if(flag){
                
                UserCostLogUtil.dealLogMsg(userId, commodityType, cost*-1, productId, ProductUtil.productLog(productId));
            }
        }
        return flag;
    }

}
