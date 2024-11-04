package avatar.module.product.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class InnoProductUnlockVersionDao {
    private static final InnoProductUnlockVersionDao instance = new InnoProductUnlockVersionDao();
    public static final InnoProductUnlockVersionDao getInstance(){
        return instance;
    }

    /**

     */
    public List<String> loadMsg() {
        
        List<String> list = loadCache();
        if(list==null){
            
            list = loadDbMsg();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<String> loadCache(){
        return (List<String>) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_UNLOCK_VERSION);
    }

    /**

     */
    private void setCache(List<String> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_UNLOCK_VERSION, list);
    }

    //=========================db===========================

    /**

     */
    private List<String> loadDbMsg() {
        String sql = "select version_code from inno_product_unlock_version";
        List<String> list = GameData.getDB().listString(sql, new Object[]{});
        return StrUtil.strRetList(list);
    }
}
