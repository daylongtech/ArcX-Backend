package avatar.service.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.code.basicConfig.CatchDollConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.product.DollInnerReceiveDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;

/**

 */
public class CatchDollService {
    /**

     */
    public static int checkOperate(int operateState, int userId, ProductRoomMsg productRoomMsg) {
        int productId = productRoomMsg.getProductId();
        int status = ClientCode.SUCCESS.getCode();
        if(operateState== ProductOperationEnum.START_GAME.getCode()
                && productRoomMsg.getGamingUserId()>0
                && (ProductUtil.startGameTime(productId)==0 || productRoomMsg.getGamingUserId()!=userId)){
            status = ClientCode.PRODUCT_OCCUPY.getCode();
        } else if(productRoomMsg.getGamingUserId()!=userId && operateState!= ProductOperationEnum.START_GAME.getCode()){
            status = ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode();
        }else if(operateState== ProductOperationEnum.START_GAME.getCode() &&
                !ProductUtil.isEnoughCost(userId, productId, 0)){
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_DOLL_MACHINE_OPERATION, status, userId, new JSONObject());
        }
        return status;
    }

    /**

     */
    public static void catchDollOperation(int productId, int operateState, int userId) {
        int status = ClientCode.SUCCESS.getCode();
        
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        if(operateState== ProductOperationEnum.START_GAME.getCode()){
            
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_START_GAME,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId,
                            StrUtil.strToStrList(CatchDollConfigMsg.startGameOperate)));
        }else if(operateState== ProductOperationEnum.CATCH.getCode()){
            
            
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_DOWN_CATCH,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initDownCatchOperateMap(productId, userId,
                            StrUtil.strToStrList(CatchDollConfigMsg.downClawOperate)));
            if(ParamsUtil.isSuccess(status)) {
                
                DollInnerReceiveDealUtil.preDownCatch(productId);
                
                DollInnerReceiveDealUtil.addDollResultCheckTask(productId);
            }
        }else if(operateState== ProductOperationEnum.OFF_LINE.getCode()){
            
            DollInnerReceiveDealUtil.offlineProduct(productId, userId, productInfoEntity.getIp());
        }else{
            
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_PRODUCT_OPERATE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initProductOperateMap(productId, userId,
                            Collections.singletonList(operateState+"")));
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_DOLL_MACHINE_OPERATION, status, userId, new JSONObject());
        }
    }
}
