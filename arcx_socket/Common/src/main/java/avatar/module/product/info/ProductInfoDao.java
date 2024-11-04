package avatar.module.product.info;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class ProductInfoDao {
    private static final ProductInfoDao instance = new ProductInfoDao();
    public static final ProductInfoDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductInfoEntity loadByProductId(int productId){
        ProductInfoEntity entity = loadCache(productId);
        if(entity==null){
            
            entity = loadDbById(productId);
            if(entity!=null){
                setCache(productId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private ProductInfoEntity loadCache(int productId){
        return (ProductInfoEntity)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_INFO+"_"+productId);
    }

    /**

     */
    private void setCache(int productId, ProductInfoEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_INFO+"_"+productId, entity);
    }


    //=========================db===========================

    /**

     */
    private ProductInfoEntity loadDbById(int productId) {
        return GameData.getDB().get(ProductInfoEntity.class, productId);
    }

}
