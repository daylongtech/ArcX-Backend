package avatar.module.user.info;

import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

/**

 */
public class UserGrandPrizeMsgDao {
    private static final UserGrandPrizeMsgDao instance = new UserGrandPrizeMsgDao();
    public static final UserGrandPrizeMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public UserGrandPrizeMsgEntity loadByUserId(int userId){
        UserGrandPrizeMsgEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByUserId(userId);
            if(entity==null){
                
                entity = insert(userId);
            }
            if(entity!=null){
                
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserGrandPrizeMsgEntity loadCache(int userId){
        return (UserGrandPrizeMsgEntity)
                GameData.getCache().get(UserPrefixMsg.USER_GRAND_PRIZE_MSG+"_"+userId);
    }

    /**

     */
    public void setCache(int userId, UserGrandPrizeMsgEntity entity){
        
        GameData.getCache().set(UserPrefixMsg.USER_GRAND_PRIZE_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private UserGrandPrizeMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from user_grand_prize_msg where user_id=?";
        return GameData.getDB().get(UserGrandPrizeMsgEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    private UserGrandPrizeMsgEntity insert(int userId) {
        UserGrandPrizeMsgEntity entity = UserUtil.initUserGrandPrizeMsgEntity(userId);
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(UserGrandPrizeMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
