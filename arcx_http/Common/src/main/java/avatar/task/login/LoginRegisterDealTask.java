package avatar.task.login;

import avatar.module.user.info.UserRegisterIpDao;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.Map;

/**

 */
public class LoginRegisterDealTask extends ScheduledTask {

    private int userId;

    private String userIp;

    private String mac;

    private String thirdPartUid;

    private Map<String, Object> paramsMap;

    public LoginRegisterDealTask(int userId, String userIp, String mac, String thirdPartUid, Map<String, Object> paramsMap) {

        this.userId = userId;
        this.userIp = userIp;
        this.mac = mac;
        this.thirdPartUid = thirdPartUid;
        this.paramsMap = paramsMap;
    }

    @Override
    public void run() {
        try {
            if (!StrUtil.checkEmpty(userIp)) {
                
                UserRegisterIpDao.getInstance().insert(UserUtil.initUserRegisterIpEntity(userId, userIp, paramsMap));
            }
            
            if (!StrUtil.checkEmpty(mac)) {
                UserUtil.addRegisterUserMacMsg(userId, mac);
            }
            
            if (!StrUtil.checkEmpty(thirdPartUid) && UserThirdPartUidMsgDao.getInstance().loadByUid(thirdPartUid) == 0) {
                UserUtil.addUserThirdPartMsg(userId, thirdPartUid);
            }
            
            UserOperateLogUtil.register(userId);
            
            UserUtil.addRegisterWelfare(userId);
            
            SchedulerSample.delayed(1, new Web3AccountDealTask(userId));
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }
}
