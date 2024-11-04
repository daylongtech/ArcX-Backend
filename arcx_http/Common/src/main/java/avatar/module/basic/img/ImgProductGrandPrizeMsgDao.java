package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductGrandPrizeMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.Collections;
import java.util.List;

/**

 */
public class ImgProductGrandPrizeMsgDao {
    private static final ImgProductGrandPrizeMsgDao instance = new ImgProductGrandPrizeMsgDao();
    public static final ImgProductGrandPrizeMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public List<ImgProductGrandPrizeMsgEntity> loadAll() {
        
        List<ImgProductGrandPrizeMsgEntity> list = loadCache();
        if(list==null){
            
            list = loadDbAll();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<ImgProductGrandPrizeMsgEntity> loadCache(){
        return (List<ImgProductGrandPrizeMsgEntity>) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_GRAND_PRIZE_MSG);
    }

    /**

     */
    private void setCache(List<ImgProductGrandPrizeMsgEntity> entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_GRAND_PRIZE_MSG, entity);
    }

    //=========================db===========================

    /**

     */
    private List<ImgProductGrandPrizeMsgEntity> loadDbAll() {
        String sql = SqlUtil.loadList("img_product_grand_prize_msg", Collections.singletonList("sequence")).toString();
        return GameData.getDB().list(ImgProductGrandPrizeMsgEntity.class, sql, new Object[]{});
    }
}
