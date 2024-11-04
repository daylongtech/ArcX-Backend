package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoStartGameMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.util.innoMsg.*;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class SyncInnoStartGameTask extends ScheduledTask {
    private String host;

    private int port;

    private InnoStartGameMsg startGameMsg;

    public SyncInnoStartGameTask(String host, int port, InnoStartGameMsg startGameMsg) {

        this.host = host;
        this.port = port;
        this.startGameMsg = startGameMsg;
    }

    @Override
    public void run() {
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            
            SyncInnoUtil.startGame(client, startGameMsg);
        }else{
            InnoSendWebsocketUtil.sendWebsocketMsg(startGameMsg.getUserId(),
                    ProductOperationEnum.START_GAME.getCode(), ClientCode.PRODUCT_EXCEPTION.getCode(), 0,
                    startGameMsg.getProductId());
        }
    }
}
