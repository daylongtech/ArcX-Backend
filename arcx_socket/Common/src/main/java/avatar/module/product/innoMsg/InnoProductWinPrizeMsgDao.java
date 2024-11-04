package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoProductWinPrizeMsgEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

/**

 */
public class InnoProductWinPrizeMsgDao {
    private static final InnoProductWinPrizeMsgDao instance = new InnoProductWinPrizeMsgDao();
    public static final InnoProductWinPrizeMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public InnoProductWinPrizeMsgEntity loadMsg(int userId, int productId, int productAwardType) {
        
        InnoProductWinPrizeMsgEntity entity = loadCache(userId, productId, productAwardType);
        if(entity==null){
            
            entity = loadDbMsg(userId, productId, productAwardType);
            if(entity!=null) {
                
                setCache(userId, productId, productAwardType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private InnoProductWinPrizeMsgEntity loadCache(int userId, int productId, int productAwardType){
        return (InnoProductWinPrizeMsgEntity) GameData.getCache().get(
                ProductPrefixMsg.INNO_USER_WIN_PRIZE+"_"+productId+"_"+userId+"_"+productAwardType);
    }

    /**

     */
    private void setCache(int userId, int productId, int productAwardType, InnoProductWinPrizeMsgEntity entity){
        GameData.getCache().set(ProductPrefixMsg.INNO_USER_WIN_PRIZE+"_"+productId+"_"+userId+"_"+productAwardType, entity);
    }

    //=========================db===========================

    /**

     */
    public InnoProductWinPrizeMsgEntity loadDbById(long awardId) {
        return GameData.getDB().get(InnoProductWinPrizeMsgEntity.class, awardId);
    }

    /**

     */
    private InnoProductWinPrizeMsgEntity loadDbMsg(int userId, int productId, int awardType) {
        String sql = "select * from inno_product_win_prize_msg where user_id=? and product_id=? " +
                "and award_type=? order by create_time desc ";
        return GameData.getDB().get(InnoProductWinPrizeMsgEntity.class, sql, new Object[]{userId, productId, awardType});
    }

    /**

     */
    public void update(InnoProductWinPrizeMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity.getProductId(), entity.getAwardType(), entity);
        }
    }

    /**

     */
    public long insert(InnoProductWinPrizeMsgEntity entity){
        long id = GameData.getDB().insertAndReturn(entity);
        if(id>0) {
            entity.setId(id);
            
            setCache(entity.getUserId(), entity.getProductId(), entity.getAwardType(), entity);
        }
        return id;
    }
}
