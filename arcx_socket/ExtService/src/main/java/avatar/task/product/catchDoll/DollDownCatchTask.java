package avatar.task.product.catchDoll;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class DollDownCatchTask extends ScheduledTask {
    private int userId;

    private int productId;

    private int time;

    private long onProductTime;

    public DollDownCatchTask(int userId, int productId, int time, long onProductTime) {

        this.userId = userId;
        this.productId = productId;
        this.time = time;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {

        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null && gamingMsg!=null){
                    
                    if(roomMsg.getGamingUserId()==userId && gamingMsg.getTime()==time && !gamingMsg.isCatch()
                            && roomMsg.getOnProductTime()==onProductTime){

                        
                        ProductSocketOperateService.catchDollOperate(productId, ProductOperationEnum.CATCH.getCode(),userId);
                    }else{

                                gamingMsg.getTime(), time, gamingMsg.isCatch(), roomMsg.getOnProductTime(), onProductTime);
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
