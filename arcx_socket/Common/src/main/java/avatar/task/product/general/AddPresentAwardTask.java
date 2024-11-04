package avatar.task.product.general;

import avatar.data.product.gamingMsg.DollAwardCommodityMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.util.log.CostUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class AddPresentAwardTask extends ScheduledTask {
    
    private int productId;

    
    private int userId;

    
    private DollAwardCommodityMsg awardMsg;

    public AddPresentAwardTask(int productId, int userId, DollAwardCommodityMsg awardMsg) {

        this.productId = productId;
        this.userId = userId;
        this.awardMsg = awardMsg;
    }

    @Override
    public void run() {
        int commodityType = awardMsg.getCommodityType();
        int awardNum = awardMsg.getAwardNum();
        if(commodityType== CommodityTypeEnum.GOLD_COIN.getCode()){
            
            CostUtil.addProductCommodityCoin(userId, productId, commodityType, awardNum);
        }
    }
}
