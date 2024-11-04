package avatar.module.activity.dragonTrain.info;

import avatar.entity.activity.dragonTrain.info.DragonTrainConfigMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**

 */
public class DragonTrainConfigMsgDao {
    private static final DragonTrainConfigMsgDao instance = new DragonTrainConfigMsgDao();
    public static final DragonTrainConfigMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public DragonTrainConfigMsgEntity loadMsg() {
        
        DragonTrainConfigMsgEntity entity = loadCache();
        if(entity==null){
            
            entity = loadDbMsg();
            if(entity!=null) {
                
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private DragonTrainConfigMsgEntity loadCache(){
        return (DragonTrainConfigMsgEntity) GameData.getCache().get(ActivityPrefixMsg.DRAGON_TRAIN_CONFIG);
    }

    /**

     */
    private void setCache(DragonTrainConfigMsgEntity entity){
        GameData.getCache().set(ActivityPrefixMsg.DRAGON_TRAIN_CONFIG, entity);
    }

    //=========================db===========================

    /**

     */
    private DragonTrainConfigMsgEntity loadDbMsg() {
        String sql = SqlUtil.getSql("dragon_train_config_msg", new HashMap<>()).toString();
        return GameData.getDB().get(DragonTrainConfigMsgEntity.class, sql, new Object[]{});
    }
}
