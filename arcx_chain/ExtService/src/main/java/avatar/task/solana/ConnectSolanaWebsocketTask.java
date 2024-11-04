package avatar.task.solana;

import avatar.util.solana.SolanaConnectUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class ConnectSolanaWebsocketTask extends ScheduledTask {
    public ConnectSolanaWebsocketTask() {

    }

    @Override
    public void run() {
        SolanaConnectUtil.connectSolanaServer();
    }
}
