package avatar.module.basic.img;

import avatar.entity.basic.img.ImgAwardMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class ImgAwardMsgDao {
    private static final ImgAwardMsgDao instance = new ImgAwardMsgDao();
    public static final ImgAwardMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public ImgAwardMsgEntity loadByImgId(int imgId) {
        
        ImgAwardMsgEntity entity = loadCache(imgId);
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
    private ImgAwardMsgEntity loadCache(int imgId){
        return (ImgAwardMsgEntity) GameData.getCache().get(PrefixMsg.IMG_AWARD_MSG+"_"+imgId);
    }

    /**

     */
    private void setCache(int imgId, ImgAwardMsgEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_AWARD_MSG+"_"+imgId, entity);
    }

    //=========================db===========================

    /**

     */
    private ImgAwardMsgEntity loadDbById(int id) {
        return GameData.getDB().get(ImgAwardMsgEntity.class, id);
    }

}
