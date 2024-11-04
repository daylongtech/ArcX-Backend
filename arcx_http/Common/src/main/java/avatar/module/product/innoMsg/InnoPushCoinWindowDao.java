package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class InnoPushCoinWindowDao {
    private static final InnoPushCoinWindowDao instance = new InnoPushCoinWindowDao();
    public static final InnoPushCoinWindowDao getInstance(){
        return instance;
    }

    /**

     */
    public InnoPushCoinWindowMsgEntity loadBySecondType(int secondType) {
        
        InnoPushCoinWindowMsgEntity entity = loadCache(secondType);
        if(entity==null){
            
            entity = loadDbBySecondType(secondType);
            if(entity!=null){
                
                setCache(secondType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private InnoPushCoinWindowMsgEntity loadCache(int secondType){
        return (InnoPushCoinWindowMsgEntity) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_WINDOW+"_"+secondType);
    }

    /**

     */
    private void setCache(int secondType, InnoPushCoinWindowMsgEntity entity){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_WINDOW+"_"+secondType, entity);
    }

    //=========================db===========================

    /**

     */
    private InnoPushCoinWindowMsgEntity loadDbBySecondType(int secondType) {
        String sql = "select * from inno_push_coin_window_msg where second_type=?";
        return GameData.getDB().get(InnoPushCoinWindowMsgEntity.class, sql, new Object[]{secondType});
    }

}
