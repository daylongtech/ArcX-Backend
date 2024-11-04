package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserOnlineUtil;

/**

 */
public class ProductRoomDao {
    private static final ProductRoomDao instance = new ProductRoomDao();
    public static final ProductRoomDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductRoomMsg loadByProductId(int productId) {
        
        ProductRoomMsg msg = loadCache(productId);
        if(msg==null && productId>0){
            msg = ProductGamingUtil.initProductRoomMsg(productId);
            
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private ProductRoomMsg loadCache(int productId) {
        return (ProductRoomMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_ROOM+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, ProductRoomMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_ROOM+"_"+productId, msg);
    }

    /**

     */
    public void delUser(int productId, int userId) {
        ProductRoomMsg msg = loadByProductId(productId);
        if (msg != null) {
            
            if (msg.getGamingUserId() == userId) {

                
                setCache(productId, ProductGamingUtil.initProductRoomMsg(productId));
                if(ProductUtil.isDollMachine(productId)) {
                    
                    ProductGamingUtil.initDollGamingMsg(productId);
                }
                
                if(ProductUtil.isInnoProduct(productId)) {
                    ProductGamingUtil.updateInnoProductSettlementMsg(productId);
                }
                
                ProductGamingUtil.reInitProductAwardLockMsg(productId);
                
                UserOnlineUtil.onlineMsgNoGaming(userId, productId);
                
                ProductGamingUserMsgDao.getInstance().setCache(productId, ProductGamingUtil.initProductGamingUserMsg(productId));
                
                UserCostLogUtil.dealGamingResult(userId, productId);
                
                UserOperateLogUtil.settlement(userId, productId);
            }
        }
    }
}
