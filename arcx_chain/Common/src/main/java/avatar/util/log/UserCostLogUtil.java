package avatar.util.log;

import avatar.global.basicConfig.UserCostMsg;
import avatar.global.enumMsg.basic.CommodityTypeEnum;
import avatar.global.enumMsg.system.TokensTypeEnum;
import avatar.util.LogUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUsdtUtil;

/**

 */
public class UserCostLogUtil {
    /**

     */
    public static void rechargeBalance(int userId, int tokenType, double costNum) {
        String costMsg = UserCostMsg.chainWalletRecharge;
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            int commodityType = CommodityTypeEnum.AXC.getCode();//AXC
            boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, (long)costNum);
            if(flag){
                UserOperateLogUtil.costBalance((long)costNum, userId, commodityType, costMsg);
            }else{

                        CommodityTypeEnum.getNameByCode(commodityType), costNum);
            }
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //USDT
            boolean flag = UserUsdtUtil.addUsdtBalance(userId, costNum);
            if(flag){
                UserOperateLogUtil.costUsdt(costNum, userId, costMsg);
            }else{

            }
        }
    }
}
