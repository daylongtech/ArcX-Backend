package avatar.module.user.attribute;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class UserAttributeLvListDao {
    private static final UserAttributeLvListDao instance = new UserAttributeLvListDao();
    public static final UserAttributeLvListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadMsg(){
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
        return (List<Integer>)
                GameData.getCache().get(UserPrefixMsg.USER_ATTRIBUTE_LV_LIST);
    }

    /**

     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.USER_ATTRIBUTE_LV_LIST, list);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbMsg() {
        String sql = "select lv from user_attribute_config order by lv";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
