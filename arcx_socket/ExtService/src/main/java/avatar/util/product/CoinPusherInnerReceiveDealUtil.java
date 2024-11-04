package avatar.util.product;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.global.code.basicConfig.CoinPusherConfigMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.gaming.PileTowerMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.task.product.operate.ProductPushCoinTask;
import avatar.task.product.pushCoin.CoinPileTowerSendMsgTask;
import avatar.task.product.pushCoin.PileStopCheckTask;
import avatar.util.LogUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**

 */
public class CoinPusherInnerReceiveDealUtil {
    /**

     */
    public static void sendCoinPusherRet(int userId, int operateState, int status, int getCoin, int productId){
        JSONObject dataJson = new JSONObject();
        dataJson.put("flAmt", getCoin);
        dataJson.put("hdlTp", operateState);
        dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));
        dataJson.put("devId", productId);
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status, userId,
                dataJson);
    }

    /**

     */
    public static void dealGetCoinResultProductMsg(int userId, int result, int productId) {
        if(UserOnlineUtil.isOnline(userId)) {
            
            sendCoinPusherRet(userId, ProductOperationEnum.GET_COIN.getCode(), ClientCode.SUCCESS.getCode(), result, productId);
        }
    }

    /**

     */
    public static int pushCoin(int userId, int productId, boolean innoFreeLink) {
        int status = ClientCode.SUCCESS.getCode();
        
        int cost = ProductUtil.productCost(productId);
        boolean flag = innoFreeLink || cost==0;
        if(!flag){
            
            flag = UserBalanceUtil.costUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), cost);
        }
        if(flag) {
            int preCoinWeight = InnoProductUtil.userCoinWeight(userId, productId);
            if(!innoFreeLink) {
                
                ProductGamingUtil.addCostCoin(productId, cost);
            }
            
            pushCoinRoomMsgDeal(productId);
            
            SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
        }else{
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();
        }
        return status;
    }

    /**

     */
    private static void pushCoinRoomMsgDeal(int productId) {
        
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        roomMsg.setPushCoinOnTime(TimeUtil.getNowTime());
        ProductRoomDao.getInstance().setCache(productId, roomMsg);
    }

    /**

     */
    public static void offlineProduct(int productId, int userId) {
        
        ProductOperateUtil.offLineProduct(userId, productId);
        
        sendCoinPusherRet(userId, ProductOperationEnum.OFF_LINE.getCode(), ClientCode.SUCCESS.getCode(), 0, productId);
        if(!ProductUtil.isInnoProduct(productId)) {
            
            InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
        }
    }

    /**

     */
    public static void getCoinDeal(int userId, int productId, int result){
        
        int cost = ProductUtil.productCost(productId);
        if (cost > 0) {
            int preCoinWeight = ProductUtil.isInnoProduct(productId)?
                    InnoProductUtil.userCoinWeight(userId, productId):0;
            int score = result*cost;
            
            ProductGamingUtil.addEarnCoin(userId, productId, score);
            
            pushCoinRoomMsgDeal(productId);
            
            dealGetCoinResultProductMsg(userId, score, productId);
            
            if(preCoinWeight>0){
                SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
            }
        }
    }

    /**

     */
    public static void startGame(InnerProductJsonMapMsg jsonMapMsg) {
        int status = InnerNormalProductUtil.loadClientCode(jsonMapMsg.getStatus());
        int userId = jsonMapMsg.getUserId();
        int productId = jsonMapMsg.getProductId();
        int secondType = 0;
        if(ParamsUtil.isSuccess(status)){
            
            int cost = ProductUtil.productCost(productId);
            boolean flag = UserBalanceUtil.costStartGame(productId, userId, cost);
            secondType = ProductUtil.loadSecondType(productId);
            if(flag){
                
                ProductGamingUtil.initProductCostCoinMsg(productId);
                if(secondType== ProductSecondTypeEnum.PILE_TOWER.getCode()){
                    
                    PileTowerMsgDao.getInstance().setCache(productId, ProductGamingUtil.initPileTowerMsg(productId));
                }
                
                ProductGamingUtil.addCostCoin(productId, cost);
                
                InnerStartGameUtil.startInitMsg(productId, userId,
                        InnoParamsUtil.initResponseGeneralMsg(ProductUtil.loadProductAlias(productId), userId));
                
                InnerStartGameUtil.getCoinTask(productId, userId);
                
                InnerOffLineUtil.offLineTask(userId, productId);
                
                pushOnlineOperate(userId, productId);
                
                SchedulerSample.delayed(10, new ProductPushCoinTask(productId, userId, cost));
            }else{
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }
        }
        
        sendCoinPusherRet(userId, ProductOperationEnum.START_GAME.getCode(), status, 0, productId);
        if(ParamsUtil.isSuccess(status)){
            
            UserOperateLogUtil.startGame(userId, productId);
            
            UserNoticePushUtil.pushLotteryNotice(productId, secondType, userId, 0, 0);
        }else{
            
            InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
        }
    }

    /**

     */
    private static void pushOnlineOperate(int userId, int productId) {
        
        InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_PRODUCT_OPERATE,
                ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                InnerNormalProductWebsocketUtil.initProductOperateMap(productId, userId,
                        StrUtil.strToStrList(CoinPusherConfigMsg.pushCoinOperate)));
    }

    /**

     */
    public static void getLotteryCoin(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();
        int productId = jsonMapMsg.getProductId();
        int result = jsonMapMsg.getDataMap().get("retNum")==null?0:
                (int) jsonMapMsg.getDataMap().get("retNum");
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();
        if(userId>0 && productId>0){
            
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                
                ProductGamingUtil.addLotteryResult(userId, result, productId);
                
                ProductGamingUtil.flushPushCoinTime(productId);
                
                dealGetCoinResultProductMsg(userId, result, productId);
            }
        }else{

        }
    }

    /**

     */
    public static void getCoin(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();
        int productId = jsonMapMsg.getProductId();
        int result = jsonMapMsg.getDataMap().get("retNum")==null?0:
                (int) jsonMapMsg.getDataMap().get("retNum");
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();
        if(userId>0 && productId>0){
            
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                if(result>0){
                    
                    getCoinDeal(userId, productId, result);
                }
            }
        }else{

        }
    }

    /**

     */
    public static void pileTower(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();
        int productId = jsonMapMsg.getProductId();
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();
        if(userId>0 && productId>0){
            
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(roomMsg.getGamingUserId()==userId &&
                    roomMsg.getOnProductTime()==onProductTime){
                

                
                updatePileTime(productId, userId, onProductTime);
                
                ProductPileTowerUtil.dealPileTowerAward(userId, productId);
            }
        }else{

        }
    }

    /**

     */
    private static void updatePileTime(int productId, int userId, long onProductTime) {
        PileTowerMsg msg = PileTowerMsgDao.getInstance().loadByProductId(productId);
        if(msg!=null){
            long oriTime = msg.getPileTime();
            msg.setPileTime(TimeUtil.getNowTime());
            msg.setTillTime(msg.getTillTime()+1);
            PileTowerMsgDao.getInstance().setCache(productId, msg);
            
            if(msg.getTillTime()>=ProductConfigMsg.pileTillTime){
                SchedulerSample.delayed(100, new CoinPileTowerSendMsgTask(productId, YesOrNoEnum.NO.getCode()));
            }
            
            if(oriTime==0){

                
                SchedulerSample.delayed(ProductConfigMsg.pileStopTime,
                        new PileStopCheckTask(productId, userId, onProductTime));
            }
        }
    }
}
