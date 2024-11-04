package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.PileTowerMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductPileTowerUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class PileStopCheckTask extends ScheduledTask {

    private int productId;

    private int userId;

    private long onProductTime;

    public PileStopCheckTask(int productId, int userId, long onProductTime) {

        this.productId = productId;
        this.userId = userId;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null && roomMsg.getGamingUserId()==userId &&
                        roomMsg.getOnProductTime()==onProductTime){
                    PileTowerMsg msg = PileTowerMsgDao.getInstance().loadByProductId(productId);
                    long checkTime = TimeUtil.getNowTime()-msg.getPileTime();
                    if(checkTime>=ProductConfigMsg.pileStopTime && msg.getTillTime()>=ProductConfigMsg.pileTillTime){

                        
                        ProductPileTowerUtil.initMsg(msg);
                        
                        SchedulerSample.delayed(100, new CoinPileTowerSendMsgTask(productId, YesOrNoEnum.YES.getCode()));
                    }else{
                        
                        SchedulerSample.delayed(ProductConfigMsg.pileStopTime-checkTime,
                                new PileStopCheckTask(productId, userId, onProductTime));
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
