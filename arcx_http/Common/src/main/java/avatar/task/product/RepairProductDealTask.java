package avatar.task.product;

import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.thirdpart.OfficialAccountUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class RepairProductDealTask extends ScheduledTask {

    private int productId;

    private int userId;

    public RepairProductDealTask(int productId, int userId) {

        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            
            OfficialAccountUtil.sendOfficalAccount(productId);
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }
}
