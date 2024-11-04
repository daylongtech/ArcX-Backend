package avatar.module.product.energy;

import avatar.entity.product.energy.EnergyExchangeAwardEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class EnergyExchangeAwardDao {
    private static final EnergyExchangeAwardDao instance = new EnergyExchangeAwardDao();
    public static final EnergyExchangeAwardDao getInstance(){
        return instance;
    }

    /**

     */
    public List<EnergyExchangeAwardEntity> loadMsg(int configId) {
        List<EnergyExchangeAwardEntity> list = loadCache(configId);
        if(list==null){
            list = loadDbByConfigId(configId);
            
            setCache(configId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<EnergyExchangeAwardEntity> loadCache(int configId){
        return (List<EnergyExchangeAwardEntity>) GameData.getCache().get(ProductPrefixMsg.ENERGY_EXCHANGE_AWARD+"_"+configId);
    }

    /**

     */
    private void setCache(int configId, List<EnergyExchangeAwardEntity> list){
        GameData.getCache().set(ProductPrefixMsg.ENERGY_EXCHANGE_AWARD+"_"+configId, list);
    }

    /**

     */
    private void removeCache(int configId){
        GameData.getCache().removeCache(ProductPrefixMsg.ENERGY_EXCHANGE_AWARD+"_"+configId);
    }

    //=========================db===========================

    /**

     */
    private List<EnergyExchangeAwardEntity> loadDbByConfigId(int configId) {
        String sql = "select * from energy_exchange_award where config_id=? order by " +
                "award_probability*1.0/total_probability desc";
        List<EnergyExchangeAwardEntity> list = GameData.getDB().list(EnergyExchangeAwardEntity.class, sql,
                new Object[]{configId});
        return list==null?new ArrayList<>():list;
    }

    /**

     */
    public void update(EnergyExchangeAwardEntity entity) {
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            removeCache(entity.getConfigId());
        }
    }
}
