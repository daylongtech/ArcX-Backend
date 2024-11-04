package avatar.module.activity.dragonTrain.user;

import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.activity.DragonTrainUtil;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class DragonTrainUserMsgDao {
    private static final DragonTrainUserMsgDao instance = new DragonTrainUserMsgDao();
    public static final DragonTrainUserMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public DragonTrainUserMsgEntity loadByUserId(int userId) {
        
        DragonTrainUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByUserId(userId);
            if(entity==null){
                
                entity = insert(DragonTrainUtil.initDragonTrainUserMsgEntity(userId));
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
    private DragonTrainUserMsgEntity loadCache(int userId){
        return (DragonTrainUserMsgEntity) GameData.getCache().get(ActivityPrefixMsg.DRAGON_TRAIN_USER_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, DragonTrainUserMsgEntity entity){
        GameData.getCache().set(ActivityPrefixMsg.DRAGON_TRAIN_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private DragonTrainUserMsgEntity loadDbByUserId(int userId) {
        Map<String, Object> equalsParams = new HashMap<>();
        equalsParams.put("user_id", userId);
        String sql = SqlUtil.getSql("dragon_train_user_msg", equalsParams).toString();
        return GameData.getDB().get(DragonTrainUserMsgEntity.class, sql, new Object[]{});
    }

    /**

     */
    private DragonTrainUserMsgEntity insert(DragonTrainUserMsgEntity entity) {
        int id  = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            return entity;
        }
        return null;
    }

    /**

     */
    public boolean update(DragonTrainUserMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
