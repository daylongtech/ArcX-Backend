package avatar.module.basic.img;

import avatar.entity.basic.img.ImgNftEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class ImgNftDao {
    private static final ImgNftDao instance = new ImgNftDao();
    public static final ImgNftDao getInstance(){
        return instance;
    }

    /**

     */
    public ImgNftEntity loadByImgId(int imgId) {
        
        ImgNftEntity entity = loadCache(imgId);
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
    private ImgNftEntity loadCache(int imgId){
        return (ImgNftEntity) GameData.getCache().get(PrefixMsg.IMG_NFT+"_"+imgId);
    }

    /**

     */
    private void setCache(int imgId, ImgNftEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_NFT+"_"+imgId, entity);
    }

    //=========================db===========================

    /**

     */
    private ImgNftEntity loadDbById(int id) {
        return GameData.getDB().get(ImgNftEntity.class, id);
    }

}
