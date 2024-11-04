package avatar.module.user.info;

import avatar.entity.user.info.UserInfoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

/**

 */
public class UserInfoDao {
    private static final UserInfoDao instance = new UserInfoDao();
    public static final UserInfoDao getInstance(){
        return instance;
    }

    /**

     */
    public UserInfoEntity loadByUserId(int userId){
        
        UserInfoEntity entity = loadCache(userId);
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
    private UserInfoEntity loadCache(int userId){
        return (UserInfoEntity) GameData.getCache().get(UserPrefixMsg.USER_INFO+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, UserInfoEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_INFO+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private UserInfoEntity loadDbByUserId(int userId) {
        return GameData.getDB().get(UserInfoEntity.class, userId);
    }

    /**

     */
    public boolean update(UserInfoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getId(), entity);
        }
        return flag;
    }

}
