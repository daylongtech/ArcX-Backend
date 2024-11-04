package avatar.service.product;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.global.Config;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.activity.dragonTrain.user.DragonTrainUserMsgDao;
import avatar.module.product.gaming.ProductGamingUserMsgDao;
import avatar.task.product.general.PushJoinProductTask;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.LogUtil;
import avatar.util.innoMsg.SendInnoInnerMsgUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.JsonUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class ProductWebsocketService {
    /**

     */
    public static void joinProduct(int userId, int productId, boolean joinFlag) {
        
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
        if(userId>0) {

            
            UserOnlineUtil.joinProductMsg(userId, productId, Config.getInstance().getLocalAddr(),
                    Config.getInstance().getWebSocketPort());
            if (joinFlag) {
                
                SchedulerSample.delayed(5, new PushJoinProductTask(productId, userId));
            }
            
            productMsgCheck(productId);
            
            UserOperateLogUtil.joinProduct(userId, productId);
        }
    }

    /**

     */
    private static void productMsgCheck(int productId) {
        
        ProductGamingUserMsg msg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(msg.getServerSideType()>0 && msg.getUserId()>0){
            
            if(ProductUtil.isInnoProduct(productId)){
                
                SendInnoInnerMsgUtil.sendClientMsg(productId, SelfInnoWebsocketInnerCmd.P2S_PRODUCT_MSG, initProductMsgMap(msg));
            }else {
                
                InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_PRODUCT_MSG,
                        ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                        InnerNormalProductWebsocketUtil.initProductMsgOperateMap(msg));
            }
        }
    }

    /**

     */
    private static Map<Object, Object> initProductMsgMap(ProductGamingUserMsg msg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        int productId = msg.getProductId();
        paramsMap.put("alias", ProductUtil.loadProductAlias(productId));
        paramsMap.put("userId", msg.getUserId());
        paramsMap.put("userName", msg.getUserName());
        paramsMap.put("imgUrl", msg.getImgUrl());
        paramsMap.put("serverSideType", msg.getServerSideType());
        return paramsMap;
    }

    /**

     */
    public static void exitProduct(int userId, int productId) {

        
        UserOnlineUtil.exitProductMsg(userId, productId);
        
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
        
        UserOperateLogUtil.existProduct(userId, productId);
    }

    /**

     */
    public static void innoDragonMsg(int userId) {
        JSONObject dataJson = new JSONObject();
        
        DragonTrainUserMsgEntity entity = DragonTrainUserMsgDao.getInstance().loadByUserId(userId);
        dataJson.put("dgbAmt", entity==null?0:entity.getDragonNum());

        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_INNO_DRAGON_MSG,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }

}
