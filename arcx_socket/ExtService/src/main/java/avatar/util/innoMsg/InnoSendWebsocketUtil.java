package avatar.util.innoMsg;

import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.util.product.CoinPusherInnerReceiveDealUtil;
import avatar.util.product.ProductUtil;

/**

 */
public class InnoSendWebsocketUtil {
    /**






     */
    public static void sendWebsocketMsg(int userId, int operateType, int status, int getCoin, int productId) {
        if(ProductUtil.loadProductType(productId) == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
            
            pushCoinMsg(userId, operateType, status, getCoin, productId);
        }
    }

    /**

     */
    private static void pushCoinMsg(int userId, int operateType, int status, int getCoin, int productId) {
        
        CoinPusherInnerReceiveDealUtil.sendCoinPusherRet(userId, operateType, status, getCoin, productId);
    }
}
