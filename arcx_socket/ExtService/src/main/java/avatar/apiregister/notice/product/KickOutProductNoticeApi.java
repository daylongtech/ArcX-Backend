package avatar.apiregister.notice.product;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.NoticeHttpCmdName;
import avatar.net.session.Session;
import avatar.task.product.general.UserKickOutProductNoticeTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.trigger.SchedulerSample;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class KickOutProductNoticeApi extends SystemEventHttpHandler<Session> {
    protected KickOutProductNoticeApi() {
        super(NoticeHttpCmdName.KICK_OUT_PRODUCT_NOTICE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            
            try {
                int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");
                int productId = ParamsUtil.intParmasNotNull(map, "productId");
                String platform = ParamsUtil.stringParmasNotNull(map, "platform");
                if (platform.equals(ConfigMsg.sysPlatform)) {
                    SchedulerSample.delayed(10, new UserKickOutProductNoticeTask(userId, productId));
                } else {

                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }
            SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
        });
        cachedPool.shutdown();
    }
}
