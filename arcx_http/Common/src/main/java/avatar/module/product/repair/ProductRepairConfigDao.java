package avatar.module.product.repair;

import avatar.entity.product.repair.ProductRepairConfigEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**

 */
public class ProductRepairConfigDao {
    private static final ProductRepairConfigDao instance = new ProductRepairConfigDao();
    public static final ProductRepairConfigDao getInstance(){
        return instance;
    }

    /**

     */
        public ProductRepairConfigEntity loadMsg(){
        ProductRepairConfigEntity entity = loadCache();
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
    private ProductRepairConfigEntity loadCache(){
        return (ProductRepairConfigEntity)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_REPAIR_CONFIG);
    }

    /**

     */
    private void setCache(ProductRepairConfigEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_REPAIR_CONFIG, entity);
    }

    //=========================db===========================

    /**

     */
    private ProductRepairConfigEntity loadDbMsg() {
        String sql = SqlUtil.getSql("product_repair_config", new HashMap<>()).toString();
        return GameData.getDB().get(ProductRepairConfigEntity.class, sql, new Object[]{});
    }
}
