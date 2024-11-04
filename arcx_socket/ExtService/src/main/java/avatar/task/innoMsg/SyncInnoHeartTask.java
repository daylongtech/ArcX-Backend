package avatar.task.innoMsg;

import avatar.module.product.innoMsg.SyncInnoHeartTimeDao;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SyncInnoHeartTask extends ScheduledTask {
    private String host;

    private int port;

    private long startTime;

    public SyncInnoHeartTask(String host, int port, long startTime) {

        this.host = host;
        this.port = port;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        if(startTime== SyncInnoHeartTimeDao.getInstance().loadTime(linkMsg)) {
            
            SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
            if (client != null && client.isOpen()) {
                
                SyncInnoUtil.heart(client);
            }
            
            SchedulerSample.delayed(10000, new SyncInnoHeartTask(host, port, startTime));
        }
    }
}
