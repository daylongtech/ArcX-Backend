package avatar.util.user;

import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.module.user.product.UserPreciseWeightNaInnoDao;
import avatar.util.LogUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class UserWeightUtil {
    /**

     */
    public static void addUserInnoNaNum(int userId, int secondType, long num){
        
        UserWeightNaInnoEntity entity = UserPreciseWeightNaInnoDao.getInstance().loadMsg(userId, secondType);
        if(entity!=null){
            entity.setNaNum(entity.getNaNum()+num);
            
            UserPreciseWeightNaInnoDao.getInstance().update(entity);
        }else{

                    userId, secondType, num);
        }
    }

    /**

     */
    public static UserWeightNaInnoEntity initUserWeightNaInnoEntity(int userId, int secondType) {
        UserWeightNaInnoEntity entity = new UserWeightNaInnoEntity();
        entity.setUserId(userId);
        entity.setSecondType(secondType);
        entity.setNaNum(0);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }
}
