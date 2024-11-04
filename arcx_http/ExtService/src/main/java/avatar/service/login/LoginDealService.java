package avatar.service.login;

import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.info.UserInfoDao;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.LoginCheckParamsUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class LoginDealService {
    /**

     */
    public static void logoutAccount(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = CheckParamsUtil.checkAccessToken(map);
        if (ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            if(userInfoEntity!=null && userInfoEntity.getStatus()!= UserStatusEnum.LOGOUT.getCode()){
                userInfoEntity.setStatus(UserStatusEnum.LOGOUT.getCode());
                
                boolean flag = UserInfoDao.getInstance().update(userInfoEntity);
                if(!flag){
                    status = ClientCode.FAIL.getCode();
                }else{
                    
                    UserOperateLogUtil.logoutAccount(userId);
                }
            }else{
                status = ClientCode.STATUS_ERROR.getCode();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void refreshToken(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = LoginCheckParamsUtil.checkRefreshToken(map);
        if(ParamsUtil.isSuccess(status)){
            String accessToken = ParamsUtil.accessToken(map);
            String refreshToken = map.get("refTkn").toString();
            
            int userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
            
            UserTokenMsgEntity entity = UserTokenMsgDao.getInstance().loadByUserId(userId);
            if(!entity.getRefreshToken().equals(refreshToken)) {
                status = ClientCode.REFRESH_TOKEN_NOT_FIT.getCode();
            }else{
                if (entity.getRefreshOutTime() <= TimeUtil.getNowTime()) {
                    status = ClientCode.REFRESH_TOKEN_OUT_TIME.getCode();
                }else{
                    
                    UserTokenMsgEntity msgEntity = UserTokenMsgDao.getInstance().update(userId);
                    if(msgEntity!=null){
                        dataMap.put("aesTkn", msgEntity.getAccessToken());
                        dataMap.put("aesOt", msgEntity.getAccessOutTime());
                        dataMap.put("refTkn", msgEntity.getRefreshToken());
                        dataMap.put("refOt", msgEntity.getRefreshOutTime());
                    }else{
                        status = ClientCode.FAIL.getCode();
                    }
                }
                
                UserUtil.updateIpMsg(userId, ParamsUtil.ip(map));
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void userLogin(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        int status = LoginCheckParamsUtil.userLogin(map);
        if(ParamsUtil.isSuccess(status)) {
            
            int userId = LoginUtil.userLogin(map);
            if (userId == 0) {
                status = ClientCode.FAIL.getCode();
            }else if(userId==-1){
                status = ClientCode.CHECK_CODE_FAIL.getCode();
            }else if(userId>0 && UserUtil.loadUserStatus(userId)!= UserStatusEnum.NORMAL.getCode()){
                status = ClientCode.ACCOUNT_DISABLED.getCode();
            }
            if(ParamsUtil.isSuccess(status)) {
                dataMap.put("arcxUid", userId);
                
                status = UserUtil.tokenLoginRet(userId, dataMap);
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void emailVerifyCode(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = LoginCheckParamsUtil.emailVerifyCode(map);
        if(ParamsUtil.isSuccess(status)){
            String email = ParamsUtil.stringParmasNotNull(map, "email");
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.EMAIL_VERIFY_CODE_LOCK + "_" + email,
                    2000);
            try {
                if (lock.lock()) {
                    status = LoginUtil.verifySendEmailCode(email);
                    if(ParamsUtil.isSuccess(status)) {
                        
                        LoginUtil.sendEmailVerifyCode(email);
                    }
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
