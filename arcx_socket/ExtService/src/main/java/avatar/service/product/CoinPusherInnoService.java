package avatar.service.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.task.innoMsg.SyncInnoAutoPushCoinTask;
import avatar.task.innoMsg.SyncInnoEndGameTask;
import avatar.task.innoMsg.SyncInnoProductOperateTask;
import avatar.task.innoMsg.SyncInnoStartGameTask;
import avatar.task.product.operate.ProductPushCoinTask;
import avatar.util.LogUtil;
import avatar.util.innoMsg.InnoSendWebsocketUtil;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.product.*;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

/**

 */
public class CoinPusherInnoService {
    /**

     */
    public static int checkOperate(int userId, int coinMulti, int operateState, ProductRoomMsg productRoomMsg,
            boolean unlockFlag) {
        int productId = productRoomMsg.getProductId();
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        int status = ClientCode.SUCCESS.getCode();
        if(operateState== ProductOperationEnum.START_GAME.getCode() && entity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        }else if(operateState== ProductOperationEnum.START_GAME.getCode() && productRoomMsg.getGamingUserId()>0){
            status = ClientCode.PRODUCT_OCCUPY.getCode();
        } else if(productRoomMsg.getGamingUserId()!=userId && operateState!= ProductOperationEnum.START_GAME.getCode()){
            status = ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode();

                    productRoomMsg.getGamingUserId(), userId);
        } else if(operateState== ProductOperationEnum.START_GAME.getCode() &&
                !ProductUtil.isEnoughCost(userId, productId, coinMulti) && !InnoProductUtil.isInnoFreeCoin(productId)){
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();
        } else if(!unlockFlag && InnoProductUtil.isCoinMultiLowerLimit(userId, coinMulti, productId)){
            status = ClientCode.MULTI_LOCK.getCode();
        } else if(operateState== ProductOperationEnum.PUSH_COIN.getCode() && !ProductUtil.isNormalProduct(productId)){
            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        } else if(ProductUtil.isOffLine(operateState) && ProductUtil.isInnoProduct(productId) &&
                ProductGamingUtil.isProductAwardLock(productId)){

            status = ClientCode.AWARD_LOCK.getCode();
        } else if(operateState== ProductOperationEnum.START_GAME.getCode() &&
                UserOnlineUtil.isNoStreeSettlementGaming(userId)){
            status = ClientCode.GAMING_FORBID.getCode();
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            JSONObject dataJson = new JSONObject();
            dataJson.put("flAmt", 0);
            dataJson.put("hdlTp", operateState);
            dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));
            dataJson.put("devId", productId);
            if(status==ClientCode.PRODUCT_EXCEPTION.getCode()){
                
                SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION,
                        ClientCode.SUCCESS.getCode(), userId, dataJson);
            }else if(status!= ClientCode.AWARD_LOCK.getCode()){
                SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status, userId, dataJson);
            }
        }
        
        dealNoGamingUser(userId, productId, status);
        return status;
    }

    /**

     */
    private static void dealNoGamingUser(int userId, int productId, int status) {
        if(status== ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode() && ProductUtil.isInnoProduct(productId)){
            String productIp = ProductUtil.productIp(productId);
            int productPort = ProductUtil.productSocketPort(productId);
            String alias = ProductUtil.loadProductAlias(productId);
            
            SchedulerSample.delayed(1, new SyncInnoEndGameTask(
                    productIp, productPort,
                    InnoParamsUtil.initInnoEndGameMsg(productId, alias, userId)));
            
            UserOnlineMsgDao.getInstance().delete(userId, productId);
            
            ProductSocketUtil.dealOffLineSession(userId, productId);
        }
    }

    /**

     */
    public static void coinPusherOperation(int productId, int operateState, int userId) {
        int status = ClientCode.SUCCESS.getCode();
        String productIp = ProductUtil.productIp(productId);
        int productPort = ProductUtil.productSocketPort(productId);
        String alias = ProductUtil.loadProductAlias(productId);
        String linkMsg = SyncInnoConnectUtil.linkMsg(productIp, productPort);
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(productIp, productPort, linkMsg);
        if (client != null && client.isOpen()) {
            
            ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(operateState== ProductOperationEnum.START_GAME.getCode()){
                
                SchedulerSample.delayed(1, new SyncInnoStartGameTask(
                        productIp, productPort,
                        InnoParamsUtil.initInnoStartGameMsg(productId, alias, userId)));
            }else if(operateState== ProductOperationEnum.PUSH_COIN.getCode()){
                
                boolean innoFreeLink = InnoProductUtil.isInnoFreeCoin(productId);
                status = CoinPusherInnerReceiveDealUtil.pushCoin(userId, productId, innoFreeLink);
                if(ParamsUtil.isSuccess(status)) {
                    
                    SchedulerSample.delayed(1, new SyncInnoProductOperateTask(
                            productIp, productPort,
                            InnoParamsUtil.initInnoProductOperateMsg(productId, alias, userId,
                                    productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.PUSH_COIN.getCode(),
                                    innoFreeLink)));
                    
                    InnoSendWebsocketUtil.sendWebsocketMsg(userId,
                            ProductOperationEnum.PUSH_COIN.getCode(), status, 0,
                            productId);
                    
                    SchedulerSample.delayed(10, new ProductPushCoinTask(productId, userId,
                            ProductUtil.productCost(productId)));
                }
            }else if(operateState== ProductOperationEnum.ROCK.getCode()){
                
                
                SchedulerSample.delayed(1, new SyncInnoProductOperateTask(
                        productIp, productPort,
                        InnoParamsUtil.initInnoProductOperateMsg(productId, alias, userId,
                                productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.WIPER.getCode(), false)));
            }else if(operateState== ProductOperationEnum.OFF_LINE.getCode()){
                
                CoinPusherInnerReceiveDealUtil.offlineProduct(productId, userId);
                
                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.NO.getCode()));
                
                SchedulerSample.delayed(1, new SyncInnoEndGameTask(
                        productIp, productPort,
                        InnoParamsUtil.initInnoEndGameMsg(productId, alias, userId)));
            }else if(operateState== ProductOperationEnum.AUTO_PUSH_COIN.getCode()){
                
                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.YES.getCode()));
            }else if(operateState== ProductOperationEnum.CANCEL_AUTO_PUSH_COIN.getCode()){
                
                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.NO.getCode()));
            }
        }else{

            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status, userId, new JSONObject());
        }
    }
}
