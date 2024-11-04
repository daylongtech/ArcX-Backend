package avatar.util.normalProduct;

import avatar.global.Config;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.normalProduct.InnerNormalProductHeartTimeDao;
import avatar.module.product.normalProduct.InnerNormalProductReconnectDao;
import avatar.service.jedis.RedisLock;
import avatar.task.normalProduct.InnerNormalProductHeartTask;
import avatar.task.normalProduct.ReconnectInnerNormalProductSocketTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

/**

 */
public class InnerNormalProductDealUtil {
    /**

     */
    public static void socketOpen(String host, int port) {

        long startHeartTime = TimeUtil.getNowTime();
        int uId = InnerNormalProductConnectUtil.loadUid(host, port);
        String linkMsg = host+port+uId;
        
        InnerNormalProductHeartTimeDao.getInstance().setCache(linkMsg, startHeartTime);
        SchedulerSample.delayed(2000, new InnerNormalProductHeartTask(host, port, startHeartTime));
        String fromKey = Config.getInstance().getLocalAddr() + Config.getInstance().getWebSocketPort();//from key
        String toKey = host+port+uId;//to key
        
        socketCloseDeal(fromKey, toKey);
    }

    /**

     */
    public static void socketCloseDeal(String fromKey, String toKey) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNER_NORMAL_PRODUCT_RECONNECT_LOCK+toKey,
                2000);
        try {
            if (lock.lock()) {
                InnerNormalProductReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.NO.getCode());
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    public static void socketClose(String host, int port) {

        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        String toKey = host+port+ InnerNormalProductConnectUtil.loadUid(host, port);//to key
        
        InnerNormalProductOperateUtil.webSocket.remove(toKey);
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNER_NORMAL_PRODUCT_RECONNECT_LOCK+toKey,
                2000);
        try {
            if (lock.lock()) {
                int status = InnerNormalProductReconnectDao.getInstance().loadMsg(fromKey, toKey);
                if(status== YesOrNoEnum.NO.getCode()){
                    InnerNormalProductReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.YES.getCode());
                    
                    SchedulerSample.delayed(3000, new ReconnectInnerNormalProductSocketTask(host, port));
                }else{

                            host, port);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    public static void socketError(String host, int port) {

        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        String toKey = host+port+ InnerNormalProductConnectUtil.loadUid(host, port);//to key
        
        InnerNormalProductOperateUtil.webSocket.remove(toKey);
        
        socketCloseDeal(fromKey, toKey);
    }
}
