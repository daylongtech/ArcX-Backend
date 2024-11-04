package avatar.module.product.normalProduct;

import avatar.entity.product.normalProduct.InnerNormalProductIpEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class InnerNormalProductIpDao {

    private static final InnerNormalProductIpDao instance = new InnerNormalProductIpDao();
    public static final InnerNormalProductIpDao getInstance(){
        return instance;
    }

    /**

     */
    public List<InnerNormalProductIpEntity> loadAll() {
        
        List<InnerNormalProductIpEntity> list = loadCache();
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
    private List<InnerNormalProductIpEntity> loadCache(){
        return (List<InnerNormalProductIpEntity>) GameData.getCache().get(ProductPrefixMsg.INNER_NORMAL_PRODUCT_IP);
    }

    /**

     */
    private void setCache(List<InnerNormalProductIpEntity> entity){
        GameData.getCache().set(ProductPrefixMsg.INNER_NORMAL_PRODUCT_IP, entity);
    }

    //=========================db===========================

    /**

     */
    private List<InnerNormalProductIpEntity> loadDbMsg() {
        String sql = SqlUtil.loadList("inner_normal_product_ip", new ArrayList<>()).toString();
        return GameData.getDB().list(InnerNormalProductIpEntity.class, sql, new Object[]{});
    }
}
