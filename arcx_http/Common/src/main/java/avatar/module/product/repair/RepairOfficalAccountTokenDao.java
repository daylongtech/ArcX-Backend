package avatar.module.product.repair;

import avatar.global.basicConfig.basic.ProductConfigMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class RepairOfficalAccountTokenDao {
    private static final RepairOfficalAccountTokenDao instance = new RepairOfficalAccountTokenDao();
    public static final RepairOfficalAccountTokenDao getInstance(){
        return instance;
    }

    /**

     */
    public String loadToken(){
        return loadCache();
    }

    //=========================cache===========================

    /**

     */
    private String loadCache() {
        List<Object> list = (List<Object>) GameData.getCache().get(ProductPrefixMsg.REPAIR_OFFICAL_ACCOUNT_TOKEN);
        if(list==null || list.size()!=2){
            return null;
        }else{
            String token = (String) list.get(0);//token
            long freshTime = (Long) list.get(1);
            
            if(((TimeUtil.getNowTime()-freshTime)>ProductConfigMsg.OFFICAL_TOKEN_OUT_TIME*1000)){
                return null;
            }else{
                return token;
            }
        }
    }

    /**

     */
    public void setCache(String token) {
        GameData.getCache().removeCache(ProductPrefixMsg.REPAIR_OFFICAL_ACCOUNT_TOKEN);
        List<Object> list = new ArrayList<>();
        list.add(token);//token
        list.add(TimeUtil.getNowTime());
        GameData.getCache().set(ProductPrefixMsg.REPAIR_OFFICAL_ACCOUNT_TOKEN, list);
    }
}
