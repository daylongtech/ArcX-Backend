package avatar.util.user;

import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;

import java.util.List;

/**

 */
public class UserOnlineUtil {
    /**

     */
    public static int loadGamingProduct(int userId) {
        int retPId = 0;
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserOnlineMsgEntity entity : list){
                int productId = entity.getProductId();
                if(entity.getIsGaming()== YesOrNoEnum.YES.getCode()){
                    retPId = productId;
                    break;
                }
            }
        }
        return retPId;
    }

    /**

     */
    public static boolean isOnline(int userId) {
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        return list.size()!=0 && list.get(0).getIsOnline() == YesOrNoEnum.YES.getCode();
    }

    /**

     */
    public static UserOnlineExpMsg initUserOnlineExpMsg(int userId) {
        UserOnlineExpMsg msg = new UserOnlineExpMsg();
        msg.setUserId(userId);
        msg.setCoinNum(0);
        msg.setExpNum(UserAttributeUtil.loadExpNum(userId));
        return msg;
    }


}
