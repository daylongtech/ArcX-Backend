package avatar.util.basic;

import avatar.entity.basic.img.ImgAwardMsgEntity;
import avatar.entity.basic.img.ImgNftEntity;
import avatar.entity.basic.img.ImgProductDetailEntity;
import avatar.entity.basic.img.ImgProductMsgEntity;
import avatar.module.basic.img.ImgAwardMsgDao;
import avatar.module.basic.img.ImgNftDao;
import avatar.module.basic.img.ImgProductDetailDao;
import avatar.module.basic.img.ImgProductMsgDao;

/**

 */
public class ImgUtil {
    /**

     */
    public static String loadProductImg(int productImgId) {
        
        ImgProductMsgEntity entity = ImgProductMsgDao.getInstance().loadByImgId(productImgId);
        return entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
    }

    /**

     */
    public static String loadProductDetailImg(int imgProductDetailId) {
        String retMsg = "";
        ImgProductDetailEntity entity = ImgProductDetailDao.getInstance().loadById(imgProductDetailId);
        if(entity!=null){
            retMsg = entity.getFileUrl();
        }
        return MediaUtil.getMediaUrl(retMsg);
    }

    /**

     */
    public static String loadAwardImg(int awardImgId){
        
        ImgAwardMsgEntity entity = ImgAwardMsgDao.getInstance().loadByImgId(awardImgId);
        return entity==null?"":entity.getImgUrl();
    }

    /**

     */
    public static String nftImg(int imgId) {
        ImgNftEntity entity = ImgNftDao.getInstance().loadByImgId(imgId);
        return entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
    }

}
