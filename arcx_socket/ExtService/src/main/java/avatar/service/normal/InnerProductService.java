package avatar.service.normal;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.normalProduct.RequestGeneralMsg;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductGamingUserMsgDao;
import avatar.module.product.info.ProductAliasDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;

/**

 */
public class InnerProductService {
    /**

     */
    public static void describeProductMsg(ResponseGeneralMsg responseGeneralMsg) {
        String alias = responseGeneralMsg.getAlias();
        int productId = ProductAliasDao.getInstance().loadByAlias(alias);
        if(productId>0){
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    ProductGamingUtil.updateGamingUserMsg(productId, responseGeneralMsg);
                    
                    SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
    }

    /**

     */
    public static void describeGetCoinMsg(ResponseGeneralMsg responseGeneralMsg, int result) {
        int productId = ProductAliasDao.getInstance().loadByAlias(responseGeneralMsg.getAlias());
        if(productId>0) {
            int cost = ProductUtil.productCost(productId);
            if(cost>0) {
                int productType = ProductUtil.loadProductType(productId);
                int secondLevelType = ProductUtil.loadSecondType(productId);
                if (productType == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
                    
                    if (ProductUtil.isLotteryProduct(secondLevelType)) {
                        
                        getLotteryCoin(responseGeneralMsg, secondLevelType, result, cost);
                    } else {
                        
                        getCoin(responseGeneralMsg, result, cost);
                    }
                }
            }
        }
    }

    /**

     */
    private static void getCoin(ResponseGeneralMsg responseGeneralMsg, int result, int cost) {
        int userId = responseGeneralMsg.getUserId();
        int serverSideType = responseGeneralMsg.getServerSideType();
    }

    /**

     */
    private static void getLotteryCoin(ResponseGeneralMsg responseGeneralMsg, int secondLevelType,
            int result, int cost) {
        int userId = responseGeneralMsg.getUserId();
        int serverSideType = responseGeneralMsg.getServerSideType();
    }

    /**

     */
    public static void productOccupyCheck(ResponseGeneralMsg responseGeneralMsg) {
        
        int productId = ProductAliasDao.getInstance().loadByAlias(responseGeneralMsg.getAlias());
        if(productId>0) {
            int gamingUserId = 0;
            
            ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
            if(gamingUserMsg.getServerSideType()==responseGeneralMsg.getUserId() &&
                    gamingUserMsg.getServerSideType()==responseGeneralMsg.getServerSideType()){
                
                gamingUserId = gamingUserMsg.getUserId();
            }
            
            RequestGeneralMsg requestGeneralMsg = InnerNormalProductWebsocketUtil.
                    initRequestGeneralMsg(gamingUserId, productId);
            
            InnerNormalProductWebsocketUtil.sendMsg(WebsocketInnerCmd.C2S_PRODUCT_OCCUPY_CHECK,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    requestGeneralMsg);
        }
    }
}
