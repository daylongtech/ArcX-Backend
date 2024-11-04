package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductAwardLockMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**

 */
public class ProductAwardLockDao {
    private static final ProductAwardLockDao instance = new ProductAwardLockDao();
    public static final ProductAwardLockDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductAwardLockMsg loadByProductId(int productId) {
        
        ProductAwardLockMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initProductAwardLockMsg(productId);
            
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private ProductAwardLockMsg loadCache(int productId) {
        return (ProductAwardLockMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_LOCK_MSG+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, ProductAwardLockMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_LOCK_MSG+"_"+productId, msg);
    }
}
