package avatar.module.user.attribute;

import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserAttributeUtil;

/**

 */
public class UserAttributeMsgDao {
    private static final UserAttributeMsgDao instance = new UserAttributeMsgDao();
    public static final UserAttributeMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public UserAttributeMsgEntity loadMsg(int userId){
        UserAttributeMsgEntity entity = loadCache(userId);
        if(entity==null){
            entity = loadDbByUserId(userId);
            if(entity==null){
                entity = insert(UserAttributeUtil.initUserAttributeMsgEntity(userId));
            }
            if(entity!=null) {
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserAttributeMsgEntity loadCache(int userId){
        return (UserAttributeMsgEntity)
                GameData.getCache().get(UserPrefixMsg.USER_ATTRIBUTE_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, UserAttributeMsgEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_ATTRIBUTE_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private UserAttributeMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from user_attribute_msg where user_id=?";
        return GameData.getDB().get(UserAttributeMsgEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    private UserAttributeMsgEntity insert(UserAttributeMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(UserAttributeMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }

}
