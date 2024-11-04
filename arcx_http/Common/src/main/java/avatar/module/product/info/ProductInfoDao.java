package avatar.module.product.info;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;

import java.util.Collections;
import java.util.List;

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

    /**

     */
    public boolean update(ProductInfoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getId(), entity);
            
            ProductTypeFordingListDao.getInstance().resetCache(entity.getProductType());
            
            ProductSecondTypeFoldingListDao.getInstance().resetCache(entity.getSecondType());
            
            ProductSecondTypeProductListDao.getInstance().resetCache(entity.getSecondType());
            
            ProductGroupListDao.getInstance().resetCache(entity.getLiveUrl());
            
            ProductAliasListDao.getInstance().resetCache();
        }
        return flag;
    }

    /**

     */
    public List<ProductInfoEntity> loadDbAll() {
        List<String> orderList = Collections.singletonList(" FIELD (status,1,0,2),product_type,sequence,second_sequence,create_time ");
        StringBuffer sb = SqlUtil.loadList("product_info", orderList);
        return GameData.getDB().list(ProductInfoEntity.class, sb.toString(), new Object[]{});
    }

    /**

     */
    public ProductInfoEntity loadDbByAlias(String alias) {
        String sql = "select * from product_info where alias=?";
        return GameData.getDB().get(ProductInfoEntity.class, sql, new Object[]{alias});
    }
}
