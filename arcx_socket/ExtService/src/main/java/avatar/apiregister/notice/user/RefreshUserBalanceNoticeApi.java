package avatar.apiregister.notice.user;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.NoticeHttpCmdName;
import avatar.net.session.Session;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
@Service
public class RefreshUserBalanceNoticeApi extends SystemEventHttpHandler<Session> {
    protected RefreshUserBalanceNoticeApi() {
        super(NoticeHttpCmdName.REFRESH_USER_BALANCE_NOTICE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
            
            try {
                int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");
                String platform = ParamsUtil.stringParmasNotNull(map, "platform");
                if (platform.equals(ConfigMsg.sysPlatform)) {
                    String accessToken = UserUtil.loadAccessToken(userId);
                    if(!StrUtil.checkEmpty(accessToken)) {
                        Session userSession = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
                        if (userSession != null) {
                            
                            UserNoticePushUtil.userBalancePush(userId);
                        }
                    }
                } else {

                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }
        });
        cachedPool.shutdown();
    }
}
