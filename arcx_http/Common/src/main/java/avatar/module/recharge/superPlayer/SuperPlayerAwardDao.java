package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerAwardEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

import java.util.List;

/**

 */
public class SuperPlayerAwardDao {
    private static final SuperPlayerAwardDao instance = new SuperPlayerAwardDao();
    public static final SuperPlayerAwardDao getInstance(){
        return instance;
    }

    /**

     */
    public List<SuperPlayerAwardEntity> loadMsg() {
        
        List<SuperPlayerAwardEntity> list = loadCache();
        if(list==null){
            list = loadDbMsg();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<SuperPlayerAwardEntity> loadCache(){
        return (List<SuperPlayerAwardEntity>) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_AWARD);
    }

    /**

     */
    private void setCache(List<SuperPlayerAwardEntity> list){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_AWARD, list);
    }

    //=========================db===========================

    /**

     */
    private List<SuperPlayerAwardEntity> loadDbMsg() {
        String sql = "select * from super_player_award order by sequence";
        return GameData.getDB().list(SuperPlayerAwardEntity.class, sql, new Object[]{});
    }

}
