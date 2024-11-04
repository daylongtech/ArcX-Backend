package avatar.module.user.info;

import avatar.entity.user.info.UserRegisterIpEntity;
import avatar.util.GameData;

/**

 */
public class UserRegisterIpDao {
    private static final UserRegisterIpDao instance = new UserRegisterIpDao();
    public static final UserRegisterIpDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public void insert(UserRegisterIpEntity entity){
        GameData.getDB().insert(entity);
    }

    /**

     */
    public UserRegisterIpEntity loadDbByUserId(int userId) {
        String sql = "select * from user_register_ip where user_id=?";
        return GameData.getDB().get(UserRegisterIpEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    public void update(UserRegisterIpEntity entity) {
        GameData.getDB().update(entity);
    }
}
