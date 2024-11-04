package avatar.module.product.energy;

import avatar.entity.product.energy.EnergyExchangeUserAwardEntity;
import avatar.util.GameData;

import java.util.List;

/**

 */
public class EnergyExchangeUserAwardDao {
    private static final EnergyExchangeUserAwardDao instance = new EnergyExchangeUserAwardDao();
    public static final EnergyExchangeUserAwardDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public void insert(List<EnergyExchangeUserAwardEntity> list){
        GameData.getDB().insert(list);
    }
}
