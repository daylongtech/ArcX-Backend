package avatar.task.product.innoMsg;

import avatar.global.enumMsg.product.award.EnergyExchangeGetTypeEnum;
import avatar.util.activity.DragonTrainUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserAttributeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnoProductDragonWinPrizeDealTask extends ScheduledTask {

    
    private int productId;

    
    private int userId;

    public InnoProductDragonWinPrizeDealTask(int productId, int userId) {

        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        
        DragonTrainUtil.addUserDragon(userId, productId);
    }
}
