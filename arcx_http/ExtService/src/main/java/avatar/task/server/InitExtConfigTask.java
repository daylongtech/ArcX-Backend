package avatar.task.server;

import avatar.service.system.ExtConfiService;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InitExtConfigTask extends ScheduledTask {
    public InitExtConfigTask() {

    }

    @Override
    public void run() {
        ExtConfiService.initData();
    }
}
