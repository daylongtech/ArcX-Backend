package avatar.task.normalProduct;

import avatar.module.product.normalProduct.InnerNormalProductHeartTimeDao;
import avatar.util.normalProduct.InnerNormalProductClient;
import avatar.util.normalProduct.InnerNormalProductConnectUtil;
import avatar.util.normalProduct.InnerNormalProductOperateUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnerNormalProductHeartTask extends ScheduledTask {
    private String host;

    private int port;

    private long startTime;

    public InnerNormalProductHeartTask(String host, int port, long startTime) {

        this.host = host;
        this.port = port;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        String linkMsg = host + port + InnerNormalProductConnectUtil.loadUid(host, port);
        if(startTime== InnerNormalProductHeartTimeDao.getInstance().loadTime(linkMsg)) {
            
            InnerNormalProductClient client = InnerNormalProductOperateUtil.webSocket.get(linkMsg);
            if (client != null && client.isOpen()) {
                
                InnerNormalProductUtil.heart(client);
            }
            
            SchedulerSample.delayed(10000, new InnerNormalProductHeartTask(host, port, startTime));
        }
    }
}
