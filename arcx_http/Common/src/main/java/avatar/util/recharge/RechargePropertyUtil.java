package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.recharge.RechargePropertyDetailMsg;
import avatar.data.recharge.RechargePropertyMsg;
import avatar.data.recharge.UserRechargePropertyMsg;
import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.entity.recharge.property.RechargePropertyConfigEntity;
import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.entity.recharge.property.RechargePropertyOrderEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.basic.systemMsg.PropertyListDao;
import avatar.module.basic.systemMsg.PropertyMsgDao;
import avatar.module.recharge.property.*;
import avatar.task.recharge.AddRechargePropertyAwardTask;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.basic.PropertyMsgUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class RechargePropertyUtil {
    /**

     */
    private static int showNum() {
        
        RechargePropertyConfigEntity entity = RechargePropertyConfigDao.getInstance().loadMsg();
        int showNum = entity==null?0:entity.getShowNum();
        return showNum==0?6:showNum;
    }

    /**

     */
    public static List<Integer> loadOnlineList() {
        List<Integer> retList = new ArrayList<>();
        int showNum = showNum();
        if(showNum>0){
            List<Integer> list = RechargePropertyListDao.getInstance().loadMsg();
            Collections.shuffle(list);
            for(int i=0;i<showNum;i++){
                retList.add(list.get(i));
                if(retList.size()==list.size()){
                    break;
                }
            }
        }
        return retList;
    }

    /**

     */
    public static RechargePropertyMsg loadPropertyMsg(int userId) {
        RechargePropertyMsg msg = new RechargePropertyMsg();
        msg.setRfTm((TimeUtil.getZeroLongTime(TimeUtil.getNextDay(TimeUtil.getNowTimeStr()))-TimeUtil.getNowTime())/1000);
        msg.setRfAxcAmt(loadRefreshAxcPrice());
        msg.setPpyTbln(loadPropertyList(userId));
        return msg;
    }

    /**

     */
    public static List<RechargePropertyDetailMsg> loadPropertyList(int userId) {
        List<RechargePropertyDetailMsg> retList = new ArrayList<>();
        List<Integer> propertyList = new ArrayList<>();
        
        UserRechargePropertyMsg userMsg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        if (userMsg != null && userMsg.getPropertyList().size() > 0) {
            propertyList = userMsg.getPropertyList();
        }
        if(propertyList.size()==0){
            
            propertyList = RechargePropertyOnlineListDao.getInstance().loadMsg();
        }
        if(propertyList.size()>0){
            
            List<Integer> activeList = PropertyListDao.getInstance().loadMsg();
            
            propertyList.forEach(id-> {
                if(activeList.contains(id)) {
                    RechargePropertyDetailMsg msg = initRechargePropertyDetailMsg(id, userMsg.getBuyList());
                    if (msg != null) {
                        retList.add(msg);
                    }
                }
            });
        }
        return retList;
    }

    /**

     */
    public static List<Integer> userActiveList(int userId){
        List<Integer> retList = new ArrayList<>();
        List<RechargePropertyDetailMsg> list = loadPropertyList(userId);
        if(list.size()>0){
            list.forEach(msg-> retList.add(msg.getCmdId()));
        }
        return retList;
    }

    /**

     */
    private static RechargePropertyDetailMsg initRechargePropertyDetailMsg(int id, List<Integer> buyList) {
        RechargePropertyDetailMsg msg = new RechargePropertyDetailMsg();
        msg.setCmdId(id);
        
        RechargePropertyMsgEntity entity = RechargePropertyMsgDao.getInstance().loadMsg(id);
        if(entity!=null){
            msg.setPpyAmt(entity.getNum());
            
            PropertyMsgEntity msgEntity = PropertyMsgDao.getInstance().loadMsg(entity.getPropertyType());
            if(msgEntity!=null) {
                msg.setNm(msgEntity.getName());
                msg.setDsc(msgEntity.getDesc());
                msg.setPct(MediaUtil.getMediaUrl(msgEntity.getImgUrl()));
            }
            msg.setAxcAmt(entity.getPrice());
            msg.setSoFlg((!ParamsUtil.isConfirm(entity.getActiveFlag()) || buyList.contains(id))?
                    YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
        }else{
            return null;
        }
        return msg;
    }

    /**

     */
    private static int loadRefreshAxcPrice() {
        
        RechargePropertyConfigEntity entity = RechargePropertyConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getRefreshPrice();
    }

    /**

     */
    public static UserRechargePropertyMsg initUserRechargePropertyMsg(int userId) {
        UserRechargePropertyMsg msg = new UserRechargePropertyMsg();
        msg.setUserId(userId);
        msg.setRefreshTime(TimeUtil.getNowTime());
        msg.setPropertyList(new ArrayList<>());
        msg.setBuyList(new ArrayList<>());
        return msg;
    }

    /**


     */
    public static UserRechargePropertyMsg dealUserRetPropertyMsg(UserRechargePropertyMsg msg) {
        if(TimeUtil.getZeroLongTime(TimeUtil.getNextDay(TimeUtil.getNowTimeStr()))>msg.getRefreshTime()){
            msg.setPropertyList(new ArrayList<>());
            msg.setBuyList(new ArrayList<>());
            
            UserRechargePropertyListDao.getInstance().setCache(msg.getUserId(), msg);
        }
        return msg;
    }

    /**

     */
    public static int refreshMallProperty(int userId) {
        int status = ClientCode.SUCCESS.getCode();
        int costAxc = loadRefreshAxc();
        if(costAxc>0){
            boolean flag = UserCostLogUtil.refreshMallProperty(userId, costAxc);
            if(!flag){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }else{
                
                refreshUserPropertyList(userId);
            }
        }else{
            status = ClientCode.SYSTEM_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    private static void refreshUserPropertyList(int userId) {
        UserRechargePropertyMsg msg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        msg.setRefreshTime(TimeUtil.getNowTime());
        msg.setPropertyList(loadOnlineList());
        msg.setBuyList(new ArrayList<>());
        UserRechargePropertyListDao.getInstance().setCache(userId, msg);
    }

    /**

     */
    private static int loadRefreshAxc() {
        
        RechargePropertyConfigEntity entity = RechargePropertyConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getRefreshPrice();
    }

    /**

     */
    public static int rechargeProperty(int userId, int commodityId, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();
        if(buyLimit(userId, commodityId)){
            status = ClientCode.BUY_LIMIT.getCode();
        }else {
            
            RechargePropertyMsgEntity entity = RechargePropertyMsgDao.getInstance().loadMsg(commodityId);
            
            boolean flag = UserCostLogUtil.costRechargeProperty(userId, entity.getPrice());
            if (flag) {
                
                RechargePropertyOrderDao.getInstance().insert(initRechargePropertyOrderEntity(userId, entity));
                
                addRechargePropertyAward(userId, entity, retList);
                
                addUserBuyProperty(userId, entity.getId());
            } else {
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }
        }
        return status;
    }

    /**

     */
    private static boolean buyLimit(int userId, int commodityId) {
        
        UserRechargePropertyMsg msg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        return msg.getBuyList().contains(commodityId);
    }

    /**

     */
    private static void addUserBuyProperty(int userId, int id) {
        
        UserRechargePropertyMsg msg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        
        List<Integer> buyList = msg.getBuyList();
        if(!buyList.contains(id)){
            buyList.add(id);
            msg.setBuyList(buyList);
            UserRechargePropertyListDao.getInstance().setCache(userId, msg);
        }
    }

    /**

     */
    private static void addRechargePropertyAward(int userId, RechargePropertyMsgEntity entity,
            List<GeneralAwardMsg> retList) {
        
        retList.add(AwardUtil.initGeneralAwardMsg(CommodityUtil.property(), PropertyMsgUtil.loadImgUrl(entity.getPropertyType()),
                entity.getNum()));
        
        SchedulerSample.delayed(1, new AddRechargePropertyAwardTask(userId, entity));
    }

    /**

     */
    private static RechargePropertyOrderEntity initRechargePropertyOrderEntity(int userId,
            RechargePropertyMsgEntity msgEntity) {
        RechargePropertyOrderEntity entity = new RechargePropertyOrderEntity();
        entity.setUserId(userId);
        entity.setPropertyType(msgEntity.getPropertyType());
        entity.setPropertyType(msgEntity.getPrice());
        entity.setNum(msgEntity.getNum());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }
}
