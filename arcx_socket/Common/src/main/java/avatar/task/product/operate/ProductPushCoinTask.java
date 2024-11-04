package avatar.task.product.operate;

import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class ProductPushCoinTask extends ScheduledTask {
    
    private int productId;

    
    private int userId;

    
    private int pushCoin;

    public ProductPushCoinTask(int productId, int userId, int pushCoin) {

        this.productId = productId;
        this.userId = userId;
        this.pushCoin = pushCoin;
    }

    @Override
    public void run() {
    }
}
