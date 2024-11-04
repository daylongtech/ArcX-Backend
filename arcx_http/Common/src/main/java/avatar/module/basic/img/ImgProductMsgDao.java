package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class ImgProductMsgDao {
    private static final ImgProductMsgDao instance = new ImgProductMsgDao();
    public static final ImgProductMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public ImgProductMsgEntity loadByImgId(int imgId) {
        
        ImgProductMsgEntity entity = loadCache(imgId);
        if(entity==null){
            
            entity = loadDbById(imgId);
            if(entity!=null) {
                
                setCache(imgId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private ImgProductMsgEntity loadCache(int imgId){
        return (ImgProductMsgEntity) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_MSG+"_"+imgId);
    }

    /**

     */
    private void setCache(int imgId, ImgProductMsgEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_MSG+"_"+imgId, entity);
    }

    //=========================db===========================

    /**

     */
    private ImgProductMsgEntity loadDbById(int id) {
        return GameData.getDB().get(ImgProductMsgEntity.class, id);
    }

}
