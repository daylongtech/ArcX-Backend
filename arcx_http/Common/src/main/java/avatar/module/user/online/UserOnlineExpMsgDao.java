package avatar.module.user.online;

import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserOnlineUtil;

/**

 */
public class UserOnlineExpMsgDao {
    private static final UserOnlineExpMsgDao instance = new UserOnlineExpMsgDao();
    public static final UserOnlineExpMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public UserOnlineExpMsg loadByMsg(int userId){
        UserOnlineExpMsg msg = loadCache(userId);
        if(msg==null){
            
            msg = UserOnlineUtil.initUserOnlineExpMsg(userId);
            setCache(userId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private UserOnlineExpMsg loadCache(int userId){
        return (UserOnlineExpMsg)
                GameData.getCache().get(UserPrefixMsg.USER_OL_EXP+"_"+userId);
    }

    /**

     */
    public void setCache(int userId, UserOnlineExpMsg msg){
        GameData.getCache().set(UserPrefixMsg.USER_OL_EXP+"_"+userId, msg);
    }
}
