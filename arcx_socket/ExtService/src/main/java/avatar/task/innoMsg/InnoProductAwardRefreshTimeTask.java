package avatar.task.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.innoMsg.SelfProductAwardMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class InnoProductAwardRefreshTimeTask extends ScheduledTask {
    private int productId;

    private int userId;

    public InnoProductAwardRefreshTimeTask(int productId, int userId) {

        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        boolean reflushFlag = true;
        RedisLock productRoomLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (productRoomLock.lock()) {
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELF_PRODUCT_AWARD_LOCK + "_" + productId,
                        2000);
                try {
                    if (lock.lock()) {
                        
                        List<Long> awardList = new ArrayList<>();
                        
                        List<Long> list = SelfProductAwardMsgDao.getInstance().loadByProductId(productId);
                        boolean flushTimeFlag = true;
                        
                        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                        long onProductTime = roomMsg.getOnProductTime();
                        if(roomMsg.getGamingUserId()!=userId){
                            flushTimeFlag = false;
                            reflushFlag = false;
                        }else{
                            ProductUtil.dealSelfProductAwardMsg(list, awardList, onProductTime);
                        }
                        
                        SelfProductAwardMsgDao.getInstance().setCache(productId, awardList);
                        if(awardList.size()==0){
                            flushTimeFlag = false;
                            reflushFlag = false;
                        }
                        if(flushTimeFlag){

                            
                            ProductDealUtil.updateProductTime(productId, userId, roomMsg);
                        }
                    }
                } catch (Exception e) {
                    ErrorDealUtil.printError(e);
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            productRoomLock.unlock();
        }
        
        if(reflushFlag){
            SchedulerSample.delayed(ProductConfigMsg.innoProductAwardRefreshTime, new InnoProductAwardRefreshTimeTask(productId, userId));
        }
    }
}
