package avatar.util.activity;

import avatar.data.activity.dragonTrain.DragonTrainAwardIndexMsg;
import avatar.data.activity.dragonTrain.DragonTrainAwardMsg;
import avatar.data.activity.dragonTrain.DragonTrainAwardPushMsg;
import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.activity.dragonTrain.info.DragonTrainConfigMsgEntity;
import avatar.entity.activity.dragonTrain.info.DragonTrainWheelIconMsgEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainBallUserGetHistoryEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainTriggerAwardMsgEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainUserTriggerMsgEntity;
import avatar.global.code.basicConfig.ActivityConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.award.EnergyExchangeGetTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.activity.dragonTrain.info.DragonTrainConfigMsgDao;
import avatar.module.activity.dragonTrain.info.DragonTrainWheelAwardIconMsgDao;
import avatar.module.activity.dragonTrain.info.DragonTrainWheelIconMsgDao;
import avatar.module.activity.dragonTrain.user.DragonTrainBallUserGetHistoryDao;
import avatar.module.activity.dragonTrain.user.DragonTrainUserMsgDao;
import avatar.module.activity.dragonTrain.user.DragonTrainUserTriggerAwardMsgDao;
import avatar.module.activity.dragonTrain.user.DragonTrainUserTriggerMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.AwardUtil;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.basic.general.ImgUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserNoticePushUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class DragonTrainUtil {
    /**

     */
    public static DragonTrainUserMsgEntity initDragonTrainUserMsgEntity(int userId) {
        DragonTrainUserMsgEntity entity = new DragonTrainUserMsgEntity();
        entity.setUserId(userId);
        entity.setDragonNum(0);
        entity.setTriggerTime(0);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }


    /**

     */
    public static void addUserDragon(int userId, int productId){
        
        DragonTrainUserMsgEntity entity = DragonTrainUserMsgDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            JSONObject dataJson = new JSONObject();
            int currentDragonNum = entity.getDragonNum()+1;
            
            boolean isTrigger = updateDragonTrainUserMsg(entity);
            
            int awardCoin = loadDragonBounsAwardCoin(userId);
            if(awardCoin>0) {
                
                UserCostLogUtil.addDragonAward(userId, productId, awardCoin);
            }
            
            DragonTrainBallUserGetHistoryDao.getInstance().insert(initDragonTrainBallUserGetHistoryEntity(
                    userId, productId, currentDragonNum, awardCoin,
                    isTrigger? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode()));
            List<GeneralAwardMsg> dragonAwardList = new ArrayList<>();
            dataJson.put("cnbAmt", currentDragonNum);
            
            UserAttributeUtil.productAwardEnergyDeal(userId, productId, EnergyExchangeGetTypeEnum.DRAGON_BALL.getCode(),
                    dragonAwardList);
            
            addAwardCoin(awardCoin, dragonAwardList);
            dataJson.put("awdTbln", dragonAwardList);
            if(isTrigger) {
                
                
                List<DragonTrainAwardMsg> awardList = loadWheelAwardMsg(userId, productId);
                
                addTriggerMsg(userId, productId, awardList);
                
                addUserTrainAwardList(userId, productId, awardList);
                
                fillDragonTrainAwardMsg(dataJson, awardList);
            }
            
            UserNoticePushUtil.pushDragonAwardMsg(userId, dataJson);
        }else{

        }
    }

    /**

     */
    private static void addAwardCoin(int awardCoin, List<GeneralAwardMsg> awardList) {
        
        DragonTrainConfigMsgEntity entity = DragonTrainConfigMsgDao.getInstance().loadMsg();
        awardList.add(AwardUtil.initGeneralAwardMsg(CommodityTypeEnum.GOLD_COIN.getCode(),
                0, entity.getAwardImgId(), awardCoin));
    }

    /**

     */
    private static boolean updateDragonTrainUserMsg(DragonTrainUserMsgEntity entity) {
        boolean isTrigger = false;
        int triggerNum = ActivityConfigMsg.triggerBallNum;
        int dragonNum = entity.getDragonNum();
        if(dragonNum+1>=triggerNum){
            
            entity.setDragonNum(dragonNum+1-triggerNum);
            entity.setTriggerTime(entity.getTriggerTime()+1);
            isTrigger = true;
        }else{
            
            entity.setDragonNum(dragonNum+1);
        }
        boolean flag = DragonTrainUserMsgDao.getInstance().update(entity);
        if(!flag){

        }
        return isTrigger;
    }

    /**

     */
    private static int loadDragonBounsAwardCoin(int userId) {
        int awardCoin = 0;
        
        DragonTrainConfigMsgEntity entity = DragonTrainConfigMsgDao.getInstance().loadMsg();
        if(entity!=null){
            awardCoin = StrUtil.loadInterValNum(entity.getDragonMinNum(), entity.getDragonMaxNum());
        }else {

        }
        return awardCoin;
    }

    /**

     */
    private static DragonTrainBallUserGetHistoryEntity initDragonTrainBallUserGetHistoryEntity(
            int userId, int productId, int currentNum, int awardCoin, int isTrigger){
        DragonTrainBallUserGetHistoryEntity entity = new DragonTrainBallUserGetHistoryEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setCurrentNum(currentNum);
        entity.setAwardCoin(awardCoin);
        entity.setIsTrigger(isTrigger);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    private static List<DragonTrainAwardMsg> loadWheelAwardMsg(int userId, int productId){
        List<DragonTrainAwardMsg> awardList = new ArrayList<>();
        
        List<DragonTrainWheelIconMsgEntity> iconList = DragonTrainWheelAwardIconMsgDao.getInstance().loadMsg();
        
        DragonTrainConfigMsgEntity configMsgEntity = DragonTrainConfigMsgDao.getInstance().loadMsg();
        if(configMsgEntity!=null && iconList.size()>0){
            int awardNum = 0;
            fillAwardMsg(awardNum, iconList, configMsgEntity, awardList);
            
            awardList = new ArrayList<>(dealRetAwardMsg(awardList));
        }else{


        }
        return awardList;
    }

    /**

     */
    private static void fillAwardMsg(int awardNum, List<DragonTrainWheelIconMsgEntity> iconList,
            DragonTrainConfigMsgEntity configMsgEntity, List<DragonTrainAwardMsg> awardList) {

        boolean repeatFlag = configMsgEntity.getIsRepeatAward()==YesOrNoEnum.YES.getCode();
        boolean againAwardFlag = canAwardAgain(configMsgEntity, awardList);
        
        List<DragonTrainWheelIconMsgEntity> selectIconList = dealSelectIconList(iconList, awardList, repeatFlag,
                againAwardFlag);
        
        boolean allAward = isAllAward(selectIconList);
        if(!allAward) {
            
            addAwardMsg(awardNum, selectIconList, awardList);
            if(awardList.get(awardList.size()-1).getWheelIconMsg().
                    getCommodityType()!= CommodityTypeEnum.AGAIN.getCode()) {
                awardNum += 1;
            }
        }
        if(!allAward && awardNum<configMsgEntity.getAwardNum()){
            fillAwardMsg(awardNum, iconList, configMsgEntity, awardList);
        }
    }

    /**

     */
    private static boolean canAwardAgain(DragonTrainConfigMsgEntity configMsgEntity, List<DragonTrainAwardMsg> awardList) {
        int againMaxTime = configMsgEntity.getAgainTime();
        int againAwardTime = 0;
        for(DragonTrainAwardMsg msg : awardList){
            if(msg.getWheelIconMsg().getCommodityType()==CommodityTypeEnum.AGAIN.getCode()){
                againAwardTime += 1;
            }
        }
        return againMaxTime>againAwardTime;
    }

    /**

     */
    private static List<DragonTrainWheelIconMsgEntity> dealSelectIconList(
            List<DragonTrainWheelIconMsgEntity> iconList, List<DragonTrainAwardMsg> awardList,
            boolean repeatFlag, boolean againAwardFlag) {
        List<DragonTrainWheelIconMsgEntity> retList = new ArrayList<>();
        if(awardList.size()==0){
            
            retList = new ArrayList<>(iconList);
        }else{
            
            for(DragonTrainWheelIconMsgEntity entity : iconList){
                if(isNoSelectAward(entity, awardList, repeatFlag, againAwardFlag)){
                    retList.add(entity);
                }
            }
        }
        return retList;
    }

    /**

     */
    private static boolean isNoSelectAward(DragonTrainWheelIconMsgEntity entity,
            List<DragonTrainAwardMsg> awardList, boolean repeatFlag, boolean againAwardFlag) {
        boolean flag = true;
        if(entity.getCommodityType()==CommodityTypeEnum.AGAIN.getCode() && !againAwardFlag){
            
            flag = false;
        }else {
            for (DragonTrainAwardMsg msg : awardList) {
                DragonTrainWheelIconMsgEntity wheelIconMsg = msg.getWheelIconMsg();
                if (entity.getId() == msg.getWheelIconMsg().getId() || (
                        wheelIconMsg.getCommodityType()!=CommodityTypeEnum.AGAIN.getCode() &&
                                wheelIconMsg.getCommodityType() == entity.getCommodityType() &&
                                wheelIconMsg.getGiftId() == entity.getGiftId() && !repeatFlag)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**

     */
    private static boolean isAllAward(List<DragonTrainWheelIconMsgEntity> selectIconList) {
        boolean flag = true;
        if(selectIconList.size()>0) {
            for (DragonTrainWheelIconMsgEntity entity : selectIconList) {
                if (entity.getCommodityType() != CommodityTypeEnum.AGAIN.getCode()) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**

     */
    private static void addAwardMsg(int awardNum, List<DragonTrainWheelIconMsgEntity> selectIconList,
            List<DragonTrainAwardMsg> awardList) {
        DragonTrainWheelIconMsgEntity firstIcon = selectIconList.get(0);
        DragonTrainWheelIconMsgEntity awardIcon = null;
        Collections.shuffle(selectIconList);
        
        for(DragonTrainWheelIconMsgEntity iconMsg : selectIconList){
            if(isIconAward(iconMsg)){
                awardIcon = iconMsg;
                break;
            }
        }
        
        if(awardIcon==null){
            awardIcon = firstIcon;
        }
        DragonTrainAwardMsg awardMsg = initDragonTrainAwardMsg(awardIcon);
        awardList.add(awardMsg);

                CommodityUtil.loadAwardName(awardIcon.getCommodityType(), awardIcon.getGiftId()),
                awardMsg.getResultAwardNum());
    }

    /**

     */
    private static boolean isIconAward(DragonTrainWheelIconMsgEntity iconMsg) {
        boolean flag = false;
        int awardPro = iconMsg.getAwardProbability();
        int totalPro = iconMsg.getTotalProbability();
        if(totalPro>=awardPro && totalPro>0 && awardPro>0){
            flag = StrUtil.isAward(awardPro, totalPro);
        }
        return flag;
    }

    /**

     */
    private static DragonTrainAwardMsg initDragonTrainAwardMsg(DragonTrainWheelIconMsgEntity awardIcon) {
        DragonTrainAwardMsg msg = new DragonTrainAwardMsg();
        msg.setResultAwardNum(StrUtil.loadInterValNum(awardIcon.getAwardMinNum(), awardIcon.getAwardMaxNum()));
        msg.setWheelIconMsg(awardIcon);
        return msg;
    }

    /**

     */
    private static List<DragonTrainAwardMsg> dealRetAwardMsg(List<DragonTrainAwardMsg> awardList) {
        List<DragonTrainAwardMsg> retList = new ArrayList<>();
        int againNum = 0;
        for(int i=awardList.size()-1;i>=0;i--){
            if(awardList.get(i).getWheelIconMsg().getCommodityType()==CommodityTypeEnum.AGAIN.getCode()){
                againNum += 1;
            }else{
                break;
            }
        }
        if(againNum>0) {
            if (awardList.size() > againNum) {
                for (int i = 0; i < (awardList.size() - againNum); i++){
                    retList.add(awardList.get(i));
                }
            }
        }else{
            retList = new ArrayList<>(awardList);
        }
        return retList;
    }

    /**

     */
    private static void addTriggerMsg(int userId, int productId, List<DragonTrainAwardMsg> awardList) {
        DragonTrainUserTriggerMsgEntity triggerMsgEntity = DragonTrainUserTriggerMsgDao.getInstance()
                .insert(initDragonTrainUserTriggerMsgEntity(userId, productId));
        if(triggerMsgEntity!=null){
            int triggerId = triggerMsgEntity.getId();
            if(awardList.size()>0){
                boolean flag = DragonTrainUserTriggerAwardMsgDao.getInstance().insert(
                        initDragonTrainTriggerAwardMsgEntity(triggerId, awardList));
                if(!flag){

                }
            }
        }else{

        }
    }

    /**

     */
    private static DragonTrainUserTriggerMsgEntity initDragonTrainUserTriggerMsgEntity(int userId, int productId){
        DragonTrainUserTriggerMsgEntity entity = new DragonTrainUserTriggerMsgEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    private static List<DragonTrainTriggerAwardMsgEntity> initDragonTrainTriggerAwardMsgEntity(
            int triggerId, List<DragonTrainAwardMsg> awardList){
        List<DragonTrainTriggerAwardMsgEntity> retList = new ArrayList<>();
        awardList.forEach(msg->{
            DragonTrainTriggerAwardMsgEntity entity = new DragonTrainTriggerAwardMsgEntity();
            DragonTrainWheelIconMsgEntity wheelIconMsg = msg.getWheelIconMsg();
            entity.setTriggerId(triggerId);
            entity.setCommodityType(wheelIconMsg.getCommodityType());
            entity.setGiftId(wheelIconMsg.getGiftId());
            entity.setResultAwardNum(msg.getResultAwardNum());
            entity.setAwardMinNum(wheelIconMsg.getAwardMinNum());
            entity.setAwardMaxNum(wheelIconMsg.getAwardMaxNum());
            entity.setAwardProbability(wheelIconMsg.getAwardProbability());
            entity.setTotalProbability(wheelIconMsg.getTotalProbability());
            retList.add(entity);
        });
        return retList;
    }

    /**

     */
    private static void addUserTrainAwardList(int userId, int productId, List<DragonTrainAwardMsg> awardList) {
        if(awardList.size()>0){
            awardList.forEach(awardMsg-> addUserTrainAwardMsg(userId, productId, awardMsg));
        }
    }

    /**

     */
    private static void addUserTrainAwardMsg(int userId, int productId, DragonTrainAwardMsg awardMsg) {
        int awardType = awardMsg.getWheelIconMsg().getCommodityType();
        int awardId = awardMsg.getWheelIconMsg().getGiftId();
        int awardNum = awardMsg.getResultAwardNum();
        if(awardNum>0) {
            if (CommodityUtil.normalFlag(awardType)) {
                
                UserCostLogUtil.dragonTrainAward(userId, productId, awardNum, awardType);
            } else if (awardType==CommodityTypeEnum.PROPERTY.getCode()){
                
                UserCostLogUtil.dragonTrainProperty(userId, awardId, awardNum);
            }
        }
    }

    /**

     */
    private static void fillDragonTrainAwardMsg(JSONObject dataJson, List<DragonTrainAwardMsg> awardList) {
        DragonTrainAwardPushMsg dragonTrainAwardMsg = new DragonTrainAwardPushMsg();
        dragonTrainAwardMsg.setIcTbln(dragonTrainIconList());
        dragonTrainAwardMsg.setIcAwdTbln(dragonTrainIconAwardIndexList(awardList));
        dragonTrainAwardMsg.setAwdTbln(initPushAwardList(awardList));
        dataJson.put("dgTnTbln", dragonTrainAwardMsg);
    }

    /**

     */
    private static List<String> dragonTrainIconList() {
        List<String> retList = new ArrayList<>();
        
        List<DragonTrainWheelIconMsgEntity> list = DragonTrainWheelIconMsgDao.getInstance().loadMsg();
        if(list.size()>0){
            list.forEach(entity->{
                String awardImg = ImgUtil.loadAwardImg(entity.getAwardImgId());
                if(!StrUtil.checkEmpty(awardImg)){
                    retList.add(MediaUtil.getMediaUrl(awardImg));
                }
            });
        }
        return retList;
    }

    /**

     */
    private static List<DragonTrainAwardIndexMsg> dragonTrainIconAwardIndexList(List<DragonTrainAwardMsg> awardList) {
        List<DragonTrainAwardIndexMsg> retList = new ArrayList<>();
        awardList.forEach(awardMsg->{
            DragonTrainAwardIndexMsg msg = new DragonTrainAwardIndexMsg();
            msg.setCmdTp(awardMsg.getWheelIconMsg().getCommodityType());
            msg.setAwdInx(awardMsg.getWheelIconMsg().getId()-1);
            retList.add(msg);
        });
        return retList;
    }

    /**

     */
    private static List<GeneralAwardMsg> initPushAwardList(List<DragonTrainAwardMsg> awardList) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        awardList.forEach(awardMsg->{
            int awardType = awardMsg.getWheelIconMsg().getCommodityType();
            int awardId = awardMsg.getWheelIconMsg().getGiftId();
            int imgId = awardMsg.getWheelIconMsg().getAwardImgId();
            if(awardType!=CommodityTypeEnum.AGAIN.getCode()) {
                
                
                boolean addFlag = false;
                if(retList.size()>0){
                    for(GeneralAwardMsg msg : retList){
                        if (msg.getCmdTp() == awardType && msg.getCmdId() == awardId) {
                            msg.setAwdAmt(msg.getAwdAmt() + awardMsg.getResultAwardNum());
                            addFlag = true;
                            break;
                        }
                    }
                }
                if(!addFlag){
                    retList.add(AwardUtil.initGeneralAwardMsg(awardType, awardId, imgId, awardMsg.getResultAwardNum()));
                }
            }
        });
        return retList;
    }

}
