package avatar.module.product.repair;

import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.List;

/**

 */
public class ProductRepairDao {
    private static final ProductRepairDao instance = new ProductRepairDao();
    public static final ProductRepairDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductRepairEntity loadByProductId(int productId){
        
        ProductRepairEntity entity = loadCache(productId);
        if(entity==null){
            
            entity = loadDbByProductId(productId);
            
            if(entity!=null){
                setCache(productId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private ProductRepairEntity loadCache(int productId) {
        return (ProductRepairEntity)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_REPAIR+"_"+productId);
    }

    /**

     */
    private void setCache(int productId, ProductRepairEntity entity) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_REPAIR+"_"+productId, entity);
    }

    /**

     */
    private void removeCache(int productId){
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_REPAIR+"_"+productId);
    }

    //=========================db===========================

    /**

     */
    private ProductRepairEntity loadDbByProductId(int productId) {
        String sql = "select * from product_repair where product_id=? and status=? order by create_time desc";
        return GameData.getDB().get(ProductRepairEntity.class, sql, new Object[]{productId, YesOrNoEnum.NO.getCode()});
    }

    /**

     */
    public boolean insert(ProductRepairEntity entity) {
        return GameData.getDB().insert(entity);
    }

    /**

     */
    public boolean update(ProductRepairEntity entity) {
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            removeCache(entity.getProductId());
        }
        return flag;
    }

    /**

     */
    public List<ProductRepairEntity> loadProductList(int productId) {
        String sql = "select * from product_repair where product_id=? and status=? order by create_time desc";
        return GameData.getDB().list(ProductRepairEntity.class, sql, new Object[]{productId, YesOrNoEnum.NO.getCode()});
    }
}
