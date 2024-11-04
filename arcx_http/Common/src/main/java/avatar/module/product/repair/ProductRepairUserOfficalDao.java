package avatar.module.product.repair;

import avatar.entity.product.repair.ProductRepairUserOfficalEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class ProductRepairUserOfficalDao {
    private static final ProductRepairUserOfficalDao instance = new ProductRepairUserOfficalDao();
    public static final ProductRepairUserOfficalDao getInstance(){
        return instance;
    }

    /**

     */
    public List<ProductRepairUserOfficalEntity> loadAll(){
        List<ProductRepairUserOfficalEntity> list = loadCache();
        if(list==null){
            
            list = loadDbAll();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<ProductRepairUserOfficalEntity> loadCache(){
        return (List<ProductRepairUserOfficalEntity>)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_REPAIR_USER_OFFICAL);
    }

    /**

     */
    private void setCache(List<ProductRepairUserOfficalEntity> list){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_REPAIR_USER_OFFICAL, list);
    }

    //=========================db===========================

    /**

     */
    private List<ProductRepairUserOfficalEntity> loadDbAll() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("status", YesOrNoEnum.YES.getCode());
        String sql = SqlUtil.loadList("product_repair_user_offical", paramsMap,
                Collections.singletonList("create_time")).toString();
        return GameData.getDB().list(ProductRepairUserOfficalEntity.class, sql, new Object[]{});
    }
}
