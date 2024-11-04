package avatar.task.user;

import avatar.global.basicConfig.ConfigMsg;
import avatar.global.linkMsg.http.NoticeHttpCmdName;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserOnlineUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class RefreshUserBalanceNoticeTask extends ScheduledTask {

    
    private int userId;

    public RefreshUserBalanceNoticeTask(int userId) {

        this.userId = userId;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId)){
            sendNotice(userId);
        }
    }

    /**

     */
    private static void sendNotice(int userId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("dealUserId", userId);
        paramsMap.put("platform", ConfigMsg.sysPlatform);
        
        String httpRequest = ParamsUtil.httpRequestProduct(NoticeHttpCmdName.REFRESH_USER_BALANCE_NOTICE, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            
            HttpClientUtil.sendHttpGet(httpRequest);
        }
    }

}
