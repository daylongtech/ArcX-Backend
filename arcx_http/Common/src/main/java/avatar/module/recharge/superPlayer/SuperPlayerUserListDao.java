package avatar.module.recharge.superPlayer;

import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**

 */
public class SuperPlayerUserListDao {
    private static final SuperPlayerUserListDao instance = new SuperPlayerUserListDao();
    public static final SuperPlayerUserListDao getInstance(){
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
        return (List<Integer>) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_USER_LIST);
    }

    /**

     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_USER_LIST, list);
    }

    /**

     */
    public void removeCache(){
        GameData.getCache().removeCache(RechargePrefixMsg.SUPER_PLAYER_USER_LIST);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbMsg() {
        String sql = "select user_id from super_player_user_msg where effect_time>='"+ TimeUtil.getNowTimeStr() +"'";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
