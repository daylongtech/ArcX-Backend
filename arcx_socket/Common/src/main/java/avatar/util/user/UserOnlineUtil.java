package avatar.util.user;

import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.online.UserOnlineExpMsgDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**

 */
public class UserOnlineUtil {
    /**

     */
    public static void onlineMsgOnline(int userId, String localIp, int localPort) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()==0){
                    UserOnlineMsgDao.getInstance().insert(initUserOnlineMsgEntity(userId, localIp, localPort, 0));
                }else{
                    list.forEach(entity->{
                        if(entity.getIsOnline()== YesOrNoEnum.NO.getCode()){
                            entity.setIsOnline(YesOrNoEnum.YES.getCode());
                            UserOnlineMsgDao.getInstance().update(0, entity);
                        }
                    });
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
    private static UserOnlineMsgEntity initUserOnlineMsgEntity(int userId, String ip, int port, int productId){
        UserOnlineMsgEntity entity = new UserOnlineMsgEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setIsOnline(YesOrNoEnum.YES.getCode());
        entity.setIsGaming(YesOrNoEnum.NO.getCode());
        entity.setIp(ip);//ip
        entity.setPort(port+"");
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void delUserOnlineMsg(int userId) {
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                
                if(!ParamsUtil.isConfirm(entity.getIsGaming())){
                    UserOnlineMsgDao.getInstance().delete(entity);
                }
            });
        }
    }

    /**

     */
    public static void onlineMsgNoGaming(int userId, int productId) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        UserOnlineMsgEntity entity = list.get(i);
                        if(entity.getIsGaming()==YesOrNoEnum.YES.getCode() && entity.getProductId()==productId){
                            if(i==0){
                                if(ParamsUtil.isConfirm(entity.getIsOnline())) {
                                    
                                    entity.setIsGaming(YesOrNoEnum.NO.getCode());
                                    UserOnlineMsgDao.getInstance().update(0, entity);
                                }else{
                                    
                                    UserOnlineMsgDao.getInstance().delete(entity);
                                }
                            }else{
                                
                                UserOnlineMsgDao.getInstance().delete(entity);
                            }
                        }
                    }
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
    public static boolean isOnline(int userId) {
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        return list.size()!=0 && list.get(0).getIsOnline() == YesOrNoEnum.YES.getCode();
    }

    /**

     */
    public static boolean isNoStreeSettlementGaming(int userId) {
        boolean isGaming = false;
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserOnlineMsgEntity entity : list){
                if(entity.getIsGaming()==YesOrNoEnum.YES.getCode()){
                    isGaming = true;
                    break;
                }
            }
        }
        return isGaming;
    }

    /**

     */
    public static void startGameProduct(int userId, int productId, String serverIp, int serverPort) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                boolean onProductFlag = false;
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId){
                            onProductFlag = true;
                            entity.setIsGaming(YesOrNoEnum.YES.getCode());
                            entity.setCreateTime(TimeUtil.getNowTimeStr());
                            UserOnlineMsgDao.getInstance().update(0, entity);
                            break;
                        }
                    }
                }
                if(!onProductFlag){
                    if(list.size()==1 && list.get(0).getProductId()==0){
                        
                        UserOnlineMsgEntity entity = list.get(0);
                        entity.setProductId(productId);
                        entity.setIsGaming(YesOrNoEnum.YES.getCode());
                        UserOnlineMsgDao.getInstance().update(0, entity);
                    }else {
                        
                        UserOnlineMsgEntity entity = initUserOnlineMsgEntity(userId, serverIp, serverPort, productId);
                        entity.setIsGaming(YesOrNoEnum.YES.getCode());
                        UserOnlineMsgDao.getInstance().insert(entity);
                    }
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
    public static int loadOnlineProduct(int userId){
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        return list.size()==0?0:list.get(0).getProductId();
    }

    /**

     */
    public static void joinProductMsg(int userId, int productId, String serverIp, int serverPort) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                boolean onProductFlag = false;
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId){
                            onProductFlag = true;
                            entity.setCreateTime(TimeUtil.getNowTimeStr());
                            UserOnlineMsgDao.getInstance().update(0, entity);
                            break;
                        }
                    }
                }
                if(!onProductFlag){
                    if(list.size()==1 && list.get(0).getProductId()==0){
                        
                        UserOnlineMsgEntity entity = list.get(0);
                        entity.setProductId(productId);
                        UserOnlineMsgDao.getInstance().update(0, entity);
                    }else {
                        
                        UserOnlineMsgDao.getInstance().insert(initUserOnlineMsgEntity(userId, serverIp, serverPort, productId));
                    }
                }
                
                List<UserOnlineMsgEntity> newList = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(newList.size()>0){
                    newList.forEach(entity->{
                        if(entity.getProductId()!=productId && entity.getIsGaming()!=YesOrNoEnum.YES.getCode()){
                            UserOnlineMsgDao.getInstance().delete(entity);
                        }
                    });
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
    public static void exitProductMsg(int userId, int productId) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId && entity.getIsGaming()!=YesOrNoEnum.YES.getCode()){
                            if(list.size()==1){
                                
                                entity.setProductId(0);
                                UserOnlineMsgDao.getInstance().update(productId, entity);
                            }else{
                                
                                UserOnlineMsgDao.getInstance().delete(entity);
                            }
                            break;
                        }
                    }
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
    public static int loadGamingProduct(int userId) {
        int retPId = 0;
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserOnlineMsgEntity entity : list){
                int productId = entity.getProductId();
                if(entity.getIsGaming()==YesOrNoEnum.YES.getCode()){
                    retPId = productId;
                    break;
                }
            }
        }
        return retPId;
    }

    /**

     */
    public static void onlineMsgNoOnline(int userId, int productId) {
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId){
                            if(entity.getIsGaming()==YesOrNoEnum.NO.getCode()){
                                
                                UserOnlineMsgDao.getInstance().delete(entity);
                            }else{
                                
                                entity.setIsOnline(YesOrNoEnum.NO.getCode());
                                UserOnlineMsgDao.getInstance().update(0, entity);
                            }
                        }
                        break;
                    }
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
    public static void updateUserExp(int userId) {
        
        UserOnlineExpMsg expMsg = UserOnlineExpMsgDao.getInstance().loadByMsg(userId);
        
        UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
        if(expMsg!=null && entity!=null && expMsg.getExpNum()!=entity.getUserLevelExp()){
            entity.setUserLevelExp(expMsg.getExpNum());
            UserAttributeMsgDao.getInstance().update(entity);
        }
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
