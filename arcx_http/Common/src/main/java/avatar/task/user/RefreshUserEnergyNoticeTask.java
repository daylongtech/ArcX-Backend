package avatar.task.user;

import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.linkMsg.NoticeHttpCmdName;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserOnlineUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class RefreshUserEnergyNoticeTask extends ScheduledTask {

    
    private int userId;

    
    private int energyNum;

    public RefreshUserEnergyNoticeTask(int userId, int energyNum) {

        this.userId = userId;
        this.energyNum = energyNum;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId)){
            sendNotice(userId, energyNum);
        }
    }

    /**

     */
    private static void sendNotice(int userId, int energyNum) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("dealUserId", userId);
        paramsMap.put("changeNum", energyNum);
        paramsMap.put("platform", ConfigMsg.sysPlatform);
        
        String httpRequest = ParamsUtil.httpRequestProduct(NoticeHttpCmdName.REFRESH_USER_ENERGY_NOTICE, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            
            HttpClientUtil.sendHttpGet(httpRequest);
        }
    }

}
