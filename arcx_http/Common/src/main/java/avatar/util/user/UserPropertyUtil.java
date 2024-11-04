package avatar.util.user;

import avatar.data.user.balance.PropertyKnapsackMsg;
import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.entity.user.info.UserPropertyMsgEntity;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.module.basic.systemMsg.PropertyListDao;
import avatar.module.basic.systemMsg.PropertyMsgDao;
import avatar.module.user.info.UserPropertyMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.user.PropertyUserAwardTask;
import avatar.util.LogUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.List;

/**

 */
public class UserPropertyUtil {
    /**

     */
    public static UserPropertyMsgEntity initUserPropertyMsgEntity(int userId, int propertyType) {
        UserPropertyMsgEntity entity = new UserPropertyMsgEntity();
        entity.setUserId(userId);
        entity.setPropertyType(propertyType);
        entity.setNum(0);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }

    /**

     */
    public static long getUserProperty(int userId, int propertyType) {
        
        UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
        return entity==null?0:entity.getNum();
    }

    /**

     */
    public static boolean addUserProperty(int userId, int propertyType, int num) {
        if(num>0) {
            boolean flag = false;
            
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
                    if (entity != null) {
                        entity.setNum(entity.getNum() + num);
                        flag = UserPropertyMsgDao.getInstance().update(entity);
                    } else {

                                PropertyTypeEnum.getNameByCode(propertyType), num);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
            return flag;
        }else{
            return true;
        }
    }

    /**

     */
    public static boolean costUserProperty(int userId, int propertyType, int costNum) {
        boolean flag = false;
        if(costNum==0){
            return true;
        }
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                if(getUserProperty(userId, propertyType)>=costNum) {
                    
                    UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
                    if (entity != null) {
                        long num = entity.getNum();
                        entity.setNum(Math.max(0, (num - costNum)));
                        flag = UserPropertyMsgDao.getInstance().update(entity);
                    } else {

                                PropertyTypeEnum.getNameByCode(propertyType), costNum);
                    }
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
    public static void propertyKnapsack(int userId, List<Integer> list, List<PropertyKnapsackMsg> retList) {
        if(list.size()>0){
            list.forEach(propertyType->{
                
                UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
                if(entity.getNum()>0){
                    retList.add(initPropertyKnapsackMsg(propertyType, entity.getNum()));
                }
            });
        }
    }

    /**

     */
    private static PropertyKnapsackMsg initPropertyKnapsackMsg(int propertyType, long num) {
        PropertyKnapsackMsg msg = new PropertyKnapsackMsg();
        
        PropertyMsgEntity entity = PropertyMsgDao.getInstance().loadMsg(propertyType);
        msg.setPptTp(propertyType);
        msg.setNm(entity==null?"":entity.getName());
        msg.setDsc(entity==null?"":entity.getDesc());
        msg.setPct(entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl()));
        msg.setPpyAmt(num);
        return msg;
    }

    /**

     */
    public static int useProperty(int userId, int propertyType) {
        int status = ClientCode.PROPERTY_NO_ENOUGH.getCode();
        
        UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
        if(entity!=null && entity.getNum()>0){
            
            if(PropertyListDao.getInstance().loadMsg().contains(propertyType)){
                boolean flag = costUserProperty(userId, propertyType, 1);
                if(flag){
                    status = ClientCode.SUCCESS.getCode();
                }
            }else{
                status = ClientCode.INVALID_COMMODITY.getCode();
            }
        }
        if(ParamsUtil.isSuccess(status)){
            
            SchedulerSample.delayed(1, new PropertyUserAwardTask(userId, propertyType));
        }
        return status;
    }
}
