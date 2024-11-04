package avatar.task.product.innoMsg;

import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductSocketUtil;
import avatar.util.trigger.SchedulerSample;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**

 */
public class InnoVoiceNoticeTask extends ScheduledTask {

    
    private JSONObject jsonMap;

    
    private int productId;

    public InnoVoiceNoticeTask(JSONObject jsonMap, int productId) {

        this.jsonMap = jsonMap;
        this.productId = productId;
    }

    @Override
    public void run() {

//                productId, JsonUtil.mapToJson(jsonMap));
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                
                List<Session> onlineSessionList = ProductSocketUtil.dealOnlineSession(productId);
                if(onlineSessionList.size()>0){
                    JSONObject clientMsg = InnoParamsUtil.loadClientDirectMsg(jsonMap);
                    for(Session sessionMsg : onlineSessionList){
                        
                        SchedulerSample.delayed(1,
                                new PushSessionVoiceNoticeTask(clientMsg, productId, sessionMsg));
                    }
                }

            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

}
