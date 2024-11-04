package avatar.task.recharge;

import avatar.util.log.UserCostLogUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class AddOfficialRechargeCoinAwardTask extends ScheduledTask {

    
    private int userId;

    
    private long awardNum;

    public AddOfficialRechargeCoinAwardTask(int userId, long awardNum) {

        this.userId = userId;
        this.awardNum = awardNum;
    }

    @Override
    public void run() {
        
        UserCostLogUtil.officialRechargeGold(userId, awardNum);
    }

}
