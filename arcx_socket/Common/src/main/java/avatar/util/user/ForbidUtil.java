package avatar.util.user;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.info.UserInfoDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class ForbidUtil {
    /**

     */
    public static int checkForbidProduct(int userId, int productId) {
        int status = ClientCode.SUCCESS.getCode();
        boolean flag = true;
        
        if(flag && userId>0){
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            if(userInfoEntity!=null && userInfoEntity.getStatus()!= UserStatusEnum.NORMAL.getCode()){
                flag = false;

            }
        }
        
        if(flag && productId>0){
            ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
            if(productInfoEntity!=null &&
                    productInfoEntity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
                flag = false;

            }
        }
        if(!flag){
            status = ClientCode.SYSTEM_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    public static int checkGamingForbidProduct(int userId, int productId, boolean unlockFlag) {
        int status = ClientCode.SUCCESS.getCode();
        boolean flag = true;
        
        if(userId>0){
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            if(userInfoEntity!=null && userInfoEntity.getStatus()!= UserStatusEnum.NORMAL.getCode()){
                flag = false;

            }
        }
        
        if(flag && productId>0){
            int pId = UserOnlineUtil.loadGamingProduct(userId);
            if (pId>0 && productId!=pId) {
                flag = false;

            }
        }
        
        if(flag && productId>0){
            ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
            if(productInfoEntity!=null &&
                    productInfoEntity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
                flag = false;

            }
        }
        if(!flag){
            status = ClientCode.SYSTEM_ERROR.getCode();
        }
        return status;
    }
}
