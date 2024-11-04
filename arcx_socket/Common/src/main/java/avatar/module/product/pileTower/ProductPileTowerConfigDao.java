package avatar.module.product.pileTower;

import avatar.entity.product.pileTower.ProductPileTowerConfigEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class ProductPileTowerConfigDao {
    private static final ProductPileTowerConfigDao instance = new ProductPileTowerConfigDao();
    public static final ProductPileTowerConfigDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductPileTowerConfigEntity loadMsg() {
        
        ProductPileTowerConfigEntity entity = loadCache();
        if(entity==null){
            
            entity = loadDbMsg();
            if(entity!=null){
                
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private ProductPileTowerConfigEntity loadCache(){
        return (ProductPileTowerConfigEntity) GameData.getCache().get(ProductPrefixMsg.PRODUCT_PILE_TOWER_CONFIG);
    }

    /**

     */
    private void setCache(ProductPileTowerConfigEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_PILE_TOWER_CONFIG, entity);
    }

    //=========================db===========================

    /**

     */
    private ProductPileTowerConfigEntity loadDbMsg() {
        String sql = "select * from product_pile_tower_config";
        return GameData.getDB().get(ProductPileTowerConfigEntity.class, sql, new Object[]{});
    }
}
