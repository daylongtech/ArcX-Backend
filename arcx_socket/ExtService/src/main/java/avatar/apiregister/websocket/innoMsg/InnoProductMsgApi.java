package avatar.apiregister.websocket.innoMsg;

import avatar.data.product.innoMsg.InnoProductMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.net.session.Session;
import avatar.service.innoMsg.InnoProductService;
import avatar.util.LogUtil;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class InnoProductMsgApi extends SystemEventHandler2<Session> {
    protected InnoProductMsgApi() {
        super(SelfInnoWebsocketInnerCmd.S2P_PRODUCT_MSG);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            
            if(InnerEnCodeUtil.checkEncode(jsonObject)) {

                InnoProductMsg innoProductMsg = InnoParamsUtil.initInnoProductMsg(jsonObject);
                
                InnoProductService.describeProductMsg(innoProductMsg);
            }
        });
        cachedPool.shutdown();
    }
}
