package avatar.module.user.product;

import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserWeightUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class UserPreciseWeightNaInnoDao {
    private static final UserPreciseWeightNaInnoDao instance = new UserPreciseWeightNaInnoDao();
    public static final UserPreciseWeightNaInnoDao getInstance(){
        return instance;
    }

    /**

     */
    public UserWeightNaInnoEntity loadMsg(int userId, int secondType){
        
        UserWeightNaInnoEntity entity = loadCache(userId, secondType);
        if(entity==null){
            entity = loadDbMsg(userId, secondType);
            if(entity==null){
                entity = insert(UserWeightUtil.initUserWeightNaInnoEntity(userId, secondType));
            }
            
            if(entity!=null) {
                setCache(userId, secondType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserWeightNaInnoEntity loadCache(int userId, int secondType) {
        return (UserWeightNaInnoEntity) GameData.getCache().get(
                UserPrefixMsg.USER_PRECISE_WEIGHT_NA_INNO+"_"+userId+"_"+secondType);
    }

    /**

     */
    public void setCache(int userId,  int secondType, UserWeightNaInnoEntity entity) {
        GameData.getCache().set(UserPrefixMsg.USER_PRECISE_WEIGHT_NA_INNO+"_"+userId+"_"+secondType, entity);
    }

    //=========================db===========================

    /**

     */
    private UserWeightNaInnoEntity loadDbMsg(int userId, int secondType) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);
        paramsMap.put("second_type", secondType);
        String sql = SqlUtil.getSql("user_weight_na_inno", paramsMap).toString();
        return GameData.getDB().get(UserWeightNaInnoEntity.class, sql, new Object[]{});
    }

    /**

     */
    private UserWeightNaInnoEntity insert(UserWeightNaInnoEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            UserWeightNaInnoDao.getInstance().removeCache(entity.getUserId());
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public void update(UserWeightNaInnoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity.getSecondType(), entity);
            
            UserWeightNaInnoDao.getInstance().removeCache(entity.getUserId());
        }
    }
}
