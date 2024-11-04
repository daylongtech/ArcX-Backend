package avatar.module.product.gaming;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.concurrent.ConcurrentHashMap;

/**

 */
public class UserJoinProductTimeDao {
    private static final UserJoinProductTimeDao instance = new UserJoinProductTimeDao();
    public static final UserJoinProductTimeDao getInstance(){
        return instance;
    }

    //=========================cache===========================

    /**

     */
    public long loadCache(int userId, int productId) {
        ConcurrentHashMap<Integer, Long> map =
                (ConcurrentHashMap<Integer, Long>)
                        GameData.getCache().get(ProductPrefixMsg.USER_JOIN_PRODUCT_TIME+"_"+userId);
        if(map!=null && map.containsKey(productId)){
            return map.get(productId);
        }else{
            return 0;
        }
    }

    /**

     */
    public void setCache(int userId, int productId, long time) {
        ConcurrentHashMap<Integer, Long> map =
                (ConcurrentHashMap<Integer, Long>)
                        GameData.getCache().get(ProductPrefixMsg.USER_JOIN_PRODUCT_TIME+"_"+userId);
        if(map==null){
            map = new ConcurrentHashMap<>();
        }
        map.put(productId, time);
        GameData.getCache().set(ProductPrefixMsg.USER_JOIN_PRODUCT_TIME+"_"+userId, map);
    }

}
