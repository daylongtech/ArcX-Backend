package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoProductOperateMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.util.innoMsg.*;
import avatar.util.product.InnoParamsUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SyncInnoProductOperateTask extends ScheduledTask {
    private String host;

    private int port;

    private InnoProductOperateMsg productOperateMsg;

    public SyncInnoProductOperateTask(String host, int port, InnoProductOperateMsg productOperateMsg) {

        this.host = host;
        this.port = port;
        this.productOperateMsg = productOperateMsg;
    }

    @Override
    public void run() {
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            
            SyncInnoUtil.productOperate(client, productOperateMsg);
        }else{
            InnoSendWebsocketUtil.sendWebsocketMsg(productOperateMsg.getUserId(),
                    InnoParamsUtil.loadProductOperateType(productOperateMsg.getInnoProductOperateType()), ClientCode.PRODUCT_EXCEPTION.getCode(), 0,
                    productOperateMsg.getProductId());
        }
    }
}
