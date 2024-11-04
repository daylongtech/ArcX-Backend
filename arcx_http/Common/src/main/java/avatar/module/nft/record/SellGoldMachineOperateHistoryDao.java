package avatar.module.nft.record;

import avatar.entity.nft.SellGoldMachineOperateHistoryEntity;
import avatar.util.GameData;

/**

 */
public class SellGoldMachineOperateHistoryDao {
    private static final SellGoldMachineOperateHistoryDao instance = new SellGoldMachineOperateHistoryDao();
    public static final SellGoldMachineOperateHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public boolean insert(SellGoldMachineOperateHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }
}
