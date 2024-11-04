package avatar.module.product.innoMsg;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class SyncInnoReconnectDao {
    private static final SyncInnoReconnectDao instance = new SyncInnoReconnectDao();
    public static final SyncInnoReconnectDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadMsg(String fromKey, String toKey) {
        
        return loadCache(fromKey, toKey);
    }

    //=========================cache===========================

    /**

     */
    private int loadCache(String fromKey, String tokey){
        int status = YesOrNoEnum.NO.getCode();
        Object object = GameData.getCache().get(ProductPrefixMsg.SYNC_INNO_RECONNECT+fromKey+tokey);
        if(object==null){
            setCache(fromKey, tokey, status);
        }else{
            status = (int) object;
        }
        return status;
    }

    /**

     */
    public void setCache(String fromKey, String tokey, int status){
        GameData.getCache().set(ProductPrefixMsg.SYNC_INNO_RECONNECT+fromKey+tokey, status);
    }
}
