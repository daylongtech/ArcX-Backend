package avatar.module.product.info;

import avatar.data.product.gamingMsg.InnoProductOffLineMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**

 */
public class ProductSettlementMsgDao {
    private static final ProductSettlementMsgDao instance = new ProductSettlementMsgDao();
    public static final ProductSettlementMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public InnoProductOffLineMsg loadByProductId(int productId){
        
        InnoProductOffLineMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initInnoProductOffLineMsg(productId);
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private InnoProductOffLineMsg loadCache(int productId) {
        return (InnoProductOffLineMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_SETTLEMENT_MSG+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, InnoProductOffLineMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_SETTLEMENT_MSG+"_"+productId, msg);
    }

}
