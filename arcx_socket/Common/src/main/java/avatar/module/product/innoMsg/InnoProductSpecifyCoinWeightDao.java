package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoProductSpecifyCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class InnoProductSpecifyCoinWeightDao {
    private static final InnoProductSpecifyCoinWeightDao instance = new InnoProductSpecifyCoinWeightDao();
    public static final InnoProductSpecifyCoinWeightDao getInstance(){
        return instance;
    }

    /**

     */
    public List<InnoProductSpecifyCoinWeightEntity> loadBySecondType(int secondType) {
        
        List<InnoProductSpecifyCoinWeightEntity> list = loadCache(secondType);
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
    private List<InnoProductSpecifyCoinWeightEntity> loadCache(int secondType){
        return (List<InnoProductSpecifyCoinWeightEntity>) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_SPECIFY_COIN_WEIGHT+"_"+secondType);
    }

    /**

     */
    private void setCache(int secondType, List<InnoProductSpecifyCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_SPECIFY_COIN_WEIGHT+"_"+secondType, list);
    }

    //=========================db===========================

    /**

     */
    private List<InnoProductSpecifyCoinWeightEntity> loadDbBySecondType(int secondType) {
        String sql = "select * from inno_product_specify_coin_weight where second_type=? order by level";
        return GameData.getDB().list(InnoProductSpecifyCoinWeightEntity.class, sql, new Object[]{secondType});
    }
}
