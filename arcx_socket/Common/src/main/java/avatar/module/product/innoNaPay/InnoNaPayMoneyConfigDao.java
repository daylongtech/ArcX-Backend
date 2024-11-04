package avatar.module.product.innoNaPay;

import avatar.entity.product.innoNaPay.InnoNaPayMoneyConfigEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**

 */
public class InnoNaPayMoneyConfigDao {
    private static final InnoNaPayMoneyConfigDao instance = new InnoNaPayMoneyConfigDao();
    public static final InnoNaPayMoneyConfigDao getInstance(){
        return instance;
    }

    /**

     */
    public InnoNaPayMoneyConfigEntity loadMsg() {
        
        InnoNaPayMoneyConfigEntity entity = loadCache();
        if(entity==null){
            
            entity = loadDbAll();
            if(entity!=null) {
                
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private InnoNaPayMoneyConfigEntity loadCache(){
        return (InnoNaPayMoneyConfigEntity) GameData.getCache().get(ProductPrefixMsg.INNO_NA_PAY_MONEY_CONFIG);
    }

    /**

     */
    private void setCache(InnoNaPayMoneyConfigEntity entity){
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_MONEY_CONFIG, entity);
    }

    //=========================db===========================

    /**

     */
    private InnoNaPayMoneyConfigEntity loadDbAll() {
        String sql = SqlUtil.getSql("inno_na_pay_money_config", new HashMap<>()).toString();
        return GameData.getDB().get(InnoNaPayMoneyConfigEntity.class, sql, new Object[]{});
    }
}
