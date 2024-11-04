package avatar.util.user;

import avatar.global.lockMsg.LockMsg;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
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
            e.printStackTrace();
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
    public static boolean isOnline(int userId) {
        
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        return list.size()!=0 && list.get(0).getIsOnline() == YesOrNoEnum.YES.getCode();
    }

}
