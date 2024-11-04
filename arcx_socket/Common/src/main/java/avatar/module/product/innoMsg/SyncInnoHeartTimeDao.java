package avatar.module.product.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class SyncInnoHeartTimeDao {
    private static final SyncInnoHeartTimeDao instance = new SyncInnoHeartTimeDao();
    public static final SyncInnoHeartTimeDao getInstance(){
        return instance;
    }

    /**

     */
    public long loadTime(String linkMsg) {
        
        long time = loadCache(linkMsg);
        if(time==-1){
            time = 0;
            setCache(linkMsg, 0);
        }
        return time;
    }

    //=========================cache===========================

    /**

     */
    public long loadCache(String linkMsg){
        Object obj = GameData.getCache().get(ProductPrefixMsg.SYNC_INNO_HEART_TIME+"_"+linkMsg);
        return obj==null?-1:Long.parseLong(obj.toString());
    }

    /**

     */
    public void setCache(String linkMsg, long time){
        GameData.getCache().set(ProductPrefixMsg.SYNC_INNO_HEART_TIME+"_"+linkMsg, time);
    }
}
