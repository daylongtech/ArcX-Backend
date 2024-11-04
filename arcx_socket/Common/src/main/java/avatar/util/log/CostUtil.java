package avatar.util.log;

import avatar.global.code.basicConfig.UserCostMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.module.product.pileTower.ProductPileTowerUserMsgDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductPileTowerUtil;
import avatar.util.user.UserBalanceUtil;

/**

 */
public class CostUtil {
    /**

     */
    public static void addProductPileTowerAward(int userId, int productId, int awardNum) {
        int commodityType = CommodityTypeEnum.GOLD_COIN.getCode();
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if (flag) {
            
            ProductPileTowerUserMsgDao.getInstance().insert(
                    ProductPileTowerUtil.initProductPileTowerUserMsgEntity(userId, productId, awardNum));
            
            UserCostLogUtil.dealLogMsg(userId, commodityType, awardNum, productId, UserCostMsg.pileTower);
        } else {

        }
    }

    /**

     */
    public static void addProductCommodityCoin(int userId, int productId, int commodityType, int presentCoin) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, presentCoin);
        if (flag) {
            
            UserCostLogUtil.dealLogMsg(userId, commodityType, presentCoin, productId, UserCostMsg.productAward);
        } else {

                    CommodityTypeEnum.getNameByCode(commodityType), presentCoin);
        }
    }

    /**

     */
    public static void addEnergyExchange(int userId, int productId, int commodityType, int presentCoin) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, presentCoin);
        if (flag) {
            
            UserCostLogUtil.dealLogMsg(userId, commodityType, presentCoin, productId, UserCostMsg.energyExchange);
        } else {

                    CommodityTypeEnum.getNameByCode(commodityType), presentCoin);
        }
    }
}
