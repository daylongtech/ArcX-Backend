package avatar.module.user.online;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**

 */
public class UserOnlineListDao {
    private static final UserOnlineListDao instance = new UserOnlineListDao();
    public static final UserOnlineListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadAll(){
        List<Integer> list = loadCache();
        if(list==null){
            
            list = loadDbAll();
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(){
        return (List<Integer>)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_LIST);
    }

    /**

     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_LIST, list);
    }

    /**

     */
    public void removeCache(){
        GameData.getCache().removeCache(UserPrefixMsg.USER_ONLINE_LIST);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbAll() {
        String sql = SqlUtil.appointListSql("user_online_msg", "user_id",
                new HashMap<>(), Collections.singletonList("create_time")).toString();
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        if(StrUtil.listSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }
}
