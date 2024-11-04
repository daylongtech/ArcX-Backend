package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerUserMsgEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

/**

 */
public class SuperPlayerUserDao {
    private static final SuperPlayerUserDao instance = new SuperPlayerUserDao();
    public static final SuperPlayerUserDao getInstance(){
        return instance;
    }

    /**

     */
    public SuperPlayerUserMsgEntity loadMsg(int userId) {
        
        SuperPlayerUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            entity = loadDbMsg(userId);
            if(entity==null && UserUtil.existUser(userId)){
                entity = insert(SuperPlayerUtil.initSuperPlayerUserMsgEntity(userId));
            }
            if(entity!=null) {
                
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private SuperPlayerUserMsgEntity loadCache(int userId){
        return (SuperPlayerUserMsgEntity) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_USER_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, SuperPlayerUserMsgEntity entity){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private SuperPlayerUserMsgEntity loadDbMsg(int userId) {
        String sql = "select * from super_player_user_msg where user_id=?";
        return GameData.getDB().get(SuperPlayerUserMsgEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    private SuperPlayerUserMsgEntity insert(SuperPlayerUserMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            
            setCache(entity.getUserId(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(SuperPlayerUserMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity);
            
            SuperPlayerUserListDao.getInstance().loadMsg();
        }
        return flag;
    }

}
