package avatar.module.product.productType;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class LotteryCoinPercentDao {
    private static final LotteryCoinPercentDao instance = new LotteryCoinPercentDao();
    public static final LotteryCoinPercentDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadBySecondLevelType(int secondLevelType) {
        
        int percent = loadCache(secondLevelType);
        if(percent==-1){
            
            percent = loadDbBySecondLevelType(secondLevelType);
            
            setCache(secondLevelType, percent);
        }
        return percent;
    }

    //=========================cache===========================

    /**

     */
    public int loadCache(int secondLevelType){
        Object obj = GameData.getCache().get(ProductPrefixMsg.LOTTERY_COIN_PERCENT+"_"+secondLevelType);
        if(obj==null){
            return -1;
        }else{
            return (int) obj;
        }
    }

    /**

     */
    public void setCache(int secondLevelType, int percent){
        GameData.getCache().set(ProductPrefixMsg.LOTTERY_COIN_PERCENT+"_"+secondLevelType, percent);
    }

    //=========================db===========================

    /**

     */
    private int loadDbBySecondLevelType(int secondLevelType) {
        String sql = "select percent from lottery_coin_percent where second_level_type=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{secondLevelType});
        return StrUtil.listNum(list);
    }

}
