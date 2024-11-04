package avatar.util.log;

import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.util.LogUtil;
import avatar.util.user.UserBalanceUtil;

/**

 */
public class CostUtil {
    /**

     */
    public static void addWelfareSignCommodity(int userId, int commodityType, int awardNum) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if(flag){
            
            UserCostLogUtil.welfareSign(userId, commodityType, awardNum);
        }else{

                    CommodityTypeEnum.getNameByCode(commodityType));
        }
    }
}
