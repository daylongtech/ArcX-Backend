package avatar.module.log;

import avatar.entity.log.UserOperateLogEntity;
import avatar.util.GameData;

/**

 */
public class UserOperateLogDao {
    private static final UserOperateLogDao instance = new UserOperateLogDao();
    public static final UserOperateLogDao getInstance(){
        return instance;
    }

    //=========================db===========================

    public void insert(UserOperateLogEntity entity){
        GameData.getLogDB().insert(entity);
    }
}
