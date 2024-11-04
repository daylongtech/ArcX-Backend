package avatar.module.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class SelfProductAwardMsgDao {
    private static final SelfProductAwardMsgDao instance = new SelfProductAwardMsgDao();
    public static final SelfProductAwardMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Long> loadByProductId(int productId) {
        
        List<Long> list = loadCache(productId);
        if(list==null){
            list = new ArrayList<>();
        }
        
        setCache(productId, list);
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Long> loadCache(int productId){
        return (List<Long>) GameData.getCache().get(ProductPrefixMsg.SELF_PRODUCT_AWARD_MSG+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, List<Long> list){
        GameData.getCache().set(ProductPrefixMsg.SELF_PRODUCT_AWARD_MSG+"_"+productId, list);
    }
}
