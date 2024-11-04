package avatar.task.user;

import avatar.util.user.UserNoticePushUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SystemNoticeNoticeTask extends ScheduledTask {
    private int userId;

    private String content;

    public SystemNoticeNoticeTask(int userId, String content) {

        this.userId = userId;
        this.content = content;
    }

    @Override
    public void run() {
        
        UserNoticePushUtil.systemNoticePush(userId, content);
    }
}
