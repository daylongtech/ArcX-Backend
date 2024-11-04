package avatar.task.product.presentMachine;

import avatar.data.product.gamingMsg.ProductRoomMsg;
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
public class PresentOffLineTask extends ScheduledTask {
    private int userId;

    private int productId;

    private int time;

    private int gamingState;

    private long onProductTime;

    public PresentOffLineTask(int userId, int productId, int time, int gamingState, long onProductTime) {

        this.userId = userId;
        this.productId = productId;
        this.time = time;
        this.gamingState = gamingState;
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
                    flag = false;
                    
                    if(roomMsg.getGamingUserId()==userId && ProductUtil.startGameTime(productId) ==time
                            && roomMsg.getOnProductTime()==onProductTime){

                        
                        ProductSocketOperateService.presentOperate(productId, ProductOperationEnum.OFF_LINE.getCode(),userId);
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        
        if(flag){

            SchedulerSample.delayed(100, new
                    PresentOffLineTask(userId,productId,time,gamingState, onProductTime));
        }
    }
}
