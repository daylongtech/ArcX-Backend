package avatar.task.recharge;

import avatar.entity.recharge.superPlayer.SuperPlayerAwardEntity;
import avatar.util.basic.CommodityUtil;
import avatar.util.log.UserCostLogUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**

 */
public class AddSuperPlayerAwardTask extends ScheduledTask {

    
    private int userId;

    
    private List<SuperPlayerAwardEntity> awardList;

    public AddSuperPlayerAwardTask(int userId, List<SuperPlayerAwardEntity> awardList) {

        this.userId = userId;
        this.awardList = awardList;
    }

    @Override
    public void run() {
        awardList.forEach(entity->{
            if(entity.getAwardNum()>0){
                int awardType = entity.getAwardType();
                int awardId = entity.getAwardId();
                int awardNum = entity.getAwardNum();
                if(awardType==CommodityUtil.gold()){
                    
                    UserCostLogUtil.superPlayerGold(userId, awardNum);
                }else if(awardType==CommodityUtil.property()){
                    
                    UserCostLogUtil.superPlayerProperty(userId, awardId, awardNum);
                }
            }
        });
    }

}
