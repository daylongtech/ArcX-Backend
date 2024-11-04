package avatar.task.product.general;

import avatar.global.code.basicConfig.ConfigMsg;
import avatar.module.product.gaming.UserJoinProductTimeDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class PushJoinProductTask extends ScheduledTask {

    private int productId;

    private int userId;

    public PushJoinProductTask(int productId, int userId) {

        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        long time = UserJoinProductTimeDao.getInstance().loadCache(userId, productId);
        if(time==0 || (TimeUtil.getNowTime()-time)>=ConfigMsg.joinProductTime*1000) {

            
            UserJoinProductTimeDao.getInstance().setCache(userId, productId, TimeUtil.getNowTime());
            
            ProductUtil.joinProductNotice(productId, userId);
        }else{

        }
    }
}
