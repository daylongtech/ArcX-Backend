package avatar.module.activity.dragonTrain.info;

import avatar.entity.activity.dragonTrain.info.DragonTrainWheelIconMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class DragonTrainWheelAwardIconMsgDao {
    private static final DragonTrainWheelAwardIconMsgDao instance = new DragonTrainWheelAwardIconMsgDao();
    public static final DragonTrainWheelAwardIconMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public List<DragonTrainWheelIconMsgEntity> loadMsg() {
        
        List<DragonTrainWheelIconMsgEntity> list = loadCache();
        if(list==null){
            
            list = loadDbMsg();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<DragonTrainWheelIconMsgEntity> loadCache(){
        return (List<DragonTrainWheelIconMsgEntity>) GameData.getCache().get(ActivityPrefixMsg.DRAGON_TRAIN_WHEEL_AWARD_ICON);
    }

    /**

     */
    private void setCache(List<DragonTrainWheelIconMsgEntity> list){
        GameData.getCache().set(ActivityPrefixMsg.DRAGON_TRAIN_WHEEL_AWARD_ICON, list);
    }

    //=========================db===========================

    /**

     */
    private List<DragonTrainWheelIconMsgEntity> loadDbMsg() {
        String sql = SqlUtil.loadList("dragon_train_wheel_icon_msg",
                Collections.singletonList("(award_probability*1.0/total_probability) desc,id")).toString();
        List<DragonTrainWheelIconMsgEntity> list = GameData.getDB().list(DragonTrainWheelIconMsgEntity.class,
                sql, new Object[]{});
        return list==null?new ArrayList<>():list;
    }
}
