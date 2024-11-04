package avatar.module.user.info;

import avatar.entity.user.info.UserPlatformBalanceEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class UserPlatformBalanceDao {
    private static final UserPlatformBalanceDao instance = new UserPlatformBalanceDao();
    public static final UserPlatformBalanceDao getInstance(){
        return instance;
    }

    /**

     */
    public UserPlatformBalanceEntity loadByMsg(int userId, int commodityType){
        UserPlatformBalanceEntity entity = loadCache(userId, commodityType);
        if(entity==null){
            
            entity = loadDbByMsg(userId, commodityType);
            if(entity==null && UserUtil.existUser(userId) && CommodityUtil.normalFlag(commodityType)){
                entity = insert(UserBalanceUtil.initUserPlatformBalanceEntity(userId, commodityType));
            }
            if(entity!=null){
                setCache(userId, commodityType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserPlatformBalanceEntity loadCache(int userId, int commodityType){
        return (UserPlatformBalanceEntity)
                GameData.getCache().get(UserPrefixMsg.USER_PLATFORM_BALANCE+"_"+userId+"_"+commodityType);
    }

    /**

     */
    private void setCache(int userId, int commodityType, UserPlatformBalanceEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_PLATFORM_BALANCE+"_"+userId+"_"+commodityType, entity);
    }

    //=========================db===========================

    /**

     */
    private UserPlatformBalanceEntity loadDbByMsg(int userId, int commodityType) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);
        paramsMap.put("commodity_type", commodityType);
        String sql = SqlUtil.getSql("user_platform_balance", paramsMap).toString();
        return GameData.getDB().get(UserPlatformBalanceEntity.class, sql, new Object[]{});
    }

    /**

     */
    private UserPlatformBalanceEntity insert(UserPlatformBalanceEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            setCache(entity.getUserId(), entity.getCommodityType(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(UserPlatformBalanceEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity.getCommodityType(), entity);
        }
        return flag;
    }
}
