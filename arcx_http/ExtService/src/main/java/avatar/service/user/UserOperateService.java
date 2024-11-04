package avatar.service.user;

import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.UserCheckParamsUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserPropertyUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class UserOperateService {
    /**

     */
    public static void upgradeAttribute(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.upgradeAttribute(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            int attributeType = ParamsUtil.intParmasNotNull(map, "atbTp");
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = UserAttributeUtil.checkUpgradeAttribute(userId, attributeType);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void useProperty(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.useProperty(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            int propertyType = ParamsUtil.intParmasNotNull(map, "pptTp");
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = UserPropertyUtil.useProperty(userId, propertyType);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }
}
