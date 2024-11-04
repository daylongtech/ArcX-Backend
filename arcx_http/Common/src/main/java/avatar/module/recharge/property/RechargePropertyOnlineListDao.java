package avatar.module.recharge.property;

import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.recharge.RechargePropertyUtil;

import java.util.List;

/**

 */
public class RechargePropertyOnlineListDao {
    private static final RechargePropertyOnlineListDao instance = new RechargePropertyOnlineListDao();
    public static final RechargePropertyOnlineListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadMsg() {
        
        List<Integer> list = loadCache();
        if(list==null){
            list = RechargePropertyUtil.loadOnlineList();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(){
        return (List<Integer>) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_ONLINE_LIST);
    }

    /**

     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_ONLINE_LIST, list);
    }


}
