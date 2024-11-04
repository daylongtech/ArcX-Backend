package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.UserStartGameMultiMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**

 */
public class UserStartGameMultiMsgDao {
    private static final UserStartGameMultiMsgDao instance = new UserStartGameMultiMsgDao();
    public static final UserStartGameMultiMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public UserStartGameMultiMsg loadByUserId(int userId) {
        
        UserStartGameMultiMsg msg = loadCache(userId);
        if(msg==null){
            msg = ProductGamingUtil.initUserStartGameMultiMsg(userId);
            
            setCache(userId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private UserStartGameMultiMsg loadCache(int userId) {
        return (UserStartGameMultiMsg) GameData.getCache().get(ProductPrefixMsg.USER_START_GAME_MULTI_MSG+"_"+userId);
    }

    /**

     */
    public void setCache(int userId, UserStartGameMultiMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.USER_START_GAME_MULTI_MSG+"_"+userId, msg);
    }
}
