package avatar.task.innoMsg;

import avatar.util.innoMsg.SyncInnoConnectUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class ConnectInnoProductTask extends ScheduledTask {
    public ConnectInnoProductTask() {

    }

    @Override
    public void run() {
        SyncInnoConnectUtil.connectInnoProductServer();
    }
}
