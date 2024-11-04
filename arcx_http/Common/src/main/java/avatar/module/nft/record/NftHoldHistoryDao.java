package avatar.module.nft.record;

import avatar.entity.nft.NftHoldHistoryEntity;
import avatar.util.GameData;

/**

 */
public class NftHoldHistoryDao {
    private static final NftHoldHistoryDao instance = new NftHoldHistoryDao();
    public static final NftHoldHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public boolean insert(NftHoldHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }
}
