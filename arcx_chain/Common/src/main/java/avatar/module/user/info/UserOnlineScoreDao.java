package avatar.module.user.info;

import avatar.data.user.balance.UserOnlineScoreMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserBalanceUtil;

/**

 */
public class UserOnlineScoreDao {
    private static final UserOnlineScoreDao instance = new UserOnlineScoreDao();
    public static final UserOnlineScoreDao getInstance(){
        return instance;
    }

    /**

     */
    public UserOnlineScoreMsg loadByMsg(int userId, int commodityType){
        UserOnlineScoreMsg msg = loadCache(userId, commodityType);
        if(msg==null){
            
            msg = UserBalanceUtil.initUserOnlineScoreMsg(userId, commodityType);
            setCache(userId, commodityType, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private UserOnlineScoreMsg loadCache(int userId, int commodityType){
        return (UserOnlineScoreMsg)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_SCORE+"_"+userId+"_"+commodityType);
    }

    /**

     */
    public void setCache(int userId, int commodityType, UserOnlineScoreMsg msg){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_SCORE+"_"+userId+"_"+commodityType, msg);
    }
}
