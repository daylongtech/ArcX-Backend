package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerConfigEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**

 */
public class SuperPlayerConfigDao {
    private static final SuperPlayerConfigDao instance = new SuperPlayerConfigDao();
    public static final SuperPlayerConfigDao getInstance(){
        return instance;
    }

    /**

     */
    public SuperPlayerConfigEntity loadMsg() {
        
        SuperPlayerConfigEntity entity = loadCache();
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
    private SuperPlayerConfigEntity loadCache(){
        return (SuperPlayerConfigEntity) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_CONFIG);
    }

    /**

     */
    private void setCache(SuperPlayerConfigEntity entity){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_CONFIG, entity);
    }

    //=========================db===========================

    /**

     */
    private SuperPlayerConfigEntity loadDbMsg() {
        String sql = "select * from super_player_config";
        return GameData.getDB().get(SuperPlayerConfigEntity.class, sql, new Object[]{});
    }

}
