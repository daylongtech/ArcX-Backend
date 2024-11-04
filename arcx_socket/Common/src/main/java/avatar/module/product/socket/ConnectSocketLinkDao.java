package avatar.module.product.socket;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class ConnectSocketLinkDao {
    private static final ConnectSocketLinkDao instance = new ConnectSocketLinkDao();
    public static final ConnectSocketLinkDao getInstance(){
        return instance;
    }

    /**

     */
    public long loadByLinkMsg(String linkMsg){
        
        return loadCache(linkMsg);
    }

    //=========================cache===========================

    /**

     */
    private long loadCache(String linkMsg) {
        return GameData.getCache().get(ProductPrefixMsg.PRODUCT_CONNECT_TIME+"_"+linkMsg)==null?0:
                (long) GameData.getCache().get(ProductPrefixMsg.PRODUCT_CONNECT_TIME+"_"+linkMsg);
    }

    /**

     */
    public void setCache(String linkMsg, long time) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_CONNECT_TIME+"_"+linkMsg, time);
    }
}
