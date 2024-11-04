package avatar.module.nft.info;

import avatar.entity.nft.SellGoldMachineUpConfigEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;

/**

 */
public class SellGoldMachineUpConfigDao {
    private static final SellGoldMachineUpConfigDao instance = new SellGoldMachineUpConfigDao();
    public static final SellGoldMachineUpConfigDao getInstance(){
        return instance;
    }

    /**

     */
    public SellGoldMachineUpConfigEntity loadMsg(int lv){
        SellGoldMachineUpConfigEntity entity = loadCache(lv);
        if(entity==null){
            entity = loadDbByLv(lv);
            if(entity!=null) {
                setCache(lv, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private SellGoldMachineUpConfigEntity loadCache(int lv){
        return (SellGoldMachineUpConfigEntity)
                GameData.getCache().get(NftPrefixMsg.SELL_GOLD_MACHINE_LV_CONFIG+"_"+lv);
    }

    /**

     */
    private void setCache(int lv, SellGoldMachineUpConfigEntity entity){
        GameData.getCache().set(NftPrefixMsg.SELL_GOLD_MACHINE_LV_CONFIG+"_"+lv, entity);
    }

    //=========================db===========================

    /**

     */
    private SellGoldMachineUpConfigEntity loadDbByLv(int lv) {
        String sql = "select * from sell_gold_machine_up_config where lv=?";
        return GameData.getDB().get(SellGoldMachineUpConfigEntity.class, sql, new Object[]{lv});
    }

}
