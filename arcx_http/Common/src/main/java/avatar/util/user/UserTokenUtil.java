package avatar.util.user;

import avatar.global.basicConfig.basic.UserConfigMsg;
import avatar.util.system.TimeUtil;

import java.util.UUID;

/**

 */
public class UserTokenUtil {
    /**

     */
    public static String initUserAccessToken(int userId) {
        return "aes"+(UUID.randomUUID().toString().replaceAll("-", ""))+userId;
    }

    /**

     */
    public static long userAccessTokenOutTime(){
        return TimeUtil.getNHour(TimeUtil.getNowTimeStr(), UserConfigMsg.userAccessTokenOutTime*24);
    }

    /**

     */
    public static String initUserRefreshToken(int userId) {
        return "ref"+(UUID.randomUUID().toString().replaceAll("-", ""))+userId;
    }

    /**

     */
    public static long userRefreshTokenOutTime(){
        return TimeUtil.getNHour(TimeUtil.getNowTimeStr(), UserConfigMsg.userRefreshTokenOutTime*24);
    }
}
