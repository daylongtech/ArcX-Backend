package avatar.module.user.attribute;

import avatar.entity.user.attribute.UserAttributeConfigEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**

 */
public class UserAttributeConfigDao {
    private static final UserAttributeConfigDao instance = new UserAttributeConfigDao();
    public static final UserAttributeConfigDao getInstance(){
        return instance;
    }

    /**

     */
    public UserAttributeConfigEntity loadMsg(int lv){
        UserAttributeConfigEntity entity = loadCache(lv);
        if(entity==null){
            entity = loadDbByLv(lv);
            if(entity!=null) {
                setCache(lv, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserAttributeConfigEntity loadCache(int lv){
        return (UserAttributeConfigEntity)
                GameData.getCache().get(UserPrefixMsg.USER_ATTRIBUTE_CONFIG+"_"+lv);
    }

    /**

     */
    private void setCache(int lv, UserAttributeConfigEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_ATTRIBUTE_CONFIG+"_"+lv, entity);
    }

    //=========================db===========================

    /**

     */
    private UserAttributeConfigEntity loadDbByLv(int lv) {
        String sql = "select * from user_attribute_config where lv=?";
        return GameData.getDB().get(UserAttributeConfigEntity.class, sql, new Object[]{lv});
    }

}
