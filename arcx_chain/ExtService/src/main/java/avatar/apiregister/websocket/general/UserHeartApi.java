package avatar.apiregister.websocket.general;

import avatar.facade.SystemEventHandler2;
import avatar.global.Config;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class UserHeartApi extends SystemEventHandler2<Session> {
    protected UserHeartApi() {
        super(WebSocketCmd.C2S_HEART);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            
            String accessToken = session.getAccessToken();
            
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            int status = WebsocketEncodeUtil.checkEncode(accessToken, false, jsonObject);
            if(ParamsUtil.isSuccess(status)) {
                int userId = UserUtil.loadUserIdByToken(session.getAccessToken());
                if (userId > 0) {
                    
                    UserOnlineUtil.onlineMsgOnline(userId, Config.getInstance().getLocalAddr(),
                            Config.getInstance().getWebSocketPort());
                }
            }
            SendWebsocketMsgUtil.sendBySession(WebSocketCmd.S2C_HEART, status, session,
                    new JSONObject());
        });
        cachedPool.shutdown();
    }
}
