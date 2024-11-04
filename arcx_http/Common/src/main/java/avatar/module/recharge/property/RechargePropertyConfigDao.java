package avatar.module.recharge.property;

import avatar.entity.recharge.property.RechargePropertyConfigEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**

 */
public class RechargePropertyConfigDao {
    private static final RechargePropertyConfigDao instance = new RechargePropertyConfigDao();
    public static final RechargePropertyConfigDao getInstance(){
        return instance;
    }

    /**

     */
    public RechargePropertyConfigEntity loadMsg() {
        
        RechargePropertyConfigEntity entity = loadCache();
        if(entity==null){
            entity = loadDbMsg();
            if(entity!=null) {
                
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private RechargePropertyConfigEntity loadCache(){
        return (RechargePropertyConfigEntity) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_CONFIG);
    }

    /**

     */
    private void setCache(RechargePropertyConfigEntity entity){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_CONFIG, entity);
    }


    //=========================db===========================

    /**

     */
    private RechargePropertyConfigEntity loadDbMsg() {
        String sql = "select * from recharge_property_config";
        return GameData.getDB().get(RechargePropertyConfigEntity.class, sql, new Object[]{});
    }

}
