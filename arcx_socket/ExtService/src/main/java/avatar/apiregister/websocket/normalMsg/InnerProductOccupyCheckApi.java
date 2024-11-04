package avatar.apiregister.websocket.normalMsg;

import avatar.data.product.general.ResponseGeneralMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.net.session.Session;
import avatar.service.normal.InnerProductService;
import avatar.util.LogUtil;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class InnerProductOccupyCheckApi extends SystemEventHandler2<Session> {
    protected InnerProductOccupyCheckApi() {
        super(WebsocketInnerCmd.S2C_PRODUCT_OCCUPY_CHECK);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            String hostId = session.getAccessToken();
            
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            
            if(InnerEnCodeUtil.checkEncode(jsonObject)){

                ResponseGeneralMsg responseGeneralMsg = InnerNormalProductUtil.initResponseGeneralMsg(jsonObject);
                
                InnerProductService.productOccupyCheck(responseGeneralMsg);
            }
        });
        cachedPool.shutdown();
    }
}
