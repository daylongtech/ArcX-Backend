package avatar.task.innoMsg;

import avatar.util.innoMsg.InnoProductSpecialUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnoPushAwardTask extends ScheduledTask {

    
    private int userId;

    
    private int productId;

    
    private int awardType;

    public InnoPushAwardTask(int userId, int productId, int awardType) {

        this.userId = userId;
        this.productId = productId;
        this.awardType = awardType;
    }

    @Override
    public void run() {
        InnoProductSpecialUtil.specialAward(userId, productId, awardType);
    }
}
