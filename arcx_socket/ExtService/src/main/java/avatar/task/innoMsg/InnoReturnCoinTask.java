package avatar.task.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.CoinPusherInnerReceiveDealUtil;
import avatar.util.product.InnoProductUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserBalanceUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnoReturnCoinTask extends ScheduledTask {

    private int productId;

    private int userId;

    private long onProductTime;

    public InnoReturnCoinTask(int productId, int userId, long onProductTime) {

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
                
                ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(productRoomMsg.getGamingUserId()==userId && productRoomMsg.getOnProductTime()==onProductTime){
                    int cost = ProductUtil.productCost(productId);

                    
                    long sumCostCoin = ProductGamingUtil.costCostCoin(productId, cost);
                    if(sumCostCoin>0) {
                        int preCoinWeight = InnoProductUtil.userCoinWeight(userId, productId);
                        
                        UserBalanceUtil.addUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), cost);
                        
                        CoinPusherInnerReceiveDealUtil.dealGetCoinResultProductMsg(userId, cost, productId);
                        
                        SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
                    }
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
