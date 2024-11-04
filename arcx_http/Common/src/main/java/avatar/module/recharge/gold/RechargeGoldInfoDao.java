package avatar.module.recharge.gold;

import avatar.entity.recharge.gold.RechargeGoldInfoEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**

 */
public class RechargeGoldInfoDao {
    private static final RechargeGoldInfoDao instance = new RechargeGoldInfoDao();
    public static final RechargeGoldInfoDao getInstance(){
        return instance;
    }

    /**

     */
    public RechargeGoldInfoEntity loadById(int id) {
        
        RechargeGoldInfoEntity entity = loadCache(id);
        if(entity==null){
            entity = loadDbById(id);
            if(entity!=null) {
                
                setCache(id, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private RechargeGoldInfoEntity loadCache(int id){
        return (RechargeGoldInfoEntity) GameData.getCache().get(RechargePrefixMsg.RECHARGE_GOLD_INFO+"_"+id);
    }

    /**

     */
    private void setCache(int id, RechargeGoldInfoEntity entity){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_GOLD_INFO+"_"+id, entity);
    }

    //=========================db===========================

    /**

     */
    private RechargeGoldInfoEntity loadDbById(int id) {
        return GameData.getDB().get(RechargeGoldInfoEntity.class, id);
    }

}
