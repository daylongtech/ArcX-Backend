package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.user.UserNoticePushUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class CoinPileTowerSendMsgTask extends ScheduledTask {

    private int productId;

    private int isStop;

    public CoinPileTowerSendMsgTask(int productId, int isStop) {

        this.productId = productId;
        this.isStop = isStop;
    }

    @Override
    public void run() {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null && roomMsg.getGamingUserId()!=0){
                    int userId = roomMsg.getGamingUserId();
                    if(userId>0) {
                        
                        ProductDealUtil.updateProductTime(productId, userId, roomMsg);
                        
                        UserNoticePushUtil.pileTower(userId, productId, isStop);
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
