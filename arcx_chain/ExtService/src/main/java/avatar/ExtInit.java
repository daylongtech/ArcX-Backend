package avatar;

import avatar.event.CommonNotifyConfigEvent;
import avatar.event.InternalEventDispatcher;
import avatar.service.system.HandleNotifyConfigService;
import avatar.task.server.InitExtConfigTask;
import avatar.task.solana.ConnectSolanaWebsocketTask;
import avatar.util.solana.SolanaRequestUtil;
import avatar.util.trigger.SchedulerSample;

/**

 */
public class ExtInit extends ServerInit {
    @Override
    public void init() {
        addCommonEventListener();
        initListenInternalEvent();
        initScheduler();

    }

    
    private void addCommonEventListener() {

    }

    private void initListenInternalEvent(){
        InternalEventDispatcher.getInstance().addEventListener(CommonNotifyConfigEvent.type , HandleNotifyConfigService.class);

    }

    private void initScheduler() {
        
        SchedulerSample.init();
        
        SchedulerSample.register(1* 60* 60 * 1000 , 10 , new InitExtConfigTask());

        
        SchedulerSample.delayed(1000, new ConnectSolanaWebsocketTask());

    }



}