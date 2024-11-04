package avatar.task.innoMsg;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.util.LogUtil;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SyncInnoAutoPushCoinTask extends ScheduledTask {

    private int userId;

    private int productId;

    private int isStart;

    public SyncInnoAutoPushCoinTask(int userId, int productId, int isStart) {

        this.userId = userId;
        this.productId = productId;
        this.isStart = isStart;
    }

    @Override
    public void run() {
        
        String host = ProductUtil.productIp(productId);
        int port = ProductUtil.productSocketPort(productId);
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {


            
            SyncInnoUtil.authPushCoin(client, InnoParamsUtil.initInnoAutoPushCoinMsg(productId,
                    ProductUtil.loadProductAlias(productId), userId, isStart));
        }
    }
}
