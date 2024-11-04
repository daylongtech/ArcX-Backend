package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoStartGameOccupyMsg;
import avatar.module.product.info.ProductAliasDao;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SyncInnoOccpuyCheckTask extends ScheduledTask {
    private InnoStartGameOccupyMsg startGameOccupyMsg;

    public SyncInnoOccpuyCheckTask(InnoStartGameOccupyMsg startGameOccupyMsg) {

        this.startGameOccupyMsg = startGameOccupyMsg;
    }

    @Override
    public void run() {
        int productId = ProductAliasDao.getInstance().loadByAlias(startGameOccupyMsg.getAlias());
        String host = ProductUtil.productIp(productId);//ip
        int port = ProductUtil.productSocketPort(productId);
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            
            SyncInnoUtil.startGameOccupy(client, startGameOccupyMsg);
        }
    }
}
