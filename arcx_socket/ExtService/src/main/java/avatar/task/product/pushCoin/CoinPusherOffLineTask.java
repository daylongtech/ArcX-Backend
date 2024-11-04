package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class CoinPusherOffLineTask extends ScheduledTask {
    private int userId;

    private int productId;

    private long onProductTime;

    public CoinPusherOffLineTask(int userId, int productId, long onProductTime) {

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
                    flag = false;
                    
                    if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                        int offLineTime = ProductUtil.productOffLineTime(productId);

                                (TimeUtil.getNowTime()-roomMsg.getPushCoinOnTime()));
                        if (((TimeUtil.getNowTime() - roomMsg.getPushCoinOnTime())>=offLineTime*1000)) {
                            
                            if(ProductGamingUtil.isProductAwardLock(productId)){

                                        userId, productId);
                                SchedulerSample.delayed((offLineTime - 1) * 1000,
                                        new CoinPusherOffLineTask(userId, productId, roomMsg.getOnProductTime()));
                            }else {

                                
                                ProductSocketOperateService.coinPusherOperate(productId, ProductOperationEnum.OFF_LINE.getCode(), userId);
                            }
                        } else {
                            long checkTime = (offLineTime - 1) * 1000 - (TimeUtil.getNowTime() - roomMsg.getPushCoinOnTime());
                            if (checkTime < 1000) {
                                checkTime = 1000;
                            }
                            SchedulerSample.delayed(checkTime,
                                    new CoinPusherOffLineTask(userId, productId, roomMsg.getOnProductTime()));
                        }
                    }else{

                                productId,userId, productId, onProductTime);

                                productId,roomMsg.getGamingUserId(), productId, roomMsg.getOnProductTime());
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        
        if(flag){

            SchedulerSample.delayed(100, new CoinPusherOffLineTask(userId,productId, onProductTime));
        }
    }
}
