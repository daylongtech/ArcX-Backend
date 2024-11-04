package avatar.module.user.info;

import avatar.entity.user.info.UserPropertyMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserPropertyUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class UserPropertyMsgDao {
    private static final UserPropertyMsgDao instance = new UserPropertyMsgDao();
    public static final UserPropertyMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public UserPropertyMsgEntity loadByMsg(int userId, int propertyType){
        UserPropertyMsgEntity entity = loadCache(userId, propertyType);
        if(entity==null){
            
            entity = loadDbByMsg(userId, propertyType);
            if(entity==null && UserUtil.existUser(userId)){
                entity = insert(UserPropertyUtil.initUserPropertyMsgEntity(userId, propertyType));
            }
            if(entity!=null){
                setCache(userId, propertyType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserPropertyMsgEntity loadCache(int userId, int propertyType){
        return (UserPropertyMsgEntity)
                GameData.getCache().get(UserPrefixMsg.USER_PROPERTY_MSG+"_"+userId+"_"+propertyType);
    }

    /**

     */
    private void setCache(int userId, int propertyType, UserPropertyMsgEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_PROPERTY_MSG+"_"+userId+"_"+propertyType, entity);
    }

    //=========================db===========================

    /**

     */
    private UserPropertyMsgEntity loadDbByMsg(int userId, int commodityType) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);
        paramsMap.put("property_type", commodityType);
        String sql = SqlUtil.getSql("user_property_msg", paramsMap).toString();
        return GameData.getDB().get(UserPropertyMsgEntity.class, sql, new Object[]{});
    }

    /**

     */
    private UserPropertyMsgEntity insert(UserPropertyMsgEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            setCache(entity.getUserId(), entity.getPropertyType(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(UserPropertyMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity.getPropertyType(), entity);
        }
        return flag;
    }

}
