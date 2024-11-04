package avatar.task.product.innoMsg;

import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnoProductWinPrizeNormalDealTask extends ScheduledTask {

    
    private int productId;

    
    private int userId;

    
    private int awardType;

    public InnoProductWinPrizeNormalDealTask(int productId, int userId, int awardType) {

        this.productId = productId;
        this.userId = userId;
        this.awardType = awardType;
    }

    @Override
    public void run() {
        
        UserUtil.updateUserGrandPrizeMsg(userId, awardType, productId);
    }
}
