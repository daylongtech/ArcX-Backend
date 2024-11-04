package avatar.util.user;

import avatar.data.user.balance.UserOnlineScoreMsg;
import avatar.entity.user.info.UserPlatformBalanceEntity;
import avatar.global.enumMsg.basic.CommodityTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.info.UserOnlineScoreDao;
import avatar.module.user.info.UserPlatformBalanceDao;
import avatar.service.jedis.RedisLock;
import avatar.task.user.RefreshUserBalanceNoticeTask;
import avatar.util.LogUtil;
import avatar.util.basic.system.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

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
                    
                    SchedulerSample.delayed(1, new RefreshUserBalanceNoticeTask(userId));
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


}
