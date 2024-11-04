package avatar.util.product;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.product.gamingMsg.DollAwardCommodityMsg;
import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.global.code.basicConfig.CatchDollConfigMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.award.EnergyExchangeGetTypeEnum;
import avatar.global.enumMsg.product.info.CatchDollResultEnum;
import avatar.global.enumMsg.product.info.GamingStateEnum;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.gaming.ProductStartTimeDao;
import avatar.service.product.ProductSocketOperateService;
import avatar.task.product.catchDoll.CatchDollOffLineTask;
import avatar.task.product.catchDoll.DollResultCheckTask;
import avatar.task.product.general.AddCatchAwardTask;
import avatar.task.product.operate.ProductPushCoinTask;
import avatar.util.LogUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class DollInnerReceiveDealUtil {
    /**

     */
    public static void startGame(InnerProductJsonMapMsg jsonMapMsg) {
        int status = InnerNormalProductUtil.loadClientCode(jsonMapMsg.getStatus());
        int userId = jsonMapMsg.getUserId();
        int productId = jsonMapMsg.getProductId();
        int time = 0;
        if(ParamsUtil.isSuccess(status)){
            
            int cost = ProductUtil.productCost(productId);
            boolean flag = UserBalanceUtil.costStartGame(productId, userId, cost);
            if(flag){
                time = ProductUtil.startGameTime(productId);
                
                ProductGamingUtil.initProductCostCoinMsg(productId);
                
                ProductGamingUtil.addCostCoin(productId, cost);
                
                InnerStartGameUtil.startInitMsg(productId, userId, jsonMapMsg.getResponseGeneralMsg());
                
                ProductStartTimeDao.getInstance().setCache(productId, TimeUtil.getNowTime());
                
                InnerOffLineUtil.dollDownCatch(productId, userId);
                
                SchedulerSample.delayed(10, new ProductPushCoinTask(productId, userId, cost));
            }else{
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }
        }
        
        sendDollRet(userId, ProductOperationEnum.START_GAME.getCode(), status, 0, productId, null);
        if(ParamsUtil.isSuccess(status) && time==0){
            
            UserOperateLogUtil.startGame(userId, productId);
        }else if(status!= ClientCode.SUCCESS.getCode()){
            
            InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
        }
    }

    /**

     */
    public static void preDownCatch(int productId) {
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        
        gamingMsg.setInitalization(true);
        gamingMsg.setCatch(true);
        DollGamingMsgDao.getInstance().setCache(productId, gamingMsg);
    }

    /**

     */
    public static void addDollResultCheckTask(int productId) {
        
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        long startGameTime = ProductStartTimeDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed((ProductConfigMsg.resetTime*2) * 1000,
                new DollResultCheckTask(productRoomMsg.getGamingUserId(), productId, ProductUtil.startGameTime(productId),
                        productRoomMsg.getOnProductTime(), startGameTime));
    }

    /**

     */
    private static void sendDollRet(int userId, int operateState, int status, int retVal, int productId,
            List<GeneralAwardMsg> awardList){
        JSONObject dataJson = new JSONObject();
        dataJson.put("dolRes", retVal);
        dataJson.put("hdlTp", operateState);
        dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));
        dataJson.put("devId", productId);
        if(awardList!=null && awardList.size()>0) {
            dataJson.put("awdTbln", awardList);
        }
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_DOLL_MACHINE_OPERATION, status, userId,
                dataJson);
    }

    /**

     */
    public static void offlineProduct(int productId, int userId, String ip) {
        
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        if(!gamingMsg.isCatch()){
            
            
            ProductSocketOperateService.catchDollOperate(productId, ProductOperationEnum.CATCH.getCode(), userId);
        }
        
        ProductOperateUtil.offLineProduct(userId, productId);
        
        sendDollRet(userId, ProductOperationEnum.OFF_LINE.getCode(), ClientCode.SUCCESS.getCode(), 0, productId, null);
        
        InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
    }

    /**

     */
    public static void dealResultProductMsg(int result, int userId, int productId, int time, long onProductTime,
            List<GeneralAwardMsg> awardList) {
        
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        boolean sendFlag = false;
        if (roomMsg != null) {
            
            int pId = UserOnlineUtil.loadOnlineProduct(userId);
            
            resetProductCache(gamingMsg);
            
            if (gamingMsg.getTime() == time && roomMsg.getOnProductTime() == onProductTime &&
                    roomMsg.getGamingUserId() == userId) {
                
                SchedulerSample.delayed(CatchDollConfigMsg.catchDollOffTime * 1000, new
                        CatchDollOffLineTask(userId, productId, gamingMsg.getTime(), gamingMsg.getGamingState(),
                        roomMsg.getOnProductTime()));
                
                if (pId == productId) {
                    sendFlag = true;
                }
            }
        }
        
        if (sendFlag) {
            
            sendDollRet(userId, ProductOperationEnum.CATCH_RESULT.getCode(), ClientCode.SUCCESS.getCode(),
                    result, productId, awardList);
        }
    }

    /**

     */
    private static void resetProductCache(DollGamingMsg gamingMsg) {
        int productId = gamingMsg.getProductId();
        gamingMsg.setInitalization(false);
        gamingMsg.setGamingState(
                GamingStateEnum.CHOOSE_LIFE_DEATH.getCode());
        
        DollGamingMsgDao.getInstance().setCache(productId, gamingMsg);
    }

    /**

     */
    public static void checkInit(int productId, long startGameTime, int userId) {
        if(ProductStartTimeDao.getInstance().loadByProductId(productId)==startGameTime){
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
            if(roomMsg!=null) {

                
                if(userId>0 && roomMsg.getGamingUserId()==userId){
                    sendDollRet(userId, ProductOperationEnum.CATCH_RESULT.getCode(),
                            ClientCode.SUCCESS.getCode(), CatchDollResultEnum.LOSE.getCode(), productId, null);
                }
                
                resetProductCache(gamingMsg);
            }
        }
    }

    /**

     */
    public static void downCatch(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();
        int productId = jsonMapMsg.getProductId();
        int result = jsonMapMsg.getDataMap().get("catchResult")==null?CatchDollResultEnum.LOSE.getCode():
                (int) jsonMapMsg.getDataMap().get("catchResult");
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();
        int time = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getGameTime();
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();
        if(userId>0 && productId>0){
            DollAwardCommodityMsg awardMsg = null;
            if(result == CatchDollResultEnum.WIN.getCode()){
                awardMsg = ProductGamingUtil.loadDollAwardMsg(productId);
            }

//            awardMsg = ProductGamingUtil.loadDollAwardMsg(productId);

            
            dealResultProductMsg(result, userId, productId, time, onProductTime,
                    ProductDealUtil.dealMachineAward(userId, productId, result,
                    EnergyExchangeGetTypeEnum.DOLL_MACHINE.getCode(), awardMsg));
            
            if (awardMsg!=null) {
                addCatchDoll(userId, productId, awardMsg);
            }
        }else{

        }
    }

    /**

     */
    private static void addCatchDoll(int userId, int productId, DollAwardCommodityMsg awardMsg) {
        
        SchedulerSample.delayed(10,
                new AddCatchAwardTask(productId, userId, awardMsg));
    }
}

