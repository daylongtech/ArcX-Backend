package avatar.task.product.general;

import avatar.global.lockMsg.LockMsg;
import avatar.service.jedis.RedisLock;
import avatar.util.basic.general.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserNoticePushUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class AddUserGameExpTask extends ScheduledTask {
    private int userId;

    private long coinNum;

    public AddUserGameExpTask(int userId, long coinNum) {

        this.userId = userId;
        this.coinNum = coinNum;
    }

    @Override
    public void run() {
        if(!CheckUtil.isSystemMaintain()) {
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    UserAttributeUtil.addUserGameExp(userId, coinNum);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
    }
}
