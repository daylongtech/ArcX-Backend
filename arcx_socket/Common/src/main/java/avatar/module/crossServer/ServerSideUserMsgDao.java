package avatar.module.crossServer;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserUtil;

/**

 */
public class ServerSideUserMsgDao {
    private static final ServerSideUserMsgDao instance = new ServerSideUserMsgDao();
    public static final ServerSideUserMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductGamingUserMsg loadByMsg(int userId, int serverSideType){
        
        return loadCache(userId, serverSideType);
    }

    //=========================cache===========================

    /**

     */
    private ProductGamingUserMsg loadCache(int userId, int serverSideType) {
        return GameData.getCache().get(ProductPrefixMsg.SERVER_SIDE_USER_MSG+"_"+userId+"_"+serverSideType)==null?
                UserUtil.initProductGamingUserMsg(userId, serverSideType) :
                (ProductGamingUserMsg) GameData.getCache().get(ProductPrefixMsg.SERVER_SIDE_USER_MSG+"_"+userId+"_"+serverSideType);
    }

    /**

     */
    public void setCache(int userId, int serverSideType, ProductGamingUserMsg userMsg) {
        GameData.getCache().set(ProductPrefixMsg.SERVER_SIDE_USER_MSG+"_"+userId+"_"+serverSideType, userMsg);
    }

}
