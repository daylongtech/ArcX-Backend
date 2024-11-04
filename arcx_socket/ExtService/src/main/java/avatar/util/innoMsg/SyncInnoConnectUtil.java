package avatar.util.innoMsg;

import avatar.entity.product.innoMsg.SyncInnoProductIpEntity;
import avatar.global.Config;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.innoMsg.SyncInnoProductIpDao;
import avatar.module.product.socket.ConnectSocketLinkDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**

 */
public class SyncInnoConnectUtil {
    /**

     */
    public static void connectInnoProductServer() {
        
        List<SyncInnoProductIpEntity> list = SyncInnoProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort().equals(Config.getInstance().getWebSocketPort()+"")){
                    SyncInnoOperateUtil.loadClient(entity.getToIp(), Integer.parseInt(entity.getToPort()));
                }
            });
        }else{

        }
    }

    /**

     */
    public static String linkMsg(String ip, int port){
        return ip+port+loadHostId(ip, port+"");
    }

    /**

     */
    public static int loadHostId(String ip, String port) {
        int hostId = 0;
        List<SyncInnoProductIpEntity> list = SyncInnoProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            for(SyncInnoProductIpEntity entity : list){
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort().equals(Config.getInstance().getWebSocketPort()+"") &&
                        entity.getToIp().equals(ip) && entity.getToPort().equals(port)){
                    hostId = entity.getUserId();
                    break;
                }
            }
        }
        return hostId;
    }

    /**

     */
    public static boolean isOutTimeLink(String linkMsg) {
        boolean flag = false;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.CONNECT_SOCKET_LINK_LOCK+linkMsg,
                2000);
        try {
            if (lock.lock()) {
                long time = ConnectSocketLinkDao.getInstance().loadByLinkMsg(linkMsg);
                if((TimeUtil.getNowTime()-time)>2000){
                    
                    flag = true;
                    
                    ConnectSocketLinkDao.getInstance().setCache(linkMsg, TimeUtil.getNowTime());
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }
}
