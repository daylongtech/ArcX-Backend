package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerOrderEntity;
import avatar.util.GameData;

/**

 */
public class SuperPlayerOrderDao {
    private static final SuperPlayerOrderDao instance = new SuperPlayerOrderDao();
    public static final SuperPlayerOrderDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public void insert(SuperPlayerOrderEntity entity){
        GameData.getDB().insert(entity);
    }

}
