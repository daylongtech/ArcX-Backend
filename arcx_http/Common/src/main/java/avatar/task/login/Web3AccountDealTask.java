package avatar.task.login;

import avatar.global.lockMsg.LockMsg;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.GameShiftUtil;
import avatar.util.thirdpart.Web3Util;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class Web3AccountDealTask extends ScheduledTask {

    private int userId;

    public Web3AccountDealTask(int userId) {

        this.userId = userId;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.WEB3_LOCK + "_" + userId,
                500);
        try {
            if (lock.lock()) {
                String email = UserUtil.loadUserEmail(userId);
                if(!StrUtil.checkEmpty(email)) {
                    
                    GameShiftUtil.addGameShiftAccount(userId, email);
                }
                
//                Web3Util.addAxcAccount(userId);
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }
}
