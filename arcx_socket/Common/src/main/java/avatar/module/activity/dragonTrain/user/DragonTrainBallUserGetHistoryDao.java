package avatar.module.activity.dragonTrain.user;

import avatar.entity.activity.dragonTrain.user.DragonTrainBallUserGetHistoryEntity;
import avatar.util.GameData;

/**

 */
public class DragonTrainBallUserGetHistoryDao {
    private static final DragonTrainBallUserGetHistoryDao instance = new DragonTrainBallUserGetHistoryDao();
    public static final DragonTrainBallUserGetHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public boolean insert(DragonTrainBallUserGetHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }
}
