package avatar.task.product.general;

import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class RefreshProductMsgTask extends ScheduledTask {

    private int productId;

    public RefreshProductMsgTask(int productId) {

        this.productId = productId;
    }

    @Override
    public void run() {

        
        ProductUtil.refreshRoomMsg(productId);
    }
}
