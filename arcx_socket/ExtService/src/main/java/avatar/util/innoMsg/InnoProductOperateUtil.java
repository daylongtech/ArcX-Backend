package avatar.util.innoMsg;

import avatar.data.product.innoMsg.InnoReceiveProductOperateMsg;
import avatar.data.product.innoMsg.InnoReceiveStartGameMsg;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.util.product.ProductUtil;

/**

 */
public class InnoProductOperateUtil {
    /**

     */
    public static void startGame(InnoReceiveStartGameMsg startGameMsg, int productId) {
        int productType = ProductUtil.loadProductType(productId);
        if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
            
            CoinPusherInnoProductOperateUtil.startGame(startGameMsg, productId);
        }
    }

    /**

     */
    public static void productOperate(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int productType = ProductUtil.loadProductType(productId);
        if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
            
            CoinPusherInnoProductOperateUtil.productOperate(productOperateMsg, productId);
        }
    }


}
