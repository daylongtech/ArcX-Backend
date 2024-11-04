package avatar.util.log;

import avatar.data.product.gamingMsg.ProductCostCoinMsg;
import avatar.global.code.basicConfig.UserCostMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductCostCoinMsgDao;
import avatar.module.product.pileTower.ProductPileTowerUserMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.ProductUserWeightNaTask;
import avatar.util.CommonUtil;
import avatar.util.LogUtil;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductPileTowerUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserPropertyUtil;
import avatar.util.user.UserUtil;

/**

 */
public class UserCostLogUtil {
    /**


     */
    public static void dealGamingResult(int userId, int productId) {
        String costMsg = ProductUtil.productLog(productId);
        long sumAddCoin = 0;
        long sumCostCoin = 0;
        
        
        RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_COST_COIN_LOCK+"_"+productId,
                2000);
        try {
            if (pushCoinLock.lock()) {
                ProductCostCoinMsg costCoinMsg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                if (costCoinMsg!=null) {
                    sumAddCoin = costCoinMsg.getSumAddCoin();
                    sumCostCoin = costCoinMsg.getSumCostCoin();
                    
                    ProductCostCoinMsgDao.getInstance().setCache(productId, ProductGamingUtil.initProductCostCoinMsg(productId));
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            pushCoinLock.unlock();
        }
        
        if(sumCostCoin>0){
            
            dealLogMsg(userId, CommodityTypeEnum.GOLD_COIN.getCode(), sumCostCoin*-1, 0, costMsg);
        }
        if(sumAddCoin>0){
            
            dealLogMsg(userId, CommodityTypeEnum.GOLD_COIN.getCode(), sumAddCoin, productId, costMsg);
        }
        
        if(sumAddCoin-sumCostCoin>0){
            if(ProductUtil.isInnoProduct(productId)) {
                SchedulerSample.delayed(10,
                        new ProductUserWeightNaTask(productId, userId, sumAddCoin-sumCostCoin));
            }
        }
    }

    /**

     */
    public static void dealLogMsg(int userId, int commodityType, long awardNum, int productId, String costMsg){
        
        UserOperateLogUtil.costBalance(awardNum, userId, productId, commodityType, costMsg);
    }

    /**

     */
    public static void addDragonAward(int userId, int productId, int awardNum) {
        int commodityType = CommodityUtil.gold();
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if (flag) {
            
            UserCostLogUtil.dealLogMsg(userId, commodityType, awardNum, productId, UserCostMsg.dragonBall);
        } else {

        }
    }

    /**

     */
    public static void dragonTrainAward(int userId, int productId, int awardNum, int awardType) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, awardType, awardNum);
        if (flag) {
            
            UserCostLogUtil.dealLogMsg(userId, awardType, awardNum, productId, UserCostMsg.dragonTrain);
        } else {

                    CommodityTypeEnum.getNameByCode(awardType));
        }
    }

    /**

     */
    public static void dragonTrainProperty(int userId, int awardId, int awardNum) {
        boolean flag = UserPropertyUtil.addUserProperty(userId, awardId, awardNum);
        if(flag){
            
            UserOperateLogUtil.costProperty(awardNum, userId, awardId, UserCostMsg.dragonTrain);
        }else{

                    PropertyTypeEnum.getNameByCode(awardId));
        }
    }
}
