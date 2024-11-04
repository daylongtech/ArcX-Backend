package avatar.task.normalProduct;

import avatar.util.LogUtil;
import avatar.util.normalProduct.InnerNormalProductDealUtil;
import avatar.util.normalProduct.InnerNormalProductOperateUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class ReconnectInnerNormalProductSocketTask extends ScheduledTask {
    private String host;

    private int port;

    public ReconnectInnerNormalProductSocketTask(String host, int port) {

        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

        
        InnerNormalProductOperateUtil.loadClient(host, port);
        
        InnerNormalProductDealUtil.socketCloseDeal(host, port+"");
    }
}
