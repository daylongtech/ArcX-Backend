package avatar.task.product.general;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.product.energy.EnergyExchangeUserAwardEntity;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.log.CostUtil;
import avatar.util.user.UserAttributeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**

 */
public class AddEnergyAwardTask extends ScheduledTask {
    
    private int productId;

    
    private int userId;

    
    private int getType;

    
    private List<GeneralAwardMsg> awardList;

    
    private List<EnergyExchangeUserAwardEntity> awardHistoryList;

    public AddEnergyAwardTask(int productId, int userId, int getType, List<GeneralAwardMsg> awardList,
            List<EnergyExchangeUserAwardEntity> awardHistoryList) {

        this.productId = productId;
        this.userId = userId;
        this.getType = getType;
        this.awardList = awardList;
        this.awardHistoryList = awardHistoryList;
    }

    @Override
    public void run() {
        
        awardList.forEach(msg->{
            if(CommodityUtil.normalFlag(msg.getCmdTp())){
                
                CostUtil.addEnergyExchange(userId, productId, msg.getCmdTp(), msg.getAwdAmt());
            }
        });
        
        UserAttributeUtil.addEnergyExchangeHistory(userId, productId, getType, awardHistoryList);

//        if(UserOnlineUtil.isOnline(userId)){
//            UserNoticePushUtil.energyAwardNotice(userId, productId, awardList);
//        }
    }
}
