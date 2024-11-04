package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductCostCoinMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**

 */
public class ProductCostCoinMsgDao {
    private static final ProductCostCoinMsgDao instance = new ProductCostCoinMsgDao();
    public static final ProductCostCoinMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductCostCoinMsg loadByProductId(int productId) {
        
        ProductCostCoinMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initProductCostCoinMsg(productId);
            
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private ProductCostCoinMsg loadCache(int productId) {
        return (ProductCostCoinMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_COST_COIN_MSG+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, ProductCostCoinMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_COST_COIN_MSG+"_"+productId, msg);
    }
}
