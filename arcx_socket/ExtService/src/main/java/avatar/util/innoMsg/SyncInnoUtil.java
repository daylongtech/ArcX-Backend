package avatar.util.innoMsg;

import avatar.data.product.innoMsg.*;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.service.innoMsg.InnoReceiveMsgService;
import avatar.task.innoMsg.SyncInnoChangeCoinWeightTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.InnoProductUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
public class SyncInnoUtil {

    /**

     */
    public static void dealMsg(String jsonStr) {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            
            if(checkEncode(jsonStr)) {
                int cmd = dealCmd(jsonStr);
                if(cmd>0){
                    Map<String, Object> jsonMap = (Map<String, Object>) JsonUtil.strToMap(jsonStr).get("param");
                    switch (cmd) {
                        case SelfInnoWebsocketInnerCmd.S2P_START_GAME :
                            
                            InnoReceiveMsgService.startGame(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_OPERATE_MSG :
                            
                            InnoReceiveMsgService.productOperate(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_START_GAME_GAMING_USER_CHECK :
                            
                            InnoReceiveMsgService.startGmmeOccopyCheck(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_PRODUCT_MSG:
                            
                            InnoReceiveMsgService.productMsg(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_AWARD_LOCK_DEAL:
                            
                            InnoReceiveMsgService.awardLockDealMsg(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_SETTLEMENT_WINDOW:
                            
                            InnoReceiveMsgService.settlementWindowMsg(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_AWARD_SCORE_MULTI:
                            
                            InnoReceiveMsgService.awardScoreMulti(jsonMap);
                            break;
                    }
                }else{

                }
            }
        });
        cachedPool.shutdown();
    }

    /**

     */
    private static boolean checkEncode(String jsonStr) {
        boolean flag = false;
        try{
            if(!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if (jsonMap != null && jsonMap.size() > 0) {
                    Map<String, Object> paramMap = (Map<String, Object>) jsonMap.get("param");
                    flag = InnerEnCodeUtil.checkEncode(paramMap);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**

     */
    private static int dealCmd(String jsonStr) {
        int cmd = 0;
        try {
            if (!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if(jsonMap!=null && jsonMap.size()>0) {
                    
                    cmd = (int) jsonMap.get("cmd");
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return cmd;
    }

    /**

     */
    public static void heart(SyncInnoClient client) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_HEART, hostId, new HashMap<>());
        }
    }

    /**

     */
    public static void startGame(SyncInnoClient client, InnoStartGameMsg startGameMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_START_GAME, hostId,
                    InnoParamsUtil.initStartGameMsg(startGameMsg));
        }else{

                    startGameMsg.getAlias());
        }
    }

    /**

     */
    public static void endGame(SyncInnoClient client, InnoEndGameMsg endGameMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_END_GAME, hostId,
                    InnoParamsUtil.initEndGameMsg(endGameMsg));
        }else{

                    endGameMsg.getAlias());
        }
    }

    /**

     */
    public static void productOperate(SyncInnoClient client, InnoProductOperateMsg productOperateMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_OPERATE_MSG, hostId,
                    InnoParamsUtil.initProductOperateMsg(productOperateMsg));
        }else{

                    productOperateMsg.getAlias(),
                    InnoProductOperateTypeEnum.getNameByCode(productOperateMsg.getInnoProductOperateType()));
        }
    }

    /**

     */
    public static void startGameOccupy(SyncInnoClient client, InnoStartGameOccupyMsg startGameOccupyMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_START_GAME_GAMING_USER_CHECK, hostId,
                    InnoParamsUtil.initStartGameOccupyMsg(startGameOccupyMsg));
        }else{

                    startGameOccupyMsg.getAlias());
        }
    }

    /**

     */
    public static void changeCoinWeight(SyncInnoClient client, InnoChangeCoinWeightMsg changeCoinWeightMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_CHANGE_COIN_WEIGHT, hostId,
                    InnoParamsUtil.initChangeCoinWeightMsg(changeCoinWeightMsg));
        }else{

                    changeCoinWeightMsg.getAlias());
        }
    }

    /**

     */
    public static void dealCoinWeight(int preCoinWeight, int userId, int productId) {
        
        int coinWeight = InnoProductUtil.userCoinWeight(userId, productId);
        if(coinWeight!=preCoinWeight) {

                    ProductUtil.loadProductName(productId), preCoinWeight, coinWeight);
            SchedulerSample.delayed(1, new SyncInnoChangeCoinWeightTask(userId, productId, coinWeight));
        }
    }

    /**

     */
    public static void authPushCoin(SyncInnoClient client, InnoAutoPushCoinMsg autoPushCoinMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_AUTO_PUSH_COIN, hostId,
                    InnoParamsUtil.initAutoPushCoinMsg(autoPushCoinMsg));
        }else{


        }
    }
}
