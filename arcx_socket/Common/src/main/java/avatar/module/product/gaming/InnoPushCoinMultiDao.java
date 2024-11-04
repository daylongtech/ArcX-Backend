package avatar.module.product.gaming;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class InnoPushCoinMultiDao {
    private static final InnoPushCoinMultiDao instance = new InnoPushCoinMultiDao();
    public static final InnoPushCoinMultiDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadBySecondType(int secondType) {
        
        List<Integer> list = loadCache(secondType);
        if(list==null){
            
            list = loadDbBySecondType(secondType);
            
            setCache(secondType, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(int secondType){
        return (List<Integer>) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_MULTI+"_"+secondType);
    }

    /**

     */
    private void setCache(int secondType, List<Integer> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_MULTI+"_"+secondType, list);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbBySecondType(int secondType) {
        String sql = "select coin_num from inno_push_coin_multi_msg where second_type=? order by coin_num desc";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{secondType});
        return StrUtil.listSize(list)>0?list:new ArrayList<>();
    }
}
