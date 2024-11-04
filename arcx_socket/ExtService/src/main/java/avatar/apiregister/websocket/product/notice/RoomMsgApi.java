package avatar.apiregister.websocket.product.notice;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.service.product.ProductWebsocketService;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.product.ProductSocketUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class RoomMsgApi extends SystemEventHandler2<Session> {
    protected RoomMsgApi() {
        super(WebSocketCmd.C2S_ROOM_MSG);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            String accessToken = session.getAccessToken();
            
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            int status = WebsocketEncodeUtil.checkEncode(accessToken, false, jsonObject);
            if(ParamsUtil.isSuccess(status)) {
                int productId = jsonObject.getInteger("devId");
                int userId = UserUtil.loadUserIdByToken(accessToken);
                int pId = userId>0? UserOnlineUtil.loadOnlineProduct(userId):0;
                
                ProductSocketUtil.dealJoinProduct(productId, session);
                if(pId==0){
                    
                    ProductWebsocketService.joinProduct(userId, productId, false);
                }else {
                    
                    SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
                }

            }
        });
        cachedPool.shutdown();
    }
}
