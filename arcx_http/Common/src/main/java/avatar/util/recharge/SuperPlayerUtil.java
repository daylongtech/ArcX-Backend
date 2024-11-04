package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.recharge.RechargeSuperPlayerMsg;
import avatar.entity.recharge.superPlayer.SuperPlayerAwardEntity;
import avatar.entity.recharge.superPlayer.SuperPlayerConfigEntity;
import avatar.entity.recharge.superPlayer.SuperPlayerOrderEntity;
import avatar.entity.recharge.superPlayer.SuperPlayerUserMsgEntity;
import avatar.global.basicConfig.basic.RechargeConfigMsg;
import avatar.global.enumMsg.basic.recharge.PayStatusEnum;
import avatar.global.enumMsg.basic.recharge.PayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.recharge.superPlayer.*;
import avatar.task.recharge.AddSuperPlayerAwardTask;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserAttributeUtil;

import java.util.List;

/**

 */
public class SuperPlayerUtil {
    /**

     */
    public static SuperPlayerUserMsgEntity initSuperPlayerUserMsgEntity(int userId) {
        SuperPlayerUserMsgEntity entity = new SuperPlayerUserMsgEntity();
        entity.setUserId(userId);
        entity.setEffectTime("");
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static boolean isSuperPlayer(int userId) {
        boolean flag = true;
        
        SuperPlayerUserMsgEntity entity = SuperPlayerUserDao.getInstance().loadMsg(userId);
        if(TimeUtil.getNowTime()>TimeUtil.strToLong(entity.getEffectTime())){
            
            SuperPlayerUserListDao.getInstance().removeCache();
            flag = false;
        }
        return flag;
    }

    /**


     */
    public static int openSuperPlayer(int userId, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();
        boolean superPlayerFlag = SuperPlayerUtil.isSuperPlayer(userId);
        
        boolean flag = UserCostLogUtil.costSuperPlayer(userId);
        if(flag) {
            
            SuperPlayerOrderDao.getInstance().insert(initSuperPlayerOrderEntity(userId, PayTypeEnum.USDT.getCode()));
            
            addSuperUserTime(userId);
            
            addSuperPlayerAward(userId, retList);
            
            if (ParamsUtil.isSuccess(status) && !superPlayerFlag) {
                UserAttributeUtil.updateChargingTime(userId);
            }
        }else{
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();
        }
        return status;
    }

    /**

     */
    private static SuperPlayerOrderEntity initSuperPlayerOrderEntity(int userId, int payType) {
        SuperPlayerOrderEntity entity = new SuperPlayerOrderEntity();
        entity.setUserId(userId);
        entity.setOrderSn(RechargeConfigMsg.superPlayerPrefix+ StrUtil.getOrderIdByUUId());
        entity.setRechargeId("");
        entity.setPayType(payType);
        entity.setPrice(loadPrice());
        entity.setStatus(RechargeUtil.isDirectPayType(payType)?PayStatusEnum.ALREADY_PAY.getCode():
                PayStatusEnum.NO_PAY.getCode());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    private static void addSuperPlayerAward(int userId, List<GeneralAwardMsg> retList) {
        List<SuperPlayerAwardEntity> list = SuperPlayerAwardDao.getInstance().loadMsg();
        if(list.size()>0){
            
            SchedulerSample.delayed(1, new AddSuperPlayerAwardTask(userId, list));
            
            list.forEach(entity-> retList.add(AwardUtil.initGeneralAwardMsg(entity.getAwardType(), entity.getAwardImgId(),
                    entity.getAwardNum())));
        }
    }

    /**

     */
    private static void addSuperUserTime(int userId) {
        int addTime = loadSuperTime();
        if(addTime>0) {
            
            SuperPlayerUserMsgEntity entity = SuperPlayerUserDao.getInstance().loadMsg(userId);
            if (TimeUtil.getNowTime() > TimeUtil.strToLong(entity.getEffectTime())) {
                entity.setEffectTime(TimeUtil.getAfterNHour(TimeUtil.getNowTimeStr(), addTime));
            }else{
                entity.setEffectTime(TimeUtil.getAfterNHour(entity.getEffectTime(), addTime));
            }
            SuperPlayerUserDao.getInstance().update(entity);
        }
    }

    /**

     */
    private static int loadSuperTime() {
        
        SuperPlayerConfigEntity entity = SuperPlayerConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getEffectDay();
    }

    /**

     */
    public static int loadPrice() {
        
        SuperPlayerConfigEntity entity = SuperPlayerConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getPrice();
    }

    /**

     */
    public static RechargeSuperPlayerMsg rechargeSuperPlayerMsg() {
        RechargeSuperPlayerMsg msg = new RechargeSuperPlayerMsg();
        
        SuperPlayerConfigEntity entity = SuperPlayerConfigDao.getInstance().loadMsg();
        msg.setPct(entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl()));
        msg.setUsdtAmt(entity==null?0:entity.getPrice());
        return msg;
    }
}
