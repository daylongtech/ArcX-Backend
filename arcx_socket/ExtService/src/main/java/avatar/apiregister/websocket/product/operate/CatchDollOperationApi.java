package avatar.apiregister.websocket.product.operate;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.service.product.CatchDollService;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class CatchDollOperationApi extends SystemEventHandler2<Session> {
    protected CatchDollOperationApi() {
        super(WebSocketCmd.C2S_DOLL_MACHINE_OPERATION);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            try {
                
                String accessToken = session.getAccessToken();
                
                JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
                
                JSONObject dataJson = new JSONObject();
                
                int status = CheckParamsUtil.checkProductOperation(accessToken, jsonObject, dataJson);
                
                if(ParamsUtil.isSuccess(status)){
                    int productId = jsonObject.getInteger("devId");
                    if(!ProductUtil.isSpecifyMachine(productId, ProductTypeEnum.DOLL_MACHINE.getCode())){
                        status = ClientCode.PRODUCT_TYPE_ERROR.getCode();
                    }
                }
                
                if(ParamsUtil.isSuccess(status)){
                    int userId = UserUtil.loadUserIdByToken(accessToken);
                    int productId = jsonObject.getInteger("devId");
                    int operateState = jsonObject.getInteger("hdlTp");
                    
                    RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                            2000);
                    try {
                        if (lock.lock()) {
                            DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
                            ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                            if(gamingMsg.isInitalization()){

                            }else {
                                
                                status = CatchDollService.checkOperate(operateState, userId, productRoomMsg);
                                if (ParamsUtil.isSuccess(status)) {
                                    
                                    ProductSocketOperateService.catchDollOperate(productId, operateState, userId);
                                }

                                        ProductOperationEnum.loadByCode(operateState), status);
                            }
                        }
                    }catch (Exception e){
                        ErrorDealUtil.printError(e);
                    }finally {
                        lock.unlock();
                    }
                }else{
                    SendWebsocketMsgUtil.sendByAccessToken(WebSocketCmd.S2C_DOLL_MACHINE_OPERATION, status,
                            accessToken, dataJson);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            }
        });
        cachedPool.shutdown();
    }

}
