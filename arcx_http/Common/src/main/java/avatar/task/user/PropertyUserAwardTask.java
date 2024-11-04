package avatar.task.user;

import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.global.enumMsg.basic.commodity.PropertyUseTypeEnum;
import avatar.module.basic.systemMsg.PropertyMsgDao;
import avatar.util.user.UserAttributeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class PropertyUserAwardTask extends ScheduledTask {

    
    private int userId;

    
    private int propertyType;

    public PropertyUserAwardTask(int userId, int propertyType) {

        this.userId = userId;
        this.propertyType = propertyType;
    }

    @Override
    public void run() {
        
        PropertyMsgEntity entity = PropertyMsgDao.getInstance().loadMsg(propertyType);
        if(entity!=null){
            int userType = entity.getPropertyUseType();
            int num = entity.getNum();
            if(userType== PropertyUseTypeEnum.RESTORE_ENERGY.getCode() && num>0){
                
                UserAttributeUtil.addUserEnergy(userId, num);
            }
        }
    }

}
