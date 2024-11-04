package avatar.util.activity;

import avatar.data.activity.welfare.WelfareSignAwardMsg;
import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.activity.sign.info.WelfareSignAwardEntity;
import avatar.entity.activity.sign.user.WelfareSignUserMsgEntity;
import avatar.global.basicConfig.basic.ActivityConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.activity.sign.WelfareSignAwardDao;
import avatar.module.activity.sign.WelfareSignUserDao;
import avatar.util.LogUtil;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.log.CostUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**

 */
public class WelfareUtil {
    /**

     */
    public static void dealUserSignMsg(WelfareSignUserMsgEntity entity) {
        long recentlySignTime = StrUtil.checkEmpty(entity.getSignTime())?0:
                TimeUtil.strToLong(entity.getSignTime());
        
        if(!TimeUtil.getNowDay().equals(TimeUtil.longToDay(recentlySignTime))){
            
            
            boolean updateFlag = false;
            if(entity.getContinueSignDay()>=ActivityConfigMsg.welfareSignMaxDay){
                entity.setContinueSignDay(0);
                updateFlag = true;
            }else{
                long time1 = TimeUtil.strToLong(TimeUtil.getNowDay()+" 00:00:00");
                long time2 = TimeUtil.strToLong(TimeUtil.longToDay(recentlySignTime)+" 00:00:00");
                if((time1-time2)>24*60*60*1000){
                    entity.setContinueSignDay(0);
                    updateFlag = true;
                }
            }
            
            if(updateFlag){
                WelfareSignUserDao.getInstance().update(entity);
            }
        }
    }

    /**

     */
    public static WelfareSignUserMsgEntity initWelfareSignUserMsgEntity(int userId) {
        WelfareSignUserMsgEntity entity = new WelfareSignUserMsgEntity();
        entity.setUserId(userId);
        entity.setSignTime("");
        entity.setContinueSignDay(0);
        entity.setSumSignDay(0);
        return entity;
    }

    /**

     */
    public static void signMsg(int userId, Map<String, Object> dataMap) {
        
        WelfareSignUserMsgEntity userMsgEntity = WelfareSignUserDao.getInstance().loadByUserId(userId);
        if(userMsgEntity!=null) {
            
            long recentlySignTime = 0;
            if (!StrUtil.checkEmpty(userMsgEntity.getSignTime())) {
                
                recentlySignTime = TimeUtil.strToLong(userMsgEntity.getSignTime());
            }
            
            boolean todaySign = false;
            if (recentlySignTime > 0 && TimeUtil.getNowDay().equals(TimeUtil.longToDay(recentlySignTime))) {
                todaySign = true;
            }
            
            int continueSignDay = todaySign ? userMsgEntity.getContinueSignDay()
                    : (userMsgEntity.getContinueSignDay() + 1);
            dataMap.put("awdAmt", loadSignDayAwardCoin());
            dataMap.put("nxTm", todaySign ?
                    (TimeUtil.getZeroLongTime(TimeUtil.getNextDay(TimeUtil.getNowTimeStr())) -
                            TimeUtil.getNowTime()) / 1000 : 0);
            dataMap.put("snTbln", loadSignList(continueSignDay, todaySign));
            dataMap.put("cnDy", userMsgEntity.getContinueSignDay());
            dataMap.put("ttDy", ActivityConfigMsg.welfareSignMaxDay);
        }else{

        }
    }

    /**

     */
    private static int loadSignDayAwardCoin(){
        int awardCoin = 0;
        List<Integer> dayList = WelfareSignAwardDao.getInstance().loadAllDay();
        if(dayList.size()>0) {
            for(int day : dayList){
                
                List<WelfareSignAwardEntity> list = loadSignDayAward(day);
                if (list != null && list.size() > 0) {
                    for (WelfareSignAwardEntity entity : list) {
                        if (entity.getAwardType() ==CommodityUtil.gold()) {
                            awardCoin += entity.getAwardNum();
                        }
                    }
                }
            }
        }
        return awardCoin;
    }

    /**

     */
    private static List<WelfareSignAwardEntity> loadSignDayAward(int day){
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = WelfareSignAwardDao.getInstance().loadAll();
        return map.getOrDefault(day, null);
    }

