package avatar.module.product.pileTower;

import avatar.entity.product.pileTower.ProductPileTowerUserMsgEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class ProductPileTowerUserMsgDao {
    private static final ProductPileTowerUserMsgDao instance = new ProductPileTowerUserMsgDao();
    public static final ProductPileTowerUserMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public ProductPileTowerUserMsgEntity loadByUserId(int userId) {
        
        ProductPileTowerUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByUserId(userId);
            if(entity!=null){
                
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private ProductPileTowerUserMsgEntity loadCache(int userId){
        return (ProductPileTowerUserMsgEntity) GameData.getCache().get(ProductPrefixMsg.PRODUCT_PILE_TOWER_USER_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, ProductPileTowerUserMsgEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_PILE_TOWER_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private ProductPileTowerUserMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from product_pile_tower_user_msg where user_id=? order by create_time desc";
        return GameData.getDB().get(ProductPileTowerUserMsgEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    public boolean insert(ProductPileTowerUserMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            setCache(entity.getUserId(), entity);
            return true;
        }
        return false;
    }
}
