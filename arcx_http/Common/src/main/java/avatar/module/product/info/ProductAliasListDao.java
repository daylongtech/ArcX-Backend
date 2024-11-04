package avatar.module.product.info;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ProductAliasListDao {
    private static final ProductAliasListDao instance = new ProductAliasListDao();
    public static final ProductAliasListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<String> loadAll() {
        
        List<String> list = loadCache();
        if(list==null || list.size()==0){
            
            list = loadDbAll();
            
            if(list!=null && list.size()>0){
                setCache(list);
            }
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    public List<String> loadCache(){
        return (List<String>) GameData.getCache().get(ProductPrefixMsg.PRODUCT_ALIAS_LIST);
    }

    /**

     */
    public void setCache(List<String> list){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_ALIAS_LIST, list);
    }

    /**

     */
    public void addCache(String alias){
        List<String> list = loadAll();
        if(list==null){
            list = new ArrayList<>();
        }
        if(!list.contains(alias)){
            list.add(alias);
        }
        setCache(list);
    }

    /**

     */
    public void resetCache() {
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_ALIAS_LIST);
    }

    //=========================db===========================

    /**

     */
    private List<String> loadDbAll() {
        String sql = "select alias from product_info";
        return GameData.getDB().listString(sql, new Object[]{});
    }

}
