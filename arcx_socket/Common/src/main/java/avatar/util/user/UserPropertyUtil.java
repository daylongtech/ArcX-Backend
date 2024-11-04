package avatar.util.user;

import avatar.entity.user.info.UserPropertyMsgEntity;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.info.UserPropertyMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class UserPropertyUtil {
    /**

     */
    public static UserPropertyMsgEntity initUserPropertyMsgEntity(int userId, int propertyType) {
        UserPropertyMsgEntity entity = new UserPropertyMsgEntity();
        entity.setUserId(userId);
        entity.setPropertyType(propertyType);
        entity.setNum(0);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }

    /**

     */
    public static long getUserProperty(int userId, int propertyType) {
        
        UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
        return entity==null?0:entity.getNum();
    }

    /**

     */
    public static boolean addUserProperty(int userId, int propertyType, int num) {
        if(num>0) {
            boolean flag = false;
            
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
                    if (entity != null) {
                        entity.setNum(entity.getNum() + num);
                        flag = UserPropertyMsgDao.getInstance().update(entity);
                    } else {

                                PropertyTypeEnum.getNameByCode(propertyType), num);
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
}
