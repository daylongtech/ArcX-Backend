package avatar.module.product.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class SelfSpecialAwardMsgDao {
    private static final SelfSpecialAwardMsgDao instance = new SelfSpecialAwardMsgDao();
    public static final SelfSpecialAwardMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public Map<Integer, Long> loadByProductId(int productId) {
        
        Map<Integer, Long> map = loadCache(productId);
        if(map==null){
            map = new HashMap<>();
        }
        
        setCache(productId, map);
        return map;
    }

    //=========================cache===========================

    /**

     */
    private Map<Integer, Long> loadCache(int productId){
        return (Map<Integer, Long>) GameData.getCache().get(ProductPrefixMsg.SELF_PRODUCT_SPECIAL_AWARD_MSG+"_"+productId);
    }

    /**

     */
    public void setCache(int productId,Map<Integer, Long> map){
        GameData.getCache().set(ProductPrefixMsg.SELF_PRODUCT_SPECIAL_AWARD_MSG+"_"+productId, map);
    }
}
