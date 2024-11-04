package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**

 */
public class PileTowerMsgDao {
    private static final PileTowerMsgDao instance = new PileTowerMsgDao();
    public static final PileTowerMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public PileTowerMsg loadByProductId(int productId) {
        
        PileTowerMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initPileTowerMsg(productId);
            
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private PileTowerMsg loadCache(int productId) {
        return (PileTowerMsg) GameData.getCache().get(ProductPrefixMsg.PILE_TOWER_MSG+"_"+productId);
    }

    /**

     */
    public void setCache(int productId, PileTowerMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PILE_TOWER_MSG+"_"+productId, msg);
    }
}
