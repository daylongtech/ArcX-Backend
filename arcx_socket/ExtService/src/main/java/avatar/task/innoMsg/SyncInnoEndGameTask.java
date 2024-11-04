package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoEndGameMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.util.innoMsg.*;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SyncInnoEndGameTask extends ScheduledTask {
    private String host;

    private int port;

    private InnoEndGameMsg endGameMsg;

    public SyncInnoEndGameTask(String host, int port, InnoEndGameMsg endGameMsg) {

        this.host = host;
        this.port = port;
        this.endGameMsg = endGameMsg;
    }

    @Override
    public void run() {
        
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            
            SyncInnoUtil.endGame(client, endGameMsg);
        }else{
            InnoSendWebsocketUtil.sendWebsocketMsg(endGameMsg.getUserId(),
                    ProductOperationEnum.OFF_LINE.getCode(), ClientCode.PRODUCT_EXCEPTION.getCode(), 0,
                    endGameMsg.getProductId());
        }
    }
}
