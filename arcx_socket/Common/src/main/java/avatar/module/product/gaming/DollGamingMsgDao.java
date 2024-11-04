package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**

 */
public class DollGamingMsgDao {
    private static final DollGamingMsgDao instance = new DollGamingMsgDao();
    public static final DollGamingMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public DollGamingMsg loadByProductId(int productId) {
        
        DollGamingMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initDollGamingMsg(productId);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private DollGamingMsg loadCache(int productId) {
        return (DollGamingMsg) GameData.getCache().get(ProductPrefixMsg.DOLL_GAMING_MSG+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, DollGamingMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.DOLL_GAMING_MSG+"_"+productId, msg);
    }
}
