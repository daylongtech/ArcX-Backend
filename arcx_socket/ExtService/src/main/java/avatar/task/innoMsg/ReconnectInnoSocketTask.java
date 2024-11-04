package avatar.task.innoMsg;

import avatar.util.LogUtil;
import avatar.util.innoMsg.SyncInnoDealUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class ReconnectInnoSocketTask extends ScheduledTask {
    private String host;

    private int port;

    private String fromKey;

    private String toKey;

    public ReconnectInnoSocketTask(String host, int port, String fromKey, String toKey) {

        this.host = host;
        this.port = port;
        this.fromKey = fromKey;
        this.toKey = toKey;
    }

    @Override
    public void run() {

        
        SyncInnoOperateUtil.loadClient(host, port);
        
        SyncInnoDealUtil.socketCloseDeal(fromKey, toKey);

    }
}
