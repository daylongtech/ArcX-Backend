package avatar.util.innoMsg;

import avatar.global.Config;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.innoMsg.SyncInnoHeartTimeDao;
import avatar.module.product.innoMsg.SyncInnoReconnectDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.ReconnectInnoSocketTask;
import avatar.task.innoMsg.SyncInnoHeartTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

/**

 */
public class SyncInnoDealUtil {
    /**

     */
    public static void socketOpen(String host, int port) {
        

        long startHeartTime = TimeUtil.getNowTime();
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        
        SyncInnoHeartTimeDao.getInstance().setCache(linkMsg, startHeartTime);
        SchedulerSample.delayed(2000, new SyncInnoHeartTask(host, port, startHeartTime));
        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        
        socketCloseDeal(fromKey, linkMsg);
    }

    /**

     */
    public static void socketClose(String host, int port) {

        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        String toKey = SyncInnoConnectUtil.linkMsg(host, port);//to key
        
        SyncInnoOperateUtil.webSocket.remove(toKey);
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SYNC_INNO_RECONNECT_LOCK+toKey,
                2000);
        try {
            if (lock.lock()) {
                int status = SyncInnoReconnectDao.getInstance().loadMsg(fromKey, toKey);
                if(status== YesOrNoEnum.NO.getCode()){
                    SyncInnoReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.YES.getCode());
                    
                    SchedulerSample.delayed(3000, new ReconnectInnoSocketTask(host, port, fromKey, toKey));
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
        int uId = SyncInnoConnectUtil.loadHostId(host, port+"");
        String toKey = host+port+uId;//to key
        
        SyncInnoOperateUtil.webSocket.remove(toKey);
        
        socketCloseDeal(fromKey, toKey);
    }

    /**

     */
    public static void socketCloseDeal(String fromKey, String toKey) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SYNC_INNO_RECONNECT_LOCK,
                2000);
        try {
            if (lock.lock()) {
                SyncInnoReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.NO.getCode());
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
