package avatar.apiregister.websocket.product.award;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.service.product.ProductWebsocketService;
import avatar.util.basic.encode.WebsocketEncodeUtil;
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
public class InnoDragonMsgApi extends SystemEventHandler2<Session> {
    protected InnoDragonMsgApi() {
        super(WebSocketCmd.C2S_INNO_DRAGON_MSG);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            String accessToken = session.getAccessToken();
            
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            int status = WebsocketEncodeUtil.checkEncode(accessToken, true, jsonObject);
            if(ParamsUtil.isSuccess(status)) {
                
                ProductWebsocketService.innoDragonMsg(UserUtil.loadUserIdByToken(accessToken));
            }
        });
        cachedPool.shutdown();
    }
}
