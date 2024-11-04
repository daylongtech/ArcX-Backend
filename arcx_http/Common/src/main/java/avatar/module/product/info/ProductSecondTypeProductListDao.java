package avatar.module.product.info;

import avatar.global.enumMsg.product.ProductStatusEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ProductSecondTypeProductListDao {
    private static final ProductSecondTypeProductListDao instance = new ProductSecondTypeProductListDao();
    public static final ProductSecondTypeProductListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadList(int secondType){
        
        List<Integer> list = loadCache(secondType);
        if(list==null){
            
            list = loadDbBySecondType(secondType);
            if(list==null){
                list = new ArrayList<>();
            }
            
            setCache(secondType, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(int secondType) {
        return (List<Integer>)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_SECOND_TYPE_PRODUCT_LIST+"_"+secondType);
    }

    /**

     */
    private void setCache(int secondType, List<Integer> list) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_SECOND_TYPE_PRODUCT_LIST+"_"+secondType, list);
    }

    /**

     */
    public void resetCache(int secondType) {
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_SECOND_TYPE_PRODUCT_LIST+"_"+secondType);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbBySecondType(int secondType) {
        String sql = "select id from product_info where second_type=? and status=? " +
                "order by sequence,second_sequence";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{secondType,
                ProductStatusEnum.NORMAL.getCode()});
        return StrUtil.listSize(list)>0?list:new ArrayList<>();
    }
}
