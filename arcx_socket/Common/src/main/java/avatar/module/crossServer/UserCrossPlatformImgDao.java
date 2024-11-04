package avatar.module.crossServer;

import avatar.entity.user.info.UserDefaultHeadImgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;
import avatar.util.basic.general.MediaUtil;
import avatar.util.system.StrUtil;

/**

 */
public class UserCrossPlatformImgDao {
    private static final UserCrossPlatformImgDao instance = new UserCrossPlatformImgDao();
    public static final UserCrossPlatformImgDao getInstance(){
        return instance;
    }

    /**

     */
    public String loadMsg(){
        String imgUrl = loadCache();
        if(StrUtil.checkEmpty(imgUrl)){
            
            imgUrl = loadDbMsg();
            if(!StrUtil.checkEmpty(imgUrl)) {
                
                setCache(imgUrl);
            }
        }
        return imgUrl;
    }

    //=========================cache===========================

    /**

     */
    private String loadCache(){
        Object obj = GameData.getCache().get(CrossServerPrefixMsg.USER_CROSS_PLATFORM_IMG);
        return obj==null?"":obj.toString();
    }

    /**

     */
    private void setCache(String imgUrl){
        
        GameData.getCache().set(CrossServerPrefixMsg.USER_CROSS_PLATFORM_IMG, imgUrl);
    }


    //=========================db===========================

    /**

     */
    private String loadDbMsg() {
        String sql = "select * from user_default_head_img where is_cross_platform=?";
        UserDefaultHeadImgEntity entity = GameData.getDB().get(UserDefaultHeadImgEntity.class, sql,
                new Object[]{YesOrNoEnum.YES.getCode()});
        if(entity!=null){
            return MediaUtil.getMediaUrl(entity.getImgUrl());
        }else{
            return "";
        }
    }


}
