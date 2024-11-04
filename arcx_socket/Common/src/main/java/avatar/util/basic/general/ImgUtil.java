package avatar.util.basic.general;

import avatar.entity.basic.img.ImgAwardMsgEntity;
import avatar.entity.basic.img.ImgProductPresentMsgEntity;
import avatar.module.basic.img.ImgAwardMsgDao;
import avatar.module.basic.img.ImgProductPresentDao;

/**

 */
public class ImgUtil {
    /**

     */
    public static String loadAwardImg(int awardImgId){
        
        ImgAwardMsgEntity entity = ImgAwardMsgDao.getInstance().loadByImgId(awardImgId);
        return entity==null?"":entity.getImgUrl();
    }

    /**

     */
    public static String productAwardImg(int awardImgId) {
        String awardImgUrl = "";
        if(awardImgId>0){
            
            ImgProductPresentMsgEntity entity = ImgProductPresentDao.getInstance().loadById(awardImgId);
            awardImgUrl = entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
        }
        return awardImgUrl;
    }


}
