package avatar.task.login;

import avatar.module.user.info.EmailUserDao;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.Map;

/**

 */
public class LoginMsgUpdateTask extends ScheduledTask {

    private int userId;

    private String thirdPartUid;

    private String userIp;

    private Map<String, Object> paramsMap;

    public LoginMsgUpdateTask(int userId, String thirdPartUid, String userIp, Map<String, Object> paramsMap) {

        this.userId = userId;
        this.thirdPartUid = thirdPartUid;
        this.userIp = userIp;
        this.paramsMap = paramsMap;
    }

    @Override
    public void run() {
        
        UserUtil.updateUserLoginMsg(userId, paramsMap);
        
        if(!StrUtil.checkEmpty(thirdPartUid) && UserThirdPartUidMsgDao.getInstance().loadByUid(thirdPartUid)==0){
            UserUtil.addUserThirdPartMsg(userId, thirdPartUid);
        }
        if(!StrUtil.checkEmpty(userIp)) {
            
            UserUtil.updateIpMsg(userId, userIp);
        }
        
        SchedulerSample.delayed(1, new Web3AccountDealTask(userId));
    }
}
