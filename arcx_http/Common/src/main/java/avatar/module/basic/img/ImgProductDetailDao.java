package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductDetailEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class ImgProductDetailDao {
    private static final ImgProductDetailDao instance = new ImgProductDetailDao();
    public static final ImgProductDetailDao getInstance(){
        return instance;
    }

    /**

     */
    public ImgProductDetailEntity loadById(int id) {
        
        ImgProductDetailEntity entity = loadCache(id);
        if(entity==null){
            
            entity = loadDbById(id);
            if(entity!=null) {
                
                setCache(id, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private ImgProductDetailEntity loadCache(int id){
        return (ImgProductDetailEntity) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_DETAIL+"_"+id);
    }

    /**

     */
    private void setCache(int id, ImgProductDetailEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_DETAIL+"_"+id, entity);
    }

    //=========================db===========================

    /**

     */
    public ImgProductDetailEntity loadDbById(int id) {
        return GameData.getDB().get(ImgProductDetailEntity.class, id);
    }

}
