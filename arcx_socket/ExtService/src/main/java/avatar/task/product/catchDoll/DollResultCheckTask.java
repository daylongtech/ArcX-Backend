package avatar.task.product.catchDoll;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.CatchDollResultEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.DollInnerReceiveDealUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.ArrayList;

/**

 */
public class DollResultCheckTask extends ScheduledTask {
    private int userId;

    private int productId;

    private int time;

    private long onProductTime;

    private long startGameTime;

    public DollResultCheckTask(int userId, int productId, int time, long onProductTime, long startGameTime) {

        this.userId = userId;
        this.productId = productId;
        this.time = time;
        this.onProductTime = onProductTime;
        this.startGameTime = startGameTime;
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
                    
                    if(roomMsg.getGamingUserId()==userId && gamingMsg.getTime()==time
                            && roomMsg.getOnProductTime()==onProductTime){

                        
                        DollInnerReceiveDealUtil.dealResultProductMsg(CatchDollResultEnum.LOSE.getCode(),
                                userId, productId, time, onProductTime, new ArrayList<>());
                    }else if(gamingMsg.isInitalization()){
                        
                        DollInnerReceiveDealUtil.checkInit(productId, startGameTime, userId);
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
