package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class CoinPusherGetCoinTask extends ScheduledTask {
    private int userId;

    private int productId;

    private long onProductTime;

    public CoinPusherGetCoinTask(int userId, int productId, long onProductTime) {

        this.userId = userId;
        this.productId = productId;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        boolean flag = true;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null){
                    String productName = ProductUtil.loadSecondTypeName(ProductUtil.loadSecondType(productId));
                    if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                        
                        ProductSocketOperateService.coinPusherOperate(productId, ProductOperationEnum.GET_COIN.getCode(),userId);
                    }else{

                                productName, userId, productId, onProductTime);

                                productName, roomMsg.getGamingUserId(), productId,
                                roomMsg.getOnProductTime());
                        flag = false;
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        if(flag){
            
            SchedulerSample.delayed(ProductConfigMsg.getCoinTime,
                    new CoinPusherGetCoinTask(userId,productId,onProductTime));
        }
    }
}
