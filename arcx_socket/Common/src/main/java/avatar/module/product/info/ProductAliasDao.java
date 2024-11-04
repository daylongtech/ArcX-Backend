package avatar.module.product.info;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.concurrent.ConcurrentHashMap;

/**

 */
public class ProductAliasDao {
    private static final ProductAliasDao instance = new ProductAliasDao();
    public static final ProductAliasDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadByAlias(String alias) {
        int productId = loadCache(alias);
        if(productId==0){
            
            ProductInfoEntity productInfoEntity = loadDbByAlias(alias);
            if(productInfoEntity!=null){
                productId = productInfoEntity.getId();
            }
            if(productId!=0){
                
                setCache(alias, productId);
            }
        }
        return productId;
    }

    //=========================cache===========================

    /**

     */
    public int loadCache(String alias){
        ConcurrentHashMap<String, Integer> map = (ConcurrentHashMap<String, Integer>)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_ALIAS);
        if(map!=null){
            return map.get(alias)==null?0:map.get(alias);
        }else{
            return 0;
        }
    }

    /**

     */
    public void setCache(String alias, int productId){
        ConcurrentHashMap<String, Integer> map = (ConcurrentHashMap<String, Integer>)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_ALIAS);
        if(map==null){
            map = new ConcurrentHashMap<>();
        }
        map.put(alias, productId);
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_ALIAS, map);
    }

    /**

     */
    public void removeCache(){
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_ALIAS);
    }


    //=========================db===========================

    /**

     */
    private ProductInfoEntity loadDbByAlias(String alias) {
        String sql = "select * from product_info where alias=? ";
        return GameData.getDB().get(ProductInfoEntity.class, sql, new Object[]{alias});
    }
}
