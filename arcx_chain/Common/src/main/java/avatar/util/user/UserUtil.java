package avatar.util.user;

import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.basicConfig.ConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.user.info.UserInfoDao;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class UserUtil {
    /**

     */
    public static String loadAccessToken(int userId){
        
        UserTokenMsgEntity entity = UserTokenMsgDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getAccessToken();
    }

    /**

     */
    public static int loadUserIdByToken(String accessToken) {
        int userId = 0;
        if(!StrUtil.checkEmpty(accessToken)){
            
            userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
        }
        return userId;
    }

    /**

     */
    public static boolean existUser(int userId) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity!=null && entity.getStatus()== UserStatusEnum.NORMAL.getCode();
    }

    /**

     */
    public static int checkAccessToken(String accessToken) {
        int status = ClientCode.SUCCESS.getCode();
        if(!StrUtil.checkEmpty(accessToken)){
            
            int userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
            if(userId==0){
                status = ClientCode.ACCESS_TOKEN_ERROR.getCode();
            }else if(userId>0 && UserUtil.loadUserStatus(userId)!= UserStatusEnum.NORMAL.getCode()){
                status = ClientCode.ACCOUNT_DISABLED.getCode();
            }else{
                
                UserTokenMsgEntity tokenMsgEntity = UserTokenMsgDao.getInstance().loadByUserId(userId);
                if(tokenMsgEntity==null || tokenMsgEntity.getAccessOutTime()<=TimeUtil.getNowTime()){
                    status = ClientCode.ACCESS_TOKEN_OUT_TIME.getCode();
                }
            }
        }
        return status;
    }

    /**

     */
    private static int loadUserStatus(int userId) {
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null?-1:userInfoEntity.getStatus();
    }

    /**

     */
    public static boolean isTourist(String accessToken){
        return accessToken.equals(ConfigMsg.touristAccessToken);
    }

    /**

     */
    public static String loadUserIp(int userId){
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getIp();
    }

}
