package avatar.module.nft.record;

import avatar.entity.nft.SellGoldMachineGoldHistoryEntity;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class SellGoldMachineGoldHistoryDao {
    private static final SellGoldMachineGoldHistoryDao instance = new SellGoldMachineGoldHistoryDao();
    public static final SellGoldMachineGoldHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public boolean insert(SellGoldMachineGoldHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }

    /**

     */
    public double loadDbEarn(String nftCode) {
        String sql = "select sum(real_earn) from sell_gold_machine_gold_history where nft_code=?";
        List<String> list = GameData.getDB().listString(sql, new Object[]{nftCode});
        String amountStr = StrUtil.strListNum(list);
        return StrUtil.truncateFourDecimal(Double.parseDouble(amountStr));
    }

    /**

     */
    public List<SellGoldMachineGoldHistoryEntity> loadDbReport(String nftCode, int pageNum, int pageSize) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select * from sell_gold_machine_gold_history ");
        
        SqlUtil.equalsParamStr(sqlBuilder, "nft_code", nftCode, 0);
        
        sqlBuilder.append(" order by create_time desc ");
        
        sqlBuilder.append(SqlUtil.pageMsg(pageNum, pageSize));
        List<SellGoldMachineGoldHistoryEntity> list = GameData.getDB().list(SellGoldMachineGoldHistoryEntity.class,
                sqlBuilder.toString(), new Object[]{});
        return list==null?new ArrayList<>():list;
    }
}
