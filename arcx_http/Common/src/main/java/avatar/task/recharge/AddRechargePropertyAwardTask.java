package avatar.task.recharge;

import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.util.log.UserCostLogUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class AddRechargePropertyAwardTask extends ScheduledTask {

    
    private int userId;

    
    private RechargePropertyMsgEntity entity;

    public AddRechargePropertyAwardTask(int userId, RechargePropertyMsgEntity entity) {

        this.userId = userId;
        this.entity = entity;
    }

    @Override
    public void run() {
        
        UserCostLogUtil.rechargePropertyAward(userId, entity);
    }

}
