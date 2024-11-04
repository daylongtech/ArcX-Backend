package avatar.task.product.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoAwardLockMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.product.ProductGamingUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnoAwardLockDealTask extends ScheduledTask {

    
    private InnoAwardLockMsg innoAwardLockMsg;

    
    private int productId;

    public InnoAwardLockDealTask(InnoAwardLockMsg innoAwardLockMsg, int productId) {

        this.innoAwardLockMsg = innoAwardLockMsg;
        this.productId = productId;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardLockMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardLockMsg.getServerSideType())){

                    
                    ProductGamingUtil.updateProductAwardLockMsg(productId, innoAwardLockMsg.getIsStart());
                }else{

                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
