package avatar.module.product.innoNaPay;

import avatar.entity.product.innoNaPay.InnoNaPaySpecifyCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class InnoNaPaySpecifyCoinWeightDao {
    private static final InnoNaPaySpecifyCoinWeightDao instance = new InnoNaPaySpecifyCoinWeightDao();
    public static final InnoNaPaySpecifyCoinWeightDao getInstance(){
        return instance;
    }

    /**

     */
    public List<InnoNaPaySpecifyCoinWeightEntity> loadBySecondType(int secondType) {
        
        List<InnoNaPaySpecifyCoinWeightEntity> list = loadCache(secondType);
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
    private List<InnoNaPaySpecifyCoinWeightEntity> loadCache(int secondType){
        return (List<InnoNaPaySpecifyCoinWeightEntity>) GameData.getCache().get(
                ProductPrefixMsg.INNO_NA_PAY_SPECIFY_COIN_WEIGHT+"_"+secondType);
    }

    /**

     */
    private void setCache(int secondType, List<InnoNaPaySpecifyCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_SPECIFY_COIN_WEIGHT+"_"+secondType, list);
    }

    //=========================db===========================

    /**

     */
    private List<InnoNaPaySpecifyCoinWeightEntity> loadDbBySecondType(int secondType) {
        String sql = "select * from inno_na_pay_specify_coin_weight where second_type=? order by level";
        return GameData.getDB().list(InnoNaPaySpecifyCoinWeightEntity.class, sql, new Object[]{secondType});
    }
}
