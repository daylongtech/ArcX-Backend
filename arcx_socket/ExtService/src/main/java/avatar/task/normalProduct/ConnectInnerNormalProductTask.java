package avatar.task.normalProduct;

import avatar.util.normalProduct.InnerNormalProductConnectUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class ConnectInnerNormalProductTask extends ScheduledTask {
    public ConnectInnerNormalProductTask() {

    }

    @Override
    public void run() {
        InnerNormalProductConnectUtil.connectProductServer();
    }
}
