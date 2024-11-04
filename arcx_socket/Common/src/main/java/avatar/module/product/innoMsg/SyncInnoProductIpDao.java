package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.SyncInnoProductIpEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class SyncInnoProductIpDao {

    private static final SyncInnoProductIpDao instance = new SyncInnoProductIpDao();
    public static final SyncInnoProductIpDao getInstance(){
        return instance;
    }

    /**

     */
    public List<SyncInnoProductIpEntity> loadAll() {
        
        List<SyncInnoProductIpEntity> list = loadCache();
        if(list==null){
            list = loadDbMsg();
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
    private List<SyncInnoProductIpEntity> loadCache(){
        return (List<SyncInnoProductIpEntity>) GameData.getCache().get(ProductPrefixMsg.SYNC_INNO_PRODUCT_IP);
    }

    /**

     */
    private void setCache(List<SyncInnoProductIpEntity> entity){
        GameData.getCache().set(ProductPrefixMsg.SYNC_INNO_PRODUCT_IP, entity);
    }

    //=========================db===========================

    /**

     */
    private List<SyncInnoProductIpEntity> loadDbMsg() {
        String sql = "select * from sync_inno_product_ip";
        return GameData.getDB().list(SyncInnoProductIpEntity.class, sql, new Object[]{});
    }
}
