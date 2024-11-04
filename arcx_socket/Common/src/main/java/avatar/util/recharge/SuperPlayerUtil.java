package avatar.util.recharge;

import avatar.entity.recharge.superPlayer.SuperPlayerUserMsgEntity;
import avatar.module.recharge.superPlayer.SuperPlayerUserDao;
import avatar.module.recharge.superPlayer.SuperPlayerUserListDao;
import avatar.util.system.TimeUtil;

/**

 */
public class SuperPlayerUtil {
    /**

     */
    public static boolean isSuperPlayer(int userId) {
        boolean flag = true;
        
        SuperPlayerUserMsgEntity entity = SuperPlayerUserDao.getInstance().loadMsg(userId);
        if(TimeUtil.getNowTime()>TimeUtil.strToLong(entity.getEffectTime())){
            
            SuperPlayerUserListDao.getInstance().removeCache();
            flag = false;
        }
        return flag;
    }

    /**

     */
    public static SuperPlayerUserMsgEntity initSuperPlayerUserMsgEntity(int userId) {
        SuperPlayerUserMsgEntity entity = new SuperPlayerUserMsgEntity();
        entity.setUserId(userId);
        entity.setEffectTime("");
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

}
