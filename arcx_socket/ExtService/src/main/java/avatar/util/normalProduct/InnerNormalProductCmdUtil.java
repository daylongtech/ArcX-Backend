package avatar.util.normalProduct;

import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.*;
import avatar.util.system.JsonUtil;

/**

 */
public class InnerNormalProductCmdUtil {
    /**

     */
    public static void dealCmdMsg(InnerProductJsonMapMsg jsonMapMsg) {
        int cmd = jsonMapMsg.getCmd();
        switch (cmd) {
            case WebsocketInnerCmd.S2C_HEART :
                
                break;
            case WebsocketInnerCmd.S2C_START_GAME:
                
                startGame(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_PRODUCT_MSG:
                
                productMsg(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_DOWN_CATCH:
                
                downCatch(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_GET_COIN:
                
                getCoin(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_COIN_PILE_TOWER:
                
                pileTower(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_AUTO_SHOOT:
                
                autoShoot(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_CANCEL_AUTO_SHOOT:
                
                cancelAutoShoot(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_SETTLEMENT:
                
                settlement(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_SETTLEMENT_REFRESH:
                
                settlementRefresh(jsonMapMsg);
                break;
        }
    }

    /**

     */
    private static void startGame(InnerProductJsonMapMsg jsonMapMsg) {
        try {
            int productId = jsonMapMsg.getProductId();
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                    2000);
            try {
                if (lock.lock()) {
                    int productType = ProductUtil.loadProductType(productId);
                    if (productType == ProductTypeEnum.DOLL_MACHINE.getCode()) {
                        
                        DollInnerReceiveDealUtil.startGame(jsonMapMsg);
                    }else if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
                        
                        CoinPusherInnerReceiveDealUtil.startGame(jsonMapMsg);
                    }else if(productType== ProductTypeEnum.PRESENT_MACHINE.getCode()){
                        
                        PresentInnerReceiveDealUtil.startGame(jsonMapMsg);
                    }
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }catch (Exception e){

                    JsonUtil.mapToJson(jsonMapMsg.getDataMap()));
        }
    }

    /**

     */
    private static void productMsg(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                
                ProductGamingUtil.updateGamingUserMsg(productId, jsonMapMsg.getResponseGeneralMsg());
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    private static void downCatch(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);
                if (productType == ProductTypeEnum.DOLL_MACHINE.getCode()) {
                    
                    DollInnerReceiveDealUtil.downCatch(jsonMapMsg);
                }else if (productType == ProductTypeEnum.PRESENT_MACHINE.getCode()) {
                    
                    PresentInnerReceiveDealUtil.downCatch(jsonMapMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    public static void getCoin(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);
                int secondLevelType = ProductUtil.loadSecondType(productId);
                if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
                    
                    boolean flag = ProductUtil.checkPushCoinProductGetCoin(productId, jsonMapMsg);
                    if(flag) {
                        
                        if (ProductUtil.isLotteryProduct(secondLevelType)) {
                            
                            CoinPusherInnerReceiveDealUtil.getLotteryCoin(jsonMapMsg);
                        } else {
                            
                            CoinPusherInnerReceiveDealUtil.getCoin(jsonMapMsg);
                        }
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    private static void pileTower(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int secondType = ProductUtil.loadSecondType(productId);
                if (secondType == ProductSecondTypeEnum.PILE_TOWER.getCode()) {
                    
                    CoinPusherInnerReceiveDealUtil.pileTower(jsonMapMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    private static void autoShoot(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    private static void cancelAutoShoot(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    private static void settlement(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    private static void settlementRefresh(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
