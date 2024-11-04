package avatar.module.nft.info;

import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class SellGoldMachineUpLvListDao {
    private static final SellGoldMachineUpLvListDao instance = new SellGoldMachineUpLvListDao();
    public static final SellGoldMachineUpLvListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadMsg(){
        List<Integer> list = loadCache();
        if(list==null){
            list = loadDbMsg();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(){
        return (List<Integer>)
                GameData.getCache().get(NftPrefixMsg.SELL_GOLD_MACHINE_UP_LV_LIST);
    }

    /**

     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(NftPrefixMsg.SELL_GOLD_MACHINE_UP_LV_LIST, list);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbMsg() {
        String sql = "select lv from sell_gold_machine_up_config order by lv";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
