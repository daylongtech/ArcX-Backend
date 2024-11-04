package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoProductCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class InnoProductCoinWeightDao {
    private static final InnoProductCoinWeightDao instance = new InnoProductCoinWeightDao();
    public static final InnoProductCoinWeightDao getInstance(){
        return instance;
    }

    /**

     */
    public List<InnoProductCoinWeightEntity> loadMsg() {
        
        List<InnoProductCoinWeightEntity> list = loadCache();
        if(list==null){
            
            list = loadDbAll();
            if(list==null){
                list = new ArrayList<>();
            }
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<InnoProductCoinWeightEntity> loadCache(){
        return (List<InnoProductCoinWeightEntity>) GameData.getCache().get(ProductPrefixMsg.INNO_PRODUCT_COIN_WEIGHT);
    }

    /**

     */
    private void setCache(List<InnoProductCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_COIN_WEIGHT, list);
    }

    //=========================db===========================

    /**

     */
    private List<InnoProductCoinWeightEntity> loadDbAll() {
        List<String> orderList = Collections.singletonList("level");
        String sql = SqlUtil.loadList("inno_product_coin_weight", orderList).toString();
        return GameData.getDB().list(InnoProductCoinWeightEntity.class, sql, new Object[]{});
    }
}
