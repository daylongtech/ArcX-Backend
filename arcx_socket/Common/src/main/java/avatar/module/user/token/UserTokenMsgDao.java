package avatar.module.user.token;

import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**

 */
public class UserTokenMsgDao {
    private static final UserTokenMsgDao instance = new UserTokenMsgDao();
    public static final UserTokenMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public UserTokenMsgEntity loadByUserId(int userId){
        
        UserTokenMsgEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByUserId(userId);
            
            if(entity!=null){
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserTokenMsgEntity loadCache(int userId){
        return (UserTokenMsgEntity) GameData.getCache().get(UserPrefixMsg.USER_TOKEN_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, UserTokenMsgEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_TOKEN_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private UserTokenMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from user_token_msg where user_id=?";
        return GameData.getDB().get(UserTokenMsgEntity.class, sql, new Object[]{userId});
    }

}
