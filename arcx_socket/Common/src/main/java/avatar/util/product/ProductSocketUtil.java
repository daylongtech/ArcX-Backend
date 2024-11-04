package avatar.util.product;

import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.UserJoinProductDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**

 */
public class ProductSocketUtil {
    
    public static ConcurrentMap<Integer, List<Session>> sessionMap = new ConcurrentHashMap<>();

    /**

     */
    public static boolean isSessionOnline(int userId, int productId) {
        String accessToken = UserUtil.loadAccessToken(userId);
        boolean flag = false;
        if(!StrUtil.checkEmpty(accessToken)) {
            List<Session> sessionList = ProductSocketUtil.sessionMap.get(productId);
            if (sessionList.size() > 0) {
                for (Session session : sessionList) {
                    if (session != null && accessToken.equals(session.getAccessToken())) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    /**

     */
    public static List<Session> dealOnlineSession(int productId) {
        List<Session> retSessionList = new ArrayList<>();
        List<Session> sessionList = ProductSocketUtil.sessionMap.get(productId);
        if(sessionList!=null && sessionList.size()>0){
            sessionList.forEach(session -> {
                if(session!=null){
                    retSessionList.add(session);
                }
            });
            if(sessionList.size()!=retSessionList.size()){
                ProductSocketUtil.sessionMap.put(productId, retSessionList);
            }
        }
        return retSessionList;
    }

    /**

     */
    public static void dealOffLineSession(int userId) {
        String accessToken = UserUtil.loadAccessToken(userId);
        List<Integer> list = UserJoinProductDao.getInstance().loadByMsg(userId);
        if(list.size()>0){
            
            list.forEach(productId->{

                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                        2000);
                try {
                    if (lock.lock()) {
                        List<Session> sessionList = dealOnlineSession(productId);
                        if(sessionList.size()>0) {
                            List<Session> newSessionList = new ArrayList<>(sessionList);
                            newSessionList.removeIf(ses -> (ses.getAccessToken().equals(accessToken)));
                            sessionMap.put(productId, newSessionList);
                            
                            joinProductMsgDeal(userId, productId, false);
                        }
                    }
                }catch (Exception e){
                    ErrorDealUtil.printError(e);
                }finally {
                    lock.unlock();
                }
            });
        }
    }

    /**

     */
    private static void joinProductMsgDeal(int userId, int productId, boolean joinRoomFlag) {
        if(userId>0){
            List<Integer> list = UserJoinProductDao.getInstance().loadByMsg(userId);
            if(joinRoomFlag){
                
                if(!list.contains(productId)){
                    list.add(productId);
                    UserJoinProductDao.getInstance().setCache(userId, list);
                }
            }else{
                
                if(list.contains(productId)){
                    List<Integer> newList = new ArrayList<>(list);
                    newList.removeIf(pId -> (pId==productId));
                    UserJoinProductDao.getInstance().setCache(userId, newList);
                }
            }
        }
    }

    /**


     */
    public static void dealOffLineSession(int userId, int pId) {
        String accessToken = UserUtil.loadAccessToken(userId);
        List<Integer> list = UserJoinProductDao.getInstance().loadByMsg(userId);
        if(list.size()>0){
            
            list.forEach(productId->{
                if(productId==pId){

                    RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                            2000);
                    try {
                        if (lock.lock()) {
                            List<Session> sessionList = dealOnlineSession(productId);
                            if(sessionList.size()>0) {
                                List<Session> newSessionList = new ArrayList<>(sessionList);
                                newSessionList.removeIf(ses -> (ses.getAccessToken().equals(accessToken)));
                                sessionMap.put(productId, newSessionList);
                                
                                joinProductMsgDeal(userId, productId, false);
                            }
                        }
                    }catch (Exception e){
                        ErrorDealUtil.printError(e);
                    }finally {
                        lock.unlock();
                    }
                }
            });
        }
    }

    /**

     */
    public static void joinProduct(int productId, Session session) {
        List<Session> sessionList = dealOnlineSession(productId);
        if(!sessionList.contains(session)) {
            sessionList.add(session);
            sessionMap.put(productId, sessionList);
            int userId = UserUtil.loadUserIdByToken(session.getAccessToken());
            
            joinProductMsgDeal(userId, productId, true);
        }

    }

    /**

     */
    public static void exitProduct(int productId, Session session) {
        List<Session> sessionList = dealOnlineSession(productId);
        if(sessionList.size()>0){
            List<Session> newSessionList = new ArrayList<>(sessionList);
            newSessionList.removeIf(ses -> (ses==session || ses.equals(session)));
            sessionMap.put(productId, newSessionList);
            int userId = UserUtil.loadUserIdByToken(session.getAccessToken());
            
            joinProductMsgDeal(userId, productId, false);
        }

                sessionMap.get(productId).size());
    }

    /**

     */
    public static void dealJoinProduct(int productId, Session session) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                joinProduct(productId, session);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
