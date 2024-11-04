package avatar.module.product.gaming;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class UserJoinProductDao {
    private static final UserJoinProductDao instance = new UserJoinProductDao();
    public static final UserJoinProductDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadByMsg(int userId) {
        List<Integer> list = loadCache(userId);
        if(list==null){
            list = new ArrayList<>();
            
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(int userId){
        return (List<Integer>) GameData.getCache().get(
                ProductPrefixMsg.USER_SESSION_JOIN_PRODUCT_MSG+"_"+userId);
    }

    /**

     */
    public void setCache(int userId, List<Integer> list){
        GameData.getCache().set(ProductPrefixMsg.USER_SESSION_JOIN_PRODUCT_MSG+"_"+userId, list);
    }

    /**

     */
    public void removeCache(int userId){
        GameData.getCache().removeCache(ProductPrefixMsg.USER_SESSION_JOIN_PRODUCT_MSG+"_"+userId);
    }

}
