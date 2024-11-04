package avatar.module.user.product;

import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class UserWeightNaInnoDao {
    private static final UserWeightNaInnoDao instance = new UserWeightNaInnoDao();
    public static final UserWeightNaInnoDao getInstance(){
        return instance;
    }

    /**

     */
    public List<UserWeightNaInnoEntity> loadByUserId(int userId){
        
        List<UserWeightNaInnoEntity> list = loadCache(userId);
        if(list==null){
            
            list = loadDbByUserId(userId);
            if(list==null){
                list = new ArrayList<>();
            }
            
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<UserWeightNaInnoEntity> loadCache(int userId) {
        return (List<UserWeightNaInnoEntity>) GameData.getCache().get(UserPrefixMsg.USER_WEIGHT_NA_INNO+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, List<UserWeightNaInnoEntity> entity) {
        GameData.getCache().set(UserPrefixMsg.USER_WEIGHT_NA_INNO+"_"+userId, entity);
    }

    /**

     */
    public void removeCache(int userId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_WEIGHT_NA_INNO+"_"+userId);
    }

    //=========================db===========================

    /**

     */
    private List<UserWeightNaInnoEntity> loadDbByUserId(int userId) {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);
        String sql = SqlUtil.loadList("user_weight_na_inno", paramsMap,
                new ArrayList<>()).toString();
        return GameData.getDB().list(UserWeightNaInnoEntity.class, sql, new Object[]{});
    }
}
