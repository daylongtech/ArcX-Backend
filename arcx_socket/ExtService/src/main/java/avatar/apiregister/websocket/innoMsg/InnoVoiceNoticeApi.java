package avatar.apiregister.websocket.innoMsg;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.net.session.Session;
import avatar.service.innoMsg.InnoProductService;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class InnoVoiceNoticeApi extends SystemEventHandler2<Session> {
    protected InnoVoiceNoticeApi() {
        super(SelfInnoWebsocketInnerCmd.S2P_VOICE_NOTICE_MSG);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            if(InnerEnCodeUtil.checkEncode(jsonObject)) {
                

                
                InnoProductService.voiceNotice(jsonObject);
            }
        });
        cachedPool.shutdown();
    }
}
