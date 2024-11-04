package avatar.module.recharge.property;

import avatar.entity.recharge.property.RechargePropertyOrderEntity;
import avatar.util.GameData;

/**

 */
public class RechargePropertyOrderDao {
    private static final RechargePropertyOrderDao instance = new RechargePropertyOrderDao();
    public static final RechargePropertyOrderDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public void insert(RechargePropertyOrderEntity entity){
        GameData.getDB().insert(entity);
    }

}
