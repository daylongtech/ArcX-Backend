package avatar.module.nft.info;

import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.module.nft.user.UserNftListDao;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

/**

 */
public class SellGoldMachineMsgDao {
    private static final SellGoldMachineMsgDao instance = new SellGoldMachineMsgDao();
    public static final SellGoldMachineMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public SellGoldMachineMsgEntity loadMsg(String nftCode){
        SellGoldMachineMsgEntity entity = loadCache(nftCode);
        if(entity==null){
            entity = loadDbByNftCode(nftCode);
            if(entity!=null) {
                setCache(nftCode, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private SellGoldMachineMsgEntity loadCache(String nftCode){
        return (SellGoldMachineMsgEntity)
                GameData.getCache().get(NftPrefixMsg.SELL_GOLD_MACHINE_MSG+"_"+nftCode);
    }

    /**

     */
    private void setCache(String nftCode, SellGoldMachineMsgEntity entity){
        GameData.getCache().set(NftPrefixMsg.SELL_GOLD_MACHINE_MSG+"_"+nftCode, entity);
    }

    //=========================db===========================

    /**

     */
    private SellGoldMachineMsgEntity loadDbByNftCode(String nftCode) {
        String sql = "select * from sell_gold_machine_msg where nft_code=?";
        return GameData.getDB().get(SellGoldMachineMsgEntity.class, sql, new Object[]{nftCode});
    }

    /**

     */
    public boolean update(int oriUserId, SellGoldMachineMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getNftCode(), entity);
            
            OperateSellGoldMachineListDao.getInstance().removeCache();
            
            NftMarketListDao.getInstance().removeCache();
            
            UserNftListDao.getInstance().removeCache(entity.getUserId());
            if(oriUserId!=entity.getUserId()){
                UserNftListDao.getInstance().removeCache(oriUserId);
            }
        }
        return flag;
    }

}
