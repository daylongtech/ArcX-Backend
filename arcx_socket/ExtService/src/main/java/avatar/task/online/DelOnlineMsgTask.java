package avatar.task.online;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.online.UserOnlineMsgDao;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**

 */
public class DelOnlineMsgTask extends ScheduledTask {

    public DelOnlineMsgTask() {

    }

    @Override
    public void run() {
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadDbAll();
        if(list!=null && list.size()>0){
            list.forEach(entity-> {
                int isGaming = entity.getIsGaming();
                if(isGaming== YesOrNoEnum.YES.getCode()){
                    
                    ProductRoomDao.getInstance().delUser(entity.getProductId(), entity.getUserId());
                }
                
                UserOnlineMsgDao.getInstance().delete(entity.getUserId(), entity.getProductId());
            });
        }
    }
}
