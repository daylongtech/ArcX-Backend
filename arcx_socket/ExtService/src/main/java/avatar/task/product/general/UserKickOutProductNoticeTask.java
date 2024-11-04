package avatar.task.product.general;

import avatar.util.product.ProductOperateUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class UserKickOutProductNoticeTask extends ScheduledTask {
    private int userId;

    private int productId;

    public UserKickOutProductNoticeTask(int userId, int productId) {

        this.userId = userId;
        this.productId = productId;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId) || !UserUtil.isIosUser(userId)) {
            
            ProductOperateUtil.kickOut(userId, productId);
        }else{
            
            UserNoticePushUtil.kickOutProductPush(userId, productId);
        }
    }
}
