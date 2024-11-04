package avatar.module.recharge.commodity;

import avatar.util.GameData;
import avatar.util.recharge.RechargeUtil;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**

 */
public class RechargeGoldOrderDao {
    private static final RechargeGoldOrderDao instance = new RechargeGoldOrderDao();
    public static final RechargeGoldOrderDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public double loadDbUserAmount(int userId, int timeRange) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select sum(price) from recharge_gold_order ");
        
        SqlUtil.equalsParamStr(sqlBuilder, "user_id", userId, 0);
        
        if(timeRange>0){
            SqlUtil.startTimeParamStr(sqlBuilder, "create_time",
                    TimeUtil.getBeforeNHour(TimeUtil.getNowTimeStr(), 24*timeRange), 1);
        }
        
        SqlUtil.listParamStr(sqlBuilder, "status", StrUtil.strToList(RechargeUtil.loadSuccessPayStr()), 1);
        List<String> list = GameData.getDB().listString(sqlBuilder.toString(), new Object[]{});
        return StrUtil.strListSize(list)>0? StrUtil.truncateTwoDecimal(Double.parseDouble(list.get(0))):0;
    }
}
