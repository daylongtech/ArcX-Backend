package avatar.module.user.info;

import avatar.entity.user.info.UserUsdtBalanceEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUsdtUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class UserUsdtBalanceDao {
    private static final UserUsdtBalanceDao instance = new UserUsdtBalanceDao();
    public static final UserUsdtBalanceDao getInstance(){
        return instance;
    }

    /**

     */
    public UserUsdtBalanceEntity loadByMsg(int userId){
        UserUsdtBalanceEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByMsg(userId);
            if(entity==null && UserUtil.existUser(userId)){
                entity = insert(UserUsdtUtil.initUserUsdtBalanceEntity(userId));
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
    private UserUsdtBalanceEntity loadCache(int userId){
        return (UserUsdtBalanceEntity)
                GameData.getCache().get(UserPrefixMsg.USER_USDT_BALANCE+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, UserUsdtBalanceEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_USDT_BALANCE+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private UserUsdtBalanceEntity loadDbByMsg(int userId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);
        String sql = SqlUtil.getSql("user_usdt_balance", paramsMap).toString();
        return GameData.getDB().get(UserUsdtBalanceEntity.class, sql, new Object[]{});
    }

    /**

     */
    private UserUsdtBalanceEntity insert(UserUsdtBalanceEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            setCache(entity.getUserId(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(UserUsdtBalanceEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
