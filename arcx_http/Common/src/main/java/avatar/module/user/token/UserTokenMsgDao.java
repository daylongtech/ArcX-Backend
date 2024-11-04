package avatar.module.user.token;

import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserTokenUtil;

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
            if(entity==null){
                
                entity = insert(userId);

            }
            
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

    /**

     */
    private void removeCache(int userId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_TOKEN_MSG+"_"+userId);
    }

    //=========================db===========================

    /**

     */
    private UserTokenMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from user_token_msg where user_id=?";
        return GameData.getDB().get(UserTokenMsgEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    private UserTokenMsgEntity insert(int userId) {
        UserTokenMsgEntity entity = new UserTokenMsgEntity();
        entity.setUserId(userId);
        
        String accessToken = UserTokenUtil.initUserAccessToken(userId);
        entity.setAccessToken(accessToken);
        entity.setAccessOutTime(UserTokenUtil.userAccessTokenOutTime());
        
        String refreshToken = UserTokenUtil.initUserRefreshToken(userId);
        entity.setRefreshToken(refreshToken);
        entity.setRefreshOutTime(UserTokenUtil.userRefreshTokenOutTime());
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            setCache(userId, entity);
            
            UserAccessTokenDao.getInstance().setCache(accessToken, userId);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public UserTokenMsgEntity update(int userId){
        UserTokenMsgEntity entity = loadByUserId(userId);
        String oriAccessToken = entity.getAccessToken();
        
        String accessToken = UserTokenUtil.initUserAccessToken(userId);
        entity.setAccessToken(accessToken);
        entity.setAccessOutTime(UserTokenUtil.userAccessTokenOutTime());
        
        String refreshToken = UserTokenUtil.initUserRefreshToken(userId);
        entity.setRefreshToken(refreshToken);
        entity.setRefreshOutTime(UserTokenUtil.userRefreshTokenOutTime());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(userId, entity);
            
            UserAccessTokenDao.getInstance().removeCache(oriAccessToken);
            UserAccessTokenDao.getInstance().setCache(accessToken, userId);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public void deleteTokenMsg(int userId){
        UserTokenMsgEntity entity = loadByUserId(userId);
        if(entity!=null){
            
            GameData.getDB().delete(entity);
            
            removeCache(userId);
            
            UserAccessTokenDao.getInstance().removeCache(entity.getAccessToken());
        }
    }
}
