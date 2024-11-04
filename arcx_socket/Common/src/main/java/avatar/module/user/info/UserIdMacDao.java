package avatar.module.user.info;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class UserIdMacDao {
    private static final UserIdMacDao instance = new UserIdMacDao();
    public static final UserIdMacDao getInstance(){
        return instance;
    }

    /**

     */
    public String loadByUserId(int userId){
        String mac = loadCache(userId);
        if(mac==null){
            
            mac = loadDbByUserId(userId);
            
            setCache(userId, mac);
        }
        return mac;
    }

    //=========================cache===========================

    /**

     */
    private String loadCache(int userId){
        Object obj = GameData.getCache().get(UserPrefixMsg.USER_ID_MAC+"_"+userId);
        return obj==null?null:obj.toString();
    }

    /**

     */
    private void setCache(int userId, String mac){
        GameData.getCache().set(UserPrefixMsg.USER_ID_MAC+"_"+userId, mac);
    }

    //=========================db===========================

    /**

     */
    private String loadDbByUserId(int userId) {
        String sql = "select mac from user_mac_msg where user_id=?";
        List<String> list = GameData.getDB().listString(sql, new Object[]{userId});
        return StrUtil.strListSize(list)>0?list.get(0):"";
    }
}
