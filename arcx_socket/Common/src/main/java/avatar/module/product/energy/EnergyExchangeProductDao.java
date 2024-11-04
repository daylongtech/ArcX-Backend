package avatar.module.product.energy;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class EnergyExchangeProductDao {
    private static final EnergyExchangeProductDao instance = new EnergyExchangeProductDao();
    public static final EnergyExchangeProductDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadMsg(int productId) {
        int configId = loadCache(productId);
        if(configId==-1){
            configId = loadDbByProductId(productId);
            
            setCache(configId, productId);
        }
        return configId;
    }

    //=========================cache===========================

    /**

     */
    private int loadCache(int productId){
        Object obj = GameData.getCache().get(ProductPrefixMsg.ENERGY_EXCHANGE_PRODUCT+"_"+productId);
        return obj==null?-1:(int)obj;
    }

    /**

     */
    private void setCache(int productId, int configId){
        GameData.getCache().set(ProductPrefixMsg.ENERGY_EXCHANGE_PRODUCT+"_"+productId, configId);
    }

    //=========================db===========================

    /**

     */
    private int loadDbByProductId(int productId) {
        String sql = "select config_id from energy_exchange_product where product_id=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{productId});
        return StrUtil.listNum(list);
    }

}
