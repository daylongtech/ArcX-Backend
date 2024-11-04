package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.recharge.RechargeCoinMsg;
import avatar.entity.recharge.gold.RechargeGoldInfoEntity;
import avatar.entity.recharge.gold.RechargeGoldOrderEntity;
import avatar.global.basicConfig.basic.RechargeConfigMsg;
import avatar.global.enumMsg.basic.recharge.PayStatusEnum;
import avatar.global.enumMsg.basic.recharge.PayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.recharge.gold.GoldUsdtConfigDao;
import avatar.module.recharge.gold.RechargeGoldInfoDao;
import avatar.module.recharge.gold.RechargeGoldListDao;
import avatar.module.recharge.gold.RechargeGoldOrderDao;
import avatar.task.recharge.AddOfficialRechargeCoinAwardTask;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class RechargeGoldUtil {
    /**

     */
    public static List<RechargeCoinMsg> loadCoinList() {
        List<RechargeCoinMsg> retList = new ArrayList<>();
        List<Integer> list = RechargeGoldListDao.getInstance().loadMsg();
        if(list.size()>0){
            list.forEach(id-> retList.add(initRechargeCoinMsg(id)));
        }
        return retList;
    }

    /**

     */
    private static RechargeCoinMsg initRechargeCoinMsg(int id) {
        RechargeCoinMsg msg = new RechargeCoinMsg();
        msg.setCmdId(id);
        
        RechargeGoldInfoEntity entity = RechargeGoldInfoDao.getInstance().loadById(id);
        if(entity!=null){
            msg.setCnAmt(goldNum(entity.getPrice()));
            msg.setUsdtAmt(entity.getPrice());
            msg.setPct(MediaUtil.getMediaUrl(entity.getImgUrl()));
        }
        return msg;
    }

    /**

     */
    public static long goldNum(int price){
        return GoldUsdtConfigDao.getInstance().loadMsg()*(long)price;
    }

    /**

     */
    public static long basicGoldNum(){
        return GoldUsdtConfigDao.getInstance().loadMsg();
    }

    /**

     */
    public static int rechargeGold(int userId, int productId, int commodityId, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();
        
        RechargeGoldInfoEntity goldInfoEntity = RechargeGoldInfoDao.getInstance().loadById(commodityId);
        
        boolean flag = UserCostLogUtil.costOfficialRechargeGold(userId, goldInfoEntity.getPrice());
        if(flag) {
            
            RechargeGoldOrderDao.getInstance().insert(initRechargeGoldOrderEntity(userId, productId,
                    goldInfoEntity, PayTypeEnum.USDT.getCode()));
            
            addRechargeGoldAward(userId, goldInfoEntity, retList);
        }else{
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();
        }
        return status;
    }

    /**

     */
    private static void addRechargeGoldAward(int userId, RechargeGoldInfoEntity entity,
            List<GeneralAwardMsg> retList) {
        long goldNum = goldNum(entity.getPrice());
        
        retList.add(AwardUtil.initGeneralAwardMsg(CommodityUtil.gold(), MediaUtil.getMediaUrl(entity.getImgUrl()),
                goldNum));
        
        SchedulerSample.delayed(1, new AddOfficialRechargeCoinAwardTask(userId, goldNum));
    }

    /**

     */
    private static RechargeGoldOrderEntity initRechargeGoldOrderEntity(int userId, int productId,
            RechargeGoldInfoEntity goldInfoEntity, int payType) {
        RechargeGoldOrderEntity entity = new RechargeGoldOrderEntity();
        entity.setUserId(userId);
        entity.setCommodityId(goldInfoEntity.getId());
        entity.setCommodityNum(goldNum(goldInfoEntity.getPrice()));
        entity.setOrderSn(RechargeConfigMsg.rechargeGoldPrefix+ StrUtil.getOrderIdByUUId());
        entity.setRechargeId("");
        entity.setPayType(payType);
        entity.setProductId(productId);
        entity.setPrice(goldInfoEntity.getPrice());
        entity.setStatus(RechargeUtil.isDirectPayType(payType)?PayStatusEnum.ALREADY_PAY.getCode():
                PayStatusEnum.NO_PAY.getCode());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }
}
