package avatar.module.recharge.property;

import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**

 */
public class RechargePropertyMsgDao {
    private static final RechargePropertyMsgDao instance = new RechargePropertyMsgDao();
    public static final RechargePropertyMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public RechargePropertyMsgEntity loadMsg(int id) {
        
        RechargePropertyMsgEntity entity = loadCache(id);
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
    private RechargePropertyMsgEntity loadCache(int id){
        return (RechargePropertyMsgEntity) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_MSG+"_"+id);
    }

    /**

     */
    private void setCache(int id, RechargePropertyMsgEntity entity){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_MSG+"_"+id, entity);
    }

    //=========================db===========================

    /**

     */
    private RechargePropertyMsgEntity loadDbById(int id) {
        return GameData.getDB().get(RechargePropertyMsgEntity.class, id);
    }

}
