package avatar.module.product.info;

import avatar.global.enumMsg.product.ProductStatusEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ProductTypeFordingListDao {
    private static final ProductTypeFordingListDao instance = new ProductTypeFordingListDao();
    public static final ProductTypeFordingListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadList(int productType){
        
        List<Integer> list = loadCache(productType);
        if(list==null){
            
            list = loadDbByProductType(productType);
            if(list==null){
                list = new ArrayList<>();
            }
            
            setCache(productType, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(int productType) {
        return (List<Integer>)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_TYPE_FORDING_LIST_MSG+"_"+productType);
    }

    /**

     */
    private void setCache(int productType, List<Integer> list) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_TYPE_FORDING_LIST_MSG+"_"+productType, list);
    }

    /**

     */
    public void resetCache(int productType){
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_TYPE_FORDING_LIST_MSG+"_"+productType);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbByProductType(int productType) {
        String sql = "select id from product_info where product_type=? and status=? " +
                "group by live_url order by sequence,second_sequence";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{productType,
                ProductStatusEnum.NORMAL.getCode()});
        return StrUtil.listSize(list)>0?list:new ArrayList<>();
    }
}
