package avatar.task.product.innoMsg;

import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.innoMsg.SelfSpecialAwardMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.Map;

/**

 */
public class InnoSpecialAwardTask extends ScheduledTask {

    
    private int userId;

    
    private int productId;

    
    private int productAwardType;

    
    private int isStart;

    public InnoSpecialAwardTask(int userId, int productId, int productAwardType, int isStart) {

        this.productId = productId;
        this.userId = userId;
        this.productAwardType = productAwardType;
        this.isStart = isStart;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNO_SPECIAL_AWARD_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                if(productAwardType== ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode()){
                    


                }else if(productAwardType== ProductAwardTypeEnum.WHISTLE.getCode()){
                    


                }else if(productAwardType== ProductAwardTypeEnum.PIRATE_CANNON.getCode()){
                    


                }
                Map<Integer, Long> awardTypeMap = SelfSpecialAwardMsgDao.getInstance().loadByProductId(productId);
                if(isStart== YesOrNoEnum.YES.getCode()){
                    awardTypeMap.put(productAwardType, TimeUtil.getNowTime());
                }else if(isStart== YesOrNoEnum.NO.getCode()){
                    awardTypeMap.remove(productAwardType);
                }
                
                SelfSpecialAwardMsgDao.getInstance().setCache(productId, awardTypeMap);
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }
}
