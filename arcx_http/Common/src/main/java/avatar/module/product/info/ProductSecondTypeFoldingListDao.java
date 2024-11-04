package avatar.module.product.info;

import avatar.global.enumMsg.product.ProductStatusEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ProductSecondTypeFoldingListDao {
    private static final ProductSecondTypeFoldingListDao instance = new ProductSecondTypeFoldingListDao();
    public static final ProductSecondTypeFoldingListDao getInstance(){
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
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_SECOND_TYPE_FOLDING_LIST+"_"+secondType);
    }

    /**

     */
    private void setCache(int secondType, List<Integer> list) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_SECOND_TYPE_FOLDING_LIST+"_"+secondType, list);
    }

    /**

     */
    public void resetCache(int secondType) {
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_SECOND_TYPE_FOLDING_LIST+"_"+secondType);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbBySecondType(int secondType) {
        String sql = "select a.id from " +
                " (select id,live_url,sequence,second_sequence,@count :=@count+1 as countNum " +
                " from product_info where second_type=? and status=? order by sequence,second_sequence) a " +
                " group by a.live_url order by a.sequence,a.second_sequence";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{secondType,
                ProductStatusEnum.NORMAL.getCode()});
        return StrUtil.listSize(list)>0?list:new ArrayList<>();
    }
}
