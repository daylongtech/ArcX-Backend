package avatar.util.product;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;

import java.util.List;

/**

 */
public class ProductOperateUtil {
    /**

     */
    public static void offLineProduct(int userId, int productId) {
        
        ProductRoomDao.getInstance().delUser(productId, userId);
        
        if(UserOnlineUtil.isOnline(userId)) {
            
            UserOnlineUtil.onlineMsgNoGaming(userId, productId);
        }else{
            
            UserOnlineMsgDao.getInstance().delete(userId, productId);
        }
        
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
    }

    /**

     */
    public static void kickOut(int userId, int productId) {
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            list.forEach(entity->{
                if(entity!=null && entity.getProductId()==productId && entity.getIsGaming()== YesOrNoEnum.YES.getCode()){
                    kickOutUser(userId, productId);
                }
            });
        }
    }

    /**

     */
    private static void kickOutUser(int userId, int productId) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                
                int productType = ProductUtil.loadProductType(productId);
                if (productType == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
                    
                    int operate = ProductOperationEnum.OFF_LINE.getCode();

                    
                    ProductSocketOperateService.coinPusherOperate(productId, operate, userId);
                } else {

                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
