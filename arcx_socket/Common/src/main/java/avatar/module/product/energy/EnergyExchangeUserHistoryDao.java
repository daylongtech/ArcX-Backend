package avatar.module.product.energy;

import avatar.entity.product.energy.EnergyExchangeUserHistoryEntity;
import avatar.util.GameData;

/**

 */
public class EnergyExchangeUserHistoryDao {
    private static final EnergyExchangeUserHistoryDao instance = new EnergyExchangeUserHistoryDao();
    public static final EnergyExchangeUserHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public long insert(EnergyExchangeUserHistoryEntity entity){
        return GameData.getDB().insertAndReturn(entity);
    }
}
