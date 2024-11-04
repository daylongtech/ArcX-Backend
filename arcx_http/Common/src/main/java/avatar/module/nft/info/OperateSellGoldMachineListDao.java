package avatar.module.nft.info;

import avatar.entity.nft.NftConfigEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**

 */
public class OperateSellGoldMachineListDao {
    private static final OperateSellGoldMachineListDao instance = new OperateSellGoldMachineListDao();
    public static final OperateSellGoldMachineListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<String> loadMsg(){
        List<String> list = loadCache();
        if(list==null){
            list = loadDbList();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<String> loadCache(){
        return (List<String>)
                GameData.getCache().get(NftPrefixMsg.OPERATE_SELL_GOLD_MACHINE_LIST);
    }

    /**

     */
    private void setCache(List<String> list){
        GameData.getCache().set(NftPrefixMsg.OPERATE_SELL_GOLD_MACHINE_LIST, list);
    }

    /**

     */
    public void removeCache(){
        GameData.getCache().removeCache(NftPrefixMsg.OPERATE_SELL_GOLD_MACHINE_LIST);
    }

    //=========================db===========================

    /**

     */
    private List<String> loadDbList() {
        
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        long adWeight = entity==null?0:entity.getAdWeight();
        long saleWeight = entity==null?0:entity.getSaleWeight();
        long nowTime = TimeUtil.getNowTime();
        String sql = "select nft_code from sell_gold_machine_msg where status=4 " +
                "order by ((?-UNIX_TIMESTAMP(start_operate_time))*(?-UNIX_TIMESTAMP(start_operate_time))/60000.0+" +
                "adv*?-sell_time*?) desc";
        List<String> list = GameData.getDB().listString(sql, new Object[]{nowTime, nowTime, adWeight, saleWeight});
        return StrUtil.strRetList(list);
    }
}
