package avatar.module.product.innoNaPay;

import avatar.entity.product.innoNaPay.InnoNaPayCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class InnoNaPayCoinWeightDao {
    private static final InnoNaPayCoinWeightDao instance = new InnoNaPayCoinWeightDao();
    public static final InnoNaPayCoinWeightDao getInstance(){
        return instance;
    }

    /**

     */
    public List<InnoNaPayCoinWeightEntity> loadMsg() {
        
        List<InnoNaPayCoinWeightEntity> list = loadCache();
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
    private List<InnoNaPayCoinWeightEntity> loadCache(){
        return (List<InnoNaPayCoinWeightEntity>) GameData.getCache().get(ProductPrefixMsg.INNO_NA_PAY_COIN_WEIGHT);
    }

    /**

     */
    private void setCache(List<InnoNaPayCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_COIN_WEIGHT, list);
    }

    //=========================db===========================

    /**

     */
    private List<InnoNaPayCoinWeightEntity> loadDbAll() {
        List<String> orderList = Collections.singletonList("level");
        String sql = SqlUtil.loadList("inno_na_pay_coin_weight", orderList).toString();
        return GameData.getDB().list(InnoNaPayCoinWeightEntity.class, sql, new Object[]{});
    }
}
