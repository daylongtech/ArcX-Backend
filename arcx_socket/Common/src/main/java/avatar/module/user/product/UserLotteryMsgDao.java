package avatar.module.user.product;

import avatar.data.product.gamingMsg.UserLotteryMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**

 */
public class UserLotteryMsgDao {
    private static final UserLotteryMsgDao instance = new UserLotteryMsgDao();
    public static final UserLotteryMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public UserLotteryMsg loadByMsg(int userId, int secondLevelType) {
        
        UserLotteryMsg msg = loadCache(userId, secondLevelType);
        if(msg==null){
            msg = ProductGamingUtil.initUserLotteryMsg(userId, secondLevelType);
            
            setCache(userId, secondLevelType, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    public UserLotteryMsg loadCache(int userId, int secondLevelType){
        return (UserLotteryMsg) GameData.getCache().get(UserPrefixMsg.USER_LOTTERY_MSG+"_"+userId+"_"+secondLevelType);
    }

    /**

     */
    public void setCache(int userId, int secondLevelType, UserLotteryMsg msg){
        GameData.getCache().set(UserPrefixMsg.USER_LOTTERY_MSG+"_"+userId+"_"+secondLevelType, msg);
    }

}
