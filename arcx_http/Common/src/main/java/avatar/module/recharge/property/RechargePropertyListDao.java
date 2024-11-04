package avatar.module.recharge.property;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class RechargePropertyListDao {
    private static final RechargePropertyListDao instance = new RechargePropertyListDao();
    public static final RechargePropertyListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadMsg() {
        
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
        return (List<Integer>) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_LIST);
    }

    /**

     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_LIST, list);
    }


    //=========================db===========================

    /**

     */
    private List<Integer> loadDbMsg() {
        String sql = "select id from recharge_property_msg where active_flag=? order by create_time";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{YesOrNoEnum.YES.getCode()});
        return StrUtil.retList(list);
    }
}
