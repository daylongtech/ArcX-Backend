package avatar.module.product.info;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ProductGroupListDao {
    private static final ProductGroupListDao instance = new ProductGroupListDao();
    public static final ProductGroupListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadByLiveUrl(String liveUrl) {
        
        List<Integer> list = loadCache(liveUrl);
        if(list==null){
            
            list = loadDbAll(liveUrl);
            
            setCache(liveUrl, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    public List<Integer> loadCache(String liveUrl){
        return (List<Integer>) GameData.getCache().get(ProductPrefixMsg.PRODUCT_GROUP_LIST+"_"+liveUrl);
    }

    /**

     */
    public void setCache(String liveUrl, List<Integer> list){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_GROUP_LIST+"_"+liveUrl, list);
    }

    /**

     */
    public void resetCache(String liveUrl) {
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_GROUP_LIST+"_"+liveUrl);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbAll(String liveUrl) {
        String sql = "select id from product_info where live_url=? order by sequence,second_sequence";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{liveUrl});
        if(StrUtil.listSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }

}
