package avatar.util.product;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.global.Config;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.task.product.pushCoin.CoinPusherGetCoinTask;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;

/**

 */
public class InnerStartGameUtil {
    /**

     */
    public static void startInitMsg(int productId, int userId, ResponseGeneralMsg responseGeneralMsg) {
        initStartGameProductMsg(productId, userId);
        
        UserOnlineUtil.startGameProduct(userId, productId, Config.getInstance().getLocalAddr(),
                Config.getInstance().getWebSocketPort());
        
        ProductGamingUtil.updateGamingUserMsg(productId, responseGeneralMsg);
        
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
    }

    /**

     */
    private static void initStartGameProductMsg(int productId, int userId) {
        
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        productRoomMsg.setOnProductTime(TimeUtil.getNowTime());
        productRoomMsg.setGamingUserId(userId);
        productRoomMsg.setPushCoinOnTime(TimeUtil.getNowTime());
        ProductRoomDao.getInstance().setCache(productId, productRoomMsg);
        if(ProductUtil.isDollMachine(productId)){
            
            initDollStartGame(productId);
        }
    }

    /**

     */
    private static void initDollStartGame(int productId) {
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        gamingMsg.setTime(gamingMsg.getTime()+1);
        gamingMsg.setInitalization(false);
        gamingMsg.setCatch(false);
        DollGamingMsgDao.getInstance().setCache(productId, gamingMsg);
    }

    /**

     */
    public static void getCoinTask(int productId, int userId) {
        
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(2000, new CoinPusherGetCoinTask(userId,
                productId, roomMsg.getOnProductTime()));
    }
}
