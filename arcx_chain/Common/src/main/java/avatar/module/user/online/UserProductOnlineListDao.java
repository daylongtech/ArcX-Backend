package avatar.module.user.online;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class UserProductOnlineListDao {
    private static final UserProductOnlineListDao instance = new UserProductOnlineListDao();
    public static final UserProductOnlineListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadByProductId(int productId){
        List<Integer> list = loadCache(productId);
        if(list==null){
            
            list = loadDbByProductId(productId);
            setCache(productId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(int productId){
        return (List<Integer>)
                GameData.getCache().get(UserPrefixMsg.USER_PRODUCT_ONLINE_LIST+"_"+productId);
    }

    /**

     */
    private void setCache(int productId, List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.USER_PRODUCT_ONLINE_LIST+"_"+productId, list);
    }

    /**

     */
    public void removeCache(int productId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_PRODUCT_ONLINE_LIST+"_"+productId);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbByProductId(int productId) {
        String sql = "select user_id from user_online_msg where product_id=? order by create_time";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{productId});
        if(StrUtil.listSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }
}
