package avatar.apiregister.websocket.product.notice;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductWebsocketService;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductSocketUtil;
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
public class ExitProductApi extends SystemEventHandler2<Session> {
    protected ExitProductApi() {
        super(WebSocketCmd.C2S_EXIT_PRODUCT);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            
            String accessToken = session.getAccessToken();
            
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            int status = WebsocketEncodeUtil.checkEncode(accessToken, false, jsonObject);
            if(ParamsUtil.isSuccess(status)) {
                int userId = UserUtil.loadUserIdByToken(accessToken);
                int productId = jsonObject.getInteger("devId");
                if(userId>0) {
                    int pId = UserOnlineUtil.loadOnlineProduct(userId);
                    if (pId == productId) {
                        
                        ProductWebsocketService.exitProduct(userId, productId);
                    }
                }
                
                dealExitProduct(productId, session);
            }
        });
        cachedPool.shutdown();
    }

    /**

     */
    private void dealExitProduct(int productId, Session session) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductSocketUtil.exitProduct(productId, session);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
