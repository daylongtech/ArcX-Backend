package avatar.util.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.task.product.catchDoll.DollDownCatchTask;
import avatar.task.product.presentMachine.PresentDownCatchTask;
import avatar.task.product.pushCoin.CoinPusherOffLineTask;
import avatar.util.LogUtil;
import avatar.util.trigger.SchedulerSample;

/**

 */
public class InnerOffLineUtil {
    /**

     */
    public static void offLineTask(int userId, int productId) {
        
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        int offLineTime = ProductUtil.productOffLineTime(productId);
        if(offLineTime>0) {
            int productType = ProductUtil.loadProductType(productId);
            if (productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
                
                SchedulerSample.delayed(offLineTime * 1000,
                        new CoinPusherOffLineTask(userId, productId, roomMsg.getOnProductTime()));
            }
        }else{

        }
    }

    /**

     */
    public static void dollDownCatch(int productId, int userId) {
        
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(ProductUtil.productOffLineTime(productId)* 1000,
                new DollDownCatchTask(userId, productId, ProductUtil.startGameTime(productId),
                        productRoomMsg.getOnProductTime()));
    }

    /**

     */
    public static void presentDownCatch(int productId, int userId) {
        
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(ProductUtil.productOffLineTime(productId)* 1000,
                new PresentDownCatchTask(userId, productId, ProductUtil.startGameTime(productId),
                        productRoomMsg.getOnProductTime()));
    }
}
