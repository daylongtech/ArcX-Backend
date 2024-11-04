package avatar.service.product;

import avatar.util.product.ProductUtil;

/**

 */
public class ProductSocketOperateService {
    /**

     */
    public static void catchDollOperate(int productId, int operateState, int userId){
        CatchDollService.catchDollOperation(productId, operateState, userId);
    }

    /**

     */
    public static void coinPusherOperate(int productId, int operateState, int userId){
        if(ProductUtil.isInnoProduct(productId)) {
            
            CoinPusherInnoService.coinPusherOperation(productId, operateState, userId);
        }else{
            
            CoinPusherNormalService.coinPusherOperation(productId, operateState, userId);
        }
    }

    /**

     */
    public static void presentOperate(int productId, int operateState, int userId){
        PresentService.presentOperation(productId, operateState, userId);
    }
}
