package avatar.module.user.info;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class MacUserListMsgDao {
    private static final MacUserListMsgDao instance = new MacUserListMsgDao();
    public static final MacUserListMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadByMacId(String macId){
        List<Integer> list = loadCache(macId);
        if(list==null){
            
            list = loadDbByMacId(macId);
            setCache(macId, list);
        }
        return list;
    }

    //=========================cache===========================


    /**

     */
    private List<Integer> loadCache(String mac){
        return (List<Integer>) GameData.getCache().get(UserPrefixMsg.MAC_USER_LIST+"_"+mac);
    }

    /**

     */
    private void setCache(String mac, List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.MAC_USER_LIST+"_"+mac, list);
    }

    /**

     */
    public void remove(String mac){
        GameData.getCache().removeCache(UserPrefixMsg.MAC_USER_LIST+"_"+mac);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbByMacId(String macId) {
        String sql = "select user_id from user_mac_msg where mac=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{macId});
        return StrUtil.listNum(list)>0?list:new ArrayList<>();
    }

}
