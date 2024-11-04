package avatar.task.innoMsg;

import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SyncInnoChangeCoinWeightTask extends ScheduledTask {

    private int userId;

    private int productId;

    private int coinWeight;

    public SyncInnoChangeCoinWeightTask(int userId, int productId, int coinWeight) {

        this.userId = userId;
        this.productId = productId;
        this.coinWeight = coinWeight;
    }

    @Override
    public void run() {
        
        String host = ProductUtil.productIp(productId);
        int port = ProductUtil.productSocketPort(productId);
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            
            SyncInnoUtil.changeCoinWeight(client, InnoParamsUtil.initInnoChangeCoinWeightMsg(productId,
                    ProductUtil.loadProductAlias(productId), userId, coinWeight));
        }
    }
}
