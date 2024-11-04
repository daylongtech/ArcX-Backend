package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductPresentMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class ImgProductPresentDao {
    private static final ImgProductPresentDao instance = new ImgProductPresentDao();
    public static final ImgProductPresentDao getInstance(){
        return instance;
    }

    /**

     */
    public ImgProductPresentMsgEntity loadById(int id) {
        
        ImgProductPresentMsgEntity entity = loadCache(id);
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
    private ImgProductPresentMsgEntity loadCache(int id){
        return (ImgProductPresentMsgEntity) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_PRESENT+"_"+id);
    }

    /**

     */
    private void setCache(int id, ImgProductPresentMsgEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_PRESENT+"_"+id, entity);
    }

    //=========================db===========================

    /**

     */
    public ImgProductPresentMsgEntity loadDbById(int id) {
        return GameData.getDB().get(ImgProductPresentMsgEntity.class, id);
    }

}
