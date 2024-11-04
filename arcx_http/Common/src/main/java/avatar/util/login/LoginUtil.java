package avatar.util.login;

import avatar.data.user.info.EmailVerifyCodeMsg;
import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.basicConfig.basic.CheckConfigMsg;
import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.basicConfig.basic.UserConfigMsg;
import avatar.global.enumMsg.basic.system.LoginWayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.HttpCmdName;
import avatar.global.lockMsg.LockMsg;
import avatar.module.basic.systemConfig.SystemConfigDao;
import avatar.module.user.info.*;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.login.LoginMsgUpdateTask;
import avatar.task.login.LoginRegisterDealTask;
import avatar.util.LogUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.*;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**

 */
public class LoginUtil {
    /**

     */
    public static int userLogin(Map<String, Object> map) {
        int loginWayType = ParamsUtil.loginWayType(map);
        String mac = ParamsUtil.macId(map);
        String code = ParamsUtil.stringParmas(map, "iosTkn");
        String userIp = ParamsUtil.ip(map);
        String email = ParamsUtil.stringParmas(map, "email");
        String thirdPartUid = "";
        int mobilePlatformType = ParamsUtil.loadMobilePlatform(map);
        String lockMsg = mac+email;
        int userId = 0;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.LOGIN_LOCK+"_"+lockMsg,
                500);
        try {
            if (lock.lock()) {
                boolean isRegister = false;
                if (loginWayType == LoginWayTypeEnum.APPLE.getCode()) {
                    
                    thirdPartUid = loadAppleUid(code);
                } else if (loginWayType == LoginWayTypeEnum.EMAIL.getCode()) {
                    
                    userId = EmailUserDao.getInstance().loadMsg(email);
                } else if(loginWayType==LoginWayTypeEnum.TOURIST.getCode()){
                    
                    userId = UserMacMsgDao.getInstance().loadByMacId(mac);
                }
                if (userId == 0) {
                    
                    if (!StrUtil.checkEmpty(thirdPartUid)) {
                        userId = UserThirdPartUidMsgDao.getInstance().loadByUid(thirdPartUid);
                    } else if (!StrUtil.checkEmpty(code) && StrUtil.checkEmpty(thirdPartUid)) {
                        userId = -1;
                    }
                }
                if (userId != -1) {
                    
                    if (userId == 0 && (loginWayType == LoginWayTypeEnum.TOURIST.getCode() || !StrUtil.checkEmpty(thirdPartUid)
                            || loginWayType == LoginWayTypeEnum.EMAIL.getCode())) {
                        String nickName = loginNickName(loadNickNamePrefix(loginWayType));
                        userId = register(nickName, userIp, email, loginWayType, mobilePlatformType);
                        isRegister = true;
                    }
                    
                    if (isRegister && userId > 0) {
                        SchedulerSample.delayed(10, new LoginRegisterDealTask(userId, userIp, mac,
                                thirdPartUid, map));
                    } else if (!isRegister && userId > 0) {
                        
                        SchedulerSample.delayed(10, new LoginMsgUpdateTask(userId, thirdPartUid, userIp, map));
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        return userId;
    }

    /**

     */
    private static String loadAppleUid(String code) {
        String sub = "";
        
        String url = CheckConfigMsg.checkIp + HttpCmdName.APPLE_USER_INFO_LOAD+"?identityToken="+code;
        
        String ret =  HttpClientUtil.sendHttpGet(url);
        if(!StrUtil.checkEmpty(ret)) {
            Map<String, Object> retMap = JsonUtil.strToMap(ret);
            if (retMap.size() > 0 && retMap.containsKey("data")) {
                String tokenStr = retMap.get("data").toString();
                if (!StrUtil.checkEmpty(tokenStr)) {
                    Map<String, Object> tokenMap = JsonUtil.strToMap(tokenStr);
                    if(tokenMap.size()>0 && tokenMap.containsKey("sub")){
                        sub = tokenMap.get("sub").toString();
                    }else{

                    }
                }
            }
        }
        return sub;
    }

    /**

     */
    private static void addBindMac(int userId, String mac) {
        int bindUserId = UserMacMsgDao.getInstance().loadByMacId(mac);
        if(userId!=bindUserId && !MacUserListMsgDao.getInstance().loadByMacId(mac).contains(userId)){
            UserMacMsgDao.getInstance().insert(UserUtil.initUserMacMsgEntity(userId, mac, YesOrNoEnum.NO.getCode()));
        }
    }

    /**

     */
    private static String loadNickNamePrefix(int loginWayType) {
        if(loginWayType==LoginWayTypeEnum.APPLE.getCode()){
            
            return UserConfigMsg.appleRegisterPrefix;
        }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode()){
            
            return UserConfigMsg.emailRegisterPrefix;
        }else{
            
            return UserConfigMsg.touristRegisterPrefix;
        }
    }

    /**

     */
    private static String loginNickName(String prefix) {
        StringBuilder str = new StringBuilder(prefix);
        String ranStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,0,1,2,3,4,5,6,7,8,9";
        List<String> ranList = StrUtil.strToStrList(ranStr);
        for(int i=0;i<5;i++){
            str.append(ranList.get((new Random()).nextInt(ranList.size())));
        }
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadDbByNickName(str.toString());
        if(userInfoEntity!=null){
            loginNickName(prefix);
        }
        return str.toString();
    }

    /**

     */
    private static int register(String nickName, String userIp, String email, int loginWayType, int mobilePlatformType) {
        String imgUrl = UserUtil.loadDefaultImg();
        
        return UserInfoDao.getInstance().insert(UserUtil.
                initUserInfoEntity(nickName, imgUrl, userIp, email, loginWayType, mobilePlatformType));
    }

    /**

     */
    public static int registerPresentCoin() {
        
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getRegisterCoin();
    }

    /**

     */
    public static void sendEmailVerifyCode(String email) {
        String verifyCode = loadVerifyCode();
        
        EmailVerifyCodeMsg msg = EmailVerifyCodeDao.getInstance().loadMsg(email);
        msg.setVerifyCode(verifyCode);
        msg.setSendTime(TimeUtil.getNowTime());
        EmailVerifyCodeDao.getInstance().setCache(email, msg);
    }

    /**

     */
    private static String loadVerifyCode() {
        if(CheckUtil.isTestEnv()){
            return "1111";
        }else {
            int num = (new Random()).nextInt(10000);
            if (num < 1000) {
                int kilobitNum = (new Random()).nextInt(10);
                if (kilobitNum == 0) {
                    kilobitNum = 1;
                }
                num += kilobitNum * 1000;
            }
            return num + "";
        }
    }

    /**

     */
    public static EmailVerifyCodeMsg initEmailVerifyCodeMsg(String email) {
        EmailVerifyCodeMsg msg = new EmailVerifyCodeMsg();
        msg.setEmail(email);
        msg.setVerifyCode("");
        msg.setSendTime(0);
        return msg;
    }

    /**

     */
    public static int verifySendEmailCode(String email) {
        int status = ClientCode.SUCCESS.getCode();
        
        EmailVerifyCodeMsg msg = EmailVerifyCodeDao.getInstance().loadMsg(email);
        if(msg.getSendTime()>0){
            if((TimeUtil.getNowTime()-msg.getSendTime())<ConfigMsg.verifyCodeSendTime){
                status = ClientCode.FREQUENTLY_SEND.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static int verifyEmailCodeOutTime(String email, String verifyCode) {
        int status = ClientCode.VERIFY_CODE_LOSE_EFFICACY.getCode();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.EMAIL_VERIFY_CODE_LOCK + "_" + email,
                2000);
        try {
            if (lock.lock()) {
                
                EmailVerifyCodeMsg msg = EmailVerifyCodeDao.getInstance().loadMsg(email);
                if (!StrUtil.checkEmpty(msg.getVerifyCode()) && msg.getSendTime() > 0) {
                    if (msg.getVerifyCode().equals(verifyCode)) {
                        if ((TimeUtil.getNowTime() - msg.getSendTime()) <= ConfigMsg.verifyCodeOutTime) {
                            status = ClientCode.SUCCESS.getCode();
                            
                            EmailVerifyCodeDao.getInstance().setCache(email,initEmailVerifyCodeMsg(email));
                        }
                    } else {
                        status = ClientCode.VERIFY_CODE_ERROR.getCode();
                    }
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
        return status;
    }

    /**

     */
    public static int checkEmailUser(String email, String pwd) {
        int status = ClientCode.PASSWORD_ERROR.getCode();
        int userId = EmailUserDao.getInstance().loadMsg(email);
        if(userId>0){
            try {
                
                UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
                if (entity != null && !StrUtil.checkEmpty(entity.getPassword()) &&
                        entity.getPassword().equals(MD5Util.MD5(pwd))){
                    status = ClientCode.SUCCESS.getCode();
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }
}
