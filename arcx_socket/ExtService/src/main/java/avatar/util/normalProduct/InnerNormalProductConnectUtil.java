package avatar.util.normalProduct;

import avatar.entity.product.normalProduct.InnerNormalProductIpEntity;
import avatar.global.Config;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.normalProduct.InnerNormalProductIpDao;
import avatar.module.product.socket.ConnectSocketLinkDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**

 */
public class InnerNormalProductConnectUtil {
    /**

     */
    public static void connectProductServer() {
        List<InnerNormalProductIpEntity> list = InnerNormalProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort()== Config.getInstance().getWebSocketPort()){
                    InnerNormalProductOperateUtil.loadClient(entity.getToIp(), entity.getToPort());
                }
            });
        }else{

        }
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

    /**

     */
    public static int loadUid(String host, int port) {
        int hostId = 0;
        List<InnerNormalProductIpEntity> list = InnerNormalProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            for(InnerNormalProductIpEntity entity : list){
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort()== Config.getInstance().getWebSocketPort() &&
                        entity.getToIp().equals(host) && entity.getToPort()==port){
                    hostId = entity.getUserId();
                    break;
                }
            }
        }
        return hostId;
    }

}
