package avatar.module.user.online;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class UserOnlineMsgDao {
    private static final UserOnlineMsgDao instance = new UserOnlineMsgDao();
    public static final UserOnlineMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public List<UserOnlineMsgEntity> loadByUserId(int userId){
        List<UserOnlineMsgEntity> list = loadCache(userId);
        if(list==null){
            
            list = loadDbByUserId(userId);
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<UserOnlineMsgEntity> loadCache(int userId){
        return (List<UserOnlineMsgEntity>)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, List<UserOnlineMsgEntity> list){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId, list);
    }

    //=========================db===========================

    /**

     */
    private List<UserOnlineMsgEntity> loadDbByUserId(int userId) {
        String sql = "select * from user_online_msg where user_id=? order by create_time desc";
        List<UserOnlineMsgEntity> list = GameData.getDB().list(UserOnlineMsgEntity.class, sql, new Object[]{userId});
        return list==null?new ArrayList<>():list;
    }

}
