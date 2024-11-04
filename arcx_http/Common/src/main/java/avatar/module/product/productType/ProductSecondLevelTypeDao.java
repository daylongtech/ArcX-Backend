package avatar.module.product.productType;

import avatar.entity.product.productType.ProductSecondLevelTypeEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class ProductSecondLevelTypeDao {
    private static final ProductSecondLevelTypeDao instance = new ProductSecondLevelTypeDao();
    public static final ProductSecondLevelTypeDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductSecondLevelTypeEntity loadBySecondType(int secondType) {
        
        ProductSecondLevelTypeEntity entity = loadCache(secondType);
        if(entity==null){
            
            entity = loadDbBySecondType(secondType);
            
            if(entity!=null){
                setCache(secondType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    public ProductSecondLevelTypeEntity loadCache(int secondType){
        return (ProductSecondLevelTypeEntity)
                        GameData.getCache().get(ProductPrefixMsg.PRODUCT_SECOND_LEVEL_TYPE+"_"+secondType);
    }

    /**

     */
    public void setCache(int secondType, ProductSecondLevelTypeEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_SECOND_LEVEL_TYPE+"_"+secondType, entity);
    }

    //=========================db===========================

    /**

     */
    private ProductSecondLevelTypeEntity loadDbBySecondType(int secondType){
        String sql = "select * from product_second_level_type where id=?";
        return GameData.getDB().get(ProductSecondLevelTypeEntity.class, sql, new Object[]{secondType});
    }
}
