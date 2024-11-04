package avatar.util.basic;

import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.module.basic.systemMsg.PropertyMsgDao;

/**

 */
public class PropertyMsgUtil {
    
    public static String loadImgUrl(int propertyType){
        
        PropertyMsgEntity entity = PropertyMsgDao.getInstance().loadMsg(propertyType);
        return entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
    }
}
