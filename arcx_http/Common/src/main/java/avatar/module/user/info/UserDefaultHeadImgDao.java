package avatar.module.user.info;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class UserDefaultHeadImgDao {
    private static final UserDefaultHeadImgDao instance = new UserDefaultHeadImgDao();
    public static final UserDefaultHeadImgDao getInstance(){
        return instance;
    }

    /**

     */
    public List<String> loadAll(){
        
        List<String> list = loadCache();
        if(list==null){
            
            list = loadDbAll();
            if(list==null){
                list = new ArrayList<>();
            }
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<String> loadCache() {
        return (List<String>) GameData.getCache().get(UserPrefixMsg.USER_DEFAULT_HEAD_IMG);
    }

    /**

     */
    private void setCache(List<String> list) {
        GameData.getCache().set(UserPrefixMsg.USER_DEFAULT_HEAD_IMG, list);
    }

    //=========================db===========================

    /**

     */
    private List<String> loadDbAll() {
        String sql = "select img_url from user_default_head_img";
        return GameData.getDB().listString(sql, new Object[]{});
    }
}
