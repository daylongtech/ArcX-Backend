package avatar;

import avatar.event.CommonNotifyConfigEvent;
import avatar.event.InternalEventDispatcher;
import avatar.module.offline.UserOfflineService;
import avatar.service.system.HandleNotifyConfigService;
import avatar.task.innoMsg.ConnectInnoProductTask;
import avatar.task.normalProduct.ConnectInnerNormalProductTask;
import avatar.task.online.DelOnlineMsgTask;
import avatar.task.server.InitExtConfigTask;
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
        
        UserOfflineService.getInstance().init();
    }

    private void initListenInternalEvent(){
        InternalEventDispatcher.getInstance().addEventListener(CommonNotifyConfigEvent.type , HandleNotifyConfigService.class);

    }

    private void initScheduler() {
        
        SchedulerSample.init();
        
        SchedulerSample.register(1* 60* 60 * 1000 , 10 , new InitExtConfigTask());

        
        SchedulerSample.delayed(1000, new ConnectInnoProductTask());
        
        SchedulerSample.delayed(1000, new ConnectInnerNormalProductTask());

        
        SchedulerSample.delayed(10 , new DelOnlineMsgTask());

    }



}