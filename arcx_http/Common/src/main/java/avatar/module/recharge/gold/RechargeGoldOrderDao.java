package avatar.module.recharge.gold;

import avatar.entity.recharge.gold.RechargeGoldOrderEntity;
import avatar.util.GameData;

/**

 */
public class RechargeGoldOrderDao {
    private static final RechargeGoldOrderDao instance = new RechargeGoldOrderDao();
    public static final RechargeGoldOrderDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public void insert(RechargeGoldOrderEntity entity){
        GameData.getDB().insert(entity);
    }
}
