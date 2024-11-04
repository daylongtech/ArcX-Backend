package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductUtil;

/**

 */
public class ProductGamingUserMsgDao {
    private static final ProductGamingUserMsgDao instance = new ProductGamingUserMsgDao();
    public static final ProductGamingUserMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductGamingUserMsg loadByProductId(int productId){
        ProductGamingUserMsg msg = loadCache(productId);
        if(msg==null){
            
            msg = ProductUtil.initProductGamingUserMsg(productId);
            
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private ProductGamingUserMsg loadCache(int productId){
        return (ProductGamingUserMsg)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_GAMING_USER+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, ProductGamingUserMsg msg){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_GAMING_USER+"_"+productId, msg);
    }


}
