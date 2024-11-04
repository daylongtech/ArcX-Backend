package avatar.util.user;

import avatar.entity.user.info.UserUsdtBalanceEntity;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.info.UserUsdtBalanceDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class UserUsdtUtil {
    /**

     */
    public static UserUsdtBalanceEntity initUserUsdtBalanceEntity(int userId) {
        UserUsdtBalanceEntity entity = new UserUsdtBalanceEntity();
        entity.setUserId(userId);
        entity.setNum(0);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }

    /**

     */
    public static double usdtBalance(int userId){
        
        UserUsdtBalanceEntity entity = UserUsdtBalanceDao.getInstance().loadByMsg(userId);
        return entity==null?0:entity.getNum();
    }

    /**

     */
    public static boolean addUsdtBalance(int userId, double num) {
        if(num>0) {
            boolean flag = false;
            
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USDT_COST_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    UserUsdtBalanceEntity entity = UserUsdtBalanceDao.getInstance().loadByMsg(userId);
                    if (entity != null) {
                        entity.setNum(StrUtil.truncateFourDecimal(entity.getNum() + num));
                        flag = UserUsdtBalanceDao.getInstance().update(entity);
                    } else {

                    }
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
    public static boolean costUsdtBalance(int userId, double costNum) {
        boolean flag = false;
        if(costNum==0){
            return true;
        }
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USDT_COST_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                if(usdtBalance(userId)>=costNum) {
                    
                    UserUsdtBalanceEntity entity = UserUsdtBalanceDao.getInstance().loadByMsg(userId);
                    if (entity != null) {
                        double usdtNum = entity.getNum();
                        entity.setNum(Math.max(0, (StrUtil.truncateFourDecimal(usdtNum-costNum))));
                        flag = UserUsdtBalanceDao.getInstance().update(entity);
                    } else {

                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }
}
