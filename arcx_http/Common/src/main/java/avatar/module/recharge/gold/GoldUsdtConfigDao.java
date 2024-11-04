package avatar.module.recharge.gold;

import avatar.entity.recharge.gold.GoldUsdtConfigEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**

 */
public class GoldUsdtConfigDao {
    private static final GoldUsdtConfigDao instance = new GoldUsdtConfigDao();
    public static final GoldUsdtConfigDao getInstance(){
        return instance;
    }

    /**

     */
    public long loadMsg() {
        
        long proportion = loadCache();
        if(proportion==-1){
            GoldUsdtConfigEntity entity = loadDbMsg();
            proportion = entity==null?0:entity.getProportion();
            
            setCache(proportion);
        }
        return proportion;
    }

    //=========================cache===========================

    /**

     */
    private long loadCache(){
        Object obj = GameData.getCache().get(RechargePrefixMsg.GOLD_USDT_CONFIG);
        return obj==null?-1:(long)obj;
    }

    /**

     */
    private void setCache(long proportion){
        GameData.getCache().set(RechargePrefixMsg.GOLD_USDT_CONFIG, proportion);
    }

    //=========================db===========================

    /**

     */
    public GoldUsdtConfigEntity loadDbMsg() {
        String sql = "select * from gold_usdt_config";
        return GameData.getDB().get(GoldUsdtConfigEntity.class, sql, new Object[]{});
    }


}
