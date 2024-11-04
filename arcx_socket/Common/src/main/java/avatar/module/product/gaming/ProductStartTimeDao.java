package avatar.module.product.gaming;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class ProductStartTimeDao {
    private static final ProductStartTimeDao instance = new ProductStartTimeDao();
    public static final ProductStartTimeDao getInstance(){
        return instance;
    }

    /**

     */
    public long loadByProductId(int productId) {
        
        return loadCache(productId);
    }

    //=========================cache===========================

    /**

     */
    public long loadCache(int productId){
        Object object = GameData.getCache().get(ProductPrefixMsg.PRODUCT_START_TIME+"_"+productId);
        if(object==null){
            return 0;
        }else{
            return (long) object;
        }
    }

    /**

     */
    public void setCache(int productId, long time){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_START_TIME+"_"+productId, time);
    }
}