    /**

     */
    private static List<WelfareSignAwardMsg> loadSignList(int continueSignDay, boolean todaySign) {
        List<WelfareSignAwardMsg> retList = new ArrayList<>();
        
        List<Integer> dayList = WelfareSignAwardDao.getInstance().loadAllDay();
        if(dayList.size()>0){
            dayList.forEach(day->{
                
                List<WelfareSignAwardEntity> list = loadSignDayAward(day);
                retList.add(initWelfareSignAwardMsg(list, day, continueSignDay, todaySign));
            });
        }
        return retList;
    }

    /**

     */
    private static WelfareSignAwardMsg initWelfareSignAwardMsg(List<WelfareSignAwardEntity> list,
            int day, int continueSignDay, boolean todaySign) {
        WelfareSignAwardMsg msg = new WelfareSignAwardMsg();
        List<GeneralAwardMsg> awardList = new ArrayList<>();
        if(list.size()>0){
            list.forEach(entity-> awardList.add(AwardUtil.initGeneralAwardMsg(entity.getAwardType(),
                    entity.getAwardImgId(), entity.getAwardNum())));
        }
        int isSign = YesOrNoEnum.NO.getCode();
        if(day<continueSignDay || (day==continueSignDay && todaySign)) {
            isSign = YesOrNoEnum.YES.getCode();
        }
        msg.setSnFlag(isSign);
        msg.setEarnArr(awardList);
        return msg;
    }

    /**

     */
    public static int receiveSignBonus(int userId, List<GeneralAwardMsg> awardList) {
        int status = ClientCode.SUCCESS.getCode();
        
        WelfareSignUserMsgEntity userMsgEntity = WelfareSignUserDao.getInstance().loadByUserId(userId);
        if(userMsgEntity!=null) {
            
            long recentlySignTime = 0;
            if (!StrUtil.checkEmpty(userMsgEntity.getSignTime())) {
                
                recentlySignTime = TimeUtil.strToLong(userMsgEntity.getSignTime());
            }
            
            boolean todaySign = false;
            if (recentlySignTime > 0 && TimeUtil.getNowDay().equals(TimeUtil.longToDay(recentlySignTime))) {
                todaySign = true;
            }
            
            int continueSignDay = todaySign ? userMsgEntity.getContinueSignDay()
                    : (userMsgEntity.getContinueSignDay() + 1);
            if(!todaySign){
                
                boolean flag = updateSignMsg(userMsgEntity);
                if(flag) {
                    
                    dealSignBonus(userId, continueSignDay, awardList);
                }else {

                    status = ClientCode.FAIL.getCode();
                }
            }else{
                status = ClientCode.NO_BONUS_TIME.getCode();
            }
        }else{

            status = ClientCode.FAIL.getCode();
        }
        return status;
    }

    /**

     */
    private static void dealSignBonus(int userId, int day, List<GeneralAwardMsg> awardList) {
        
        List<WelfareSignAwardEntity> list = loadSignDayAward(day);
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                int commodityType = entity.getAwardId();
                int awardNum = entity.getAwardNum();
                if(CommodityUtil.normalFlag(commodityType)){
                    
                    CostUtil.addWelfareSignCommodity(userId, commodityType, awardNum);
                }
                
                awardList.add(AwardUtil.initGeneralAwardMsg(commodityType, entity.getAwardImgId(), awardNum));
            });
        }
    }

    /**

     */
    private static boolean updateSignMsg(WelfareSignUserMsgEntity entity) {
        entity.setSignTime(TimeUtil.getNowTimeStr());
        entity.setContinueSignDay(entity.getContinueSignDay()+1);
        entity.setSumSignDay(entity.getSumSignDay()+1);
        
        return WelfareSignUserDao.getInstance().update(entity);
    }

    /**

     */
    public static void resetUserSignMsg(int userId, int continueDay) {
        
        WelfareSignUserMsgEntity entity = WelfareSignUserDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            if(continueDay>0){
                if(continueDay>ActivityConfigMsg.welfareSignMaxDay){
                    continueDay = ActivityConfigMsg.welfareSignMaxDay;
                }
                entity.setSignTime(TimeUtil.getBeforeNHour(TimeUtil.getNowTimeStr(), 24));
            }else{
                entity.setSignTime("");
                continueDay = 0;
            }
            entity.setContinueSignDay(continueDay);
            
            WelfareSignUserDao.getInstance().update(entity);
        }
    }


}
