package avatar.util.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.innoMsg.*;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.basic.errrorCode.InnoClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.info.UserInfoDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.system.TimeUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class InnoParamsUtil {
    /**

     */
    public static InnoStartGameMsg initInnoStartGameMsg(int productId, String alias, int userId) {
        InnoStartGameMsg msg = new InnoStartGameMsg();
        msg.setProductId(productId);
        msg.setAlias(alias);
        msg.setUserId(userId);
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());
        msg.setRequestTime(TimeUtil.getNowTime());
        return msg;
    }

    /**

     */
    public static Map<Object, Object> initStartGameMsg(InnoStartGameMsg startGameMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("alias", startGameMsg.getAlias());
        int userId = startGameMsg.getUserId();
        paramsMap.put("userId", userId);
        paramsMap.put("userName", startGameMsg.getUserName());
        paramsMap.put("imgUrl", startGameMsg.getImgUrl());
        paramsMap.put("serverSideType", startGameMsg.getServerSideType());
        paramsMap.put("coinLevelWeight", InnoProductUtil.userCoinWeight(userId, startGameMsg.getProductId()));
        paramsMap.put("requestTime", startGameMsg.getRequestTime());
        return paramsMap;
    }

    /**

     */
    public static int loadProductOperateType(int operateType) {
        int retOperateType = -1;
        if(operateType==InnoProductOperateTypeEnum.PUSH_COIN.getCode()){
            
            retOperateType = ProductOperationEnum.PUSH_COIN.getCode();
        }else if(operateType==InnoProductOperateTypeEnum.WIPER.getCode()){
            
            retOperateType = ProductOperationEnum.ROCK.getCode();
        }else if(operateType==InnoProductOperateTypeEnum.AUTO_PUSH_COIN.getCode()){
            
            retOperateType = ProductOperationEnum.AUTO_SHOOT.getCode();
        }else if(operateType==InnoProductOperateTypeEnum.CANCEL_AUTO_PUSH_COIN.getCode()){
            
            retOperateType = ProductOperationEnum.CANCEL_AUTO_SHOOT.getCode();
        }
        return retOperateType;
    }

    /**

     */
    public static InnoProductOperateMsg initInnoProductOperateMsg(int productId, String alias, int userId,
            long onProductTime, int operateType, boolean innoFreeLink) {
        InnoProductOperateMsg msg = new InnoProductOperateMsg();
        msg.setProductId(productId);
        msg.setAlias(alias);
        msg.setUserId(userId);
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());
        msg.setRequestTime(TimeUtil.getNowTime());
        msg.setOnProductTime(onProductTime);
        msg.setInnoProductOperateType(operateType);
        msg.setProductCost(ProductUtil.productCost(productId));
        int secondType = ProductUtil.loadSecondType(productId);
        msg.setAgyptOpenBox((innoFreeLink && secondType== ProductSecondTypeEnum.AGYPT.getCode())
                ? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());
        msg.setClownCircusFerrule((innoFreeLink && secondType== ProductSecondTypeEnum.CLOWN_CIRCUS.getCode())
                ? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());
        msg.setPirateCannon((innoFreeLink && secondType== ProductSecondTypeEnum.PIRATE.getCode())
                ? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());
        msg.setCoinLevelWeight(InnoProductUtil.userCoinWeight(userId, productId));
        msg.setPayFlag(InnoNaPayUtil.isPay(userId)? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());
        return msg;
    }

    /**

     */
    public static InnoEndGameMsg initInnoEndGameMsg(int productId, String alias, int userId) {
        InnoEndGameMsg msg = new InnoEndGameMsg();
        msg.setProductId(productId);
        msg.setAlias(alias);
        msg.setUserId(userId);
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());
        msg.setRequestTime(TimeUtil.getNowTime());
        msg.setProductMulti(ProductGamingUtil.loadMultiLevel(productId));
        return msg;
    }

    /**

     */
    public static Map<Object, Object> initEndGameMsg(InnoEndGameMsg endGameMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("alias", endGameMsg.getAlias());
        paramsMap.put("userId", endGameMsg.getUserId());
        paramsMap.put("userName", endGameMsg.getUserName());
        paramsMap.put("imgUrl", endGameMsg.getImgUrl());
        paramsMap.put("serverSideType", endGameMsg.getServerSideType());
        paramsMap.put("requestTime", endGameMsg.getRequestTime());
        paramsMap.put("productMulti", endGameMsg.getProductMulti());
        return paramsMap;
    }

    /**

     */
    public static Map<Object, Object> initProductOperateMsg(InnoProductOperateMsg productOperateMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        try {
            int userId = productOperateMsg.getUserId();
            String alias = productOperateMsg.getAlias();
            paramsMap.put("alias", productOperateMsg.getAlias());
            paramsMap.put("userId", userId);
            paramsMap.put("userName", productOperateMsg.getUserName());
            paramsMap.put("imgUrl", productOperateMsg.getImgUrl());
            paramsMap.put("serverSideType", productOperateMsg.getServerSideType());
            paramsMap.put("requestTime", productOperateMsg.getRequestTime());
            paramsMap.put("innoProductOperateType", productOperateMsg.getInnoProductOperateType());
            paramsMap.put("onProductTime", productOperateMsg.getOnProductTime());
            paramsMap.put("productCost", productOperateMsg.getProductCost());
            paramsMap.put("agyptOpenBox", productOperateMsg.getAgyptOpenBox());
            paramsMap.put("clownCircusFerrule", productOperateMsg.getClownCircusFerrule());
            paramsMap.put("pirateCannon", productOperateMsg.getPirateCannon());
            if (productOperateMsg.getInnoProductOperateType() == InnoProductOperateTypeEnum.PUSH_COIN.getCode()) {
                paramsMap.put("coinLevelWeight", productOperateMsg.getCoinLevelWeight());
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return paramsMap;
    }

    /**

     */
    public static Map<Object, Object> initStartGameOccupyMsg(InnoStartGameOccupyMsg startGameOccupyMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("alias", startGameOccupyMsg.getAlias());
        paramsMap.put("requestTime", TimeUtil.getNowTime());
        paramsMap.put("onProductTime", startGameOccupyMsg.getOnProductTime());
        return paramsMap;
    }

    /**

     */
    public static Map<Object, Object> initChangeCoinWeightMsg(InnoChangeCoinWeightMsg changeCoinWeightMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("alias", changeCoinWeightMsg.getAlias());
        paramsMap.put("userId", changeCoinWeightMsg.getUserId());
        paramsMap.put("userName", changeCoinWeightMsg.getUserName());
        paramsMap.put("imgUrl", changeCoinWeightMsg.getImgUrl());
        paramsMap.put("serverSideType", changeCoinWeightMsg.getServerSideType());
        paramsMap.put("requestTime", changeCoinWeightMsg.getRequestTime());
        paramsMap.put("coinLevelWeight", changeCoinWeightMsg.getCoinLevelWeight());
        return paramsMap;
    }

    /**

     */
    public static InnoChangeCoinWeightMsg initInnoChangeCoinWeightMsg(int productId, String alias, int userId,
            int coinWeight) {
        InnoChangeCoinWeightMsg msg = new InnoChangeCoinWeightMsg();
        msg.setProductId(productId);
        msg.setAlias(alias);
        msg.setUserId(userId);
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());
        msg.setRequestTime(TimeUtil.getNowTime());
        msg.setCoinLevelWeight(coinWeight);
        return msg;
    }

    /**

     */
    public static int loadClientCode(int status) {
        if(status==InnoClientCode.PRODUCT_EXCEPTION.getCode()){
            
            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        }else if(status==InnoClientCode.PRODUCT_OCCUPY.getCode()){
            
            status = ClientCode.PRODUCT_OCCUPY.getCode();
        }
        return status;
    }

    /**

     */
    public static InnoReceiveStartGameMsg startGameMsg(Map<String, Object> jsonMap) {
        InnoReceiveStartGameMsg msg = new InnoReceiveStartGameMsg();
        try{
            msg.setStatus((int)jsonMap.get("status"));
            msg.setAlias(jsonMap.get("alias").toString());
            msg.setUserId((int)jsonMap.get("userId"));
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;

        }
        return msg;
    }

    /**

     */
    public static InnoReceiveProductOperateMsg productOperateMsg(Map<String, Object> jsonMap) {
        InnoReceiveProductOperateMsg msg = new InnoReceiveProductOperateMsg();
        try{
            msg.setStatus((int)jsonMap.get("status"));
            msg.setAlias(jsonMap.get("alias").toString());
            msg.setUserId((int)jsonMap.get("userId"));
            msg.setUserName((String)jsonMap.get("userName"));
            msg.setImgUrl((String)jsonMap.get("imgUrl"));
            msg.setServerSideType((int)jsonMap.get("serverSideType"));
            msg.setInnoProductOperateType((int)jsonMap.get("innoProductOperateType"));
            msg.setOnProductTime(Long.parseLong(jsonMap.get("onProductTime").toString()));
            
            if(jsonMap.get("awardType")!=null){
                msg.setAwardType((int)jsonMap.get("awardType"));
            }
            
            if(jsonMap.get("breakType")!=null){
                msg.setBreakType((int)jsonMap.get("breakType"));
            }
            
            if(jsonMap.get("coinNum")!=null){
                msg.setCoinNum((int)jsonMap.get("coinNum"));
            }
            
            if(jsonMap.get("awardNum")!=null){
                msg.setAwardNum((int)jsonMap.get("awardNum"));
            }
            
            if(jsonMap.get("isStart")!=null){
                msg.setIsStart((int)jsonMap.get("isStart"));
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;

        }
        return msg;
    }

    /**

     */
    public static InnoStartGameOccupyMsg startGameOccupyMsg(Map<String, Object> jsonMap) {
        InnoStartGameOccupyMsg msg = new InnoStartGameOccupyMsg();
        try{
            msg.setAlias(jsonMap.get("alias").toString());
            msg.setUserId((int)jsonMap.get("userId"));
            msg.setUserName((String)jsonMap.get("userName"));
            msg.setImgUrl((String)jsonMap.get("imgUrl"));
            msg.setServerSideType((int)jsonMap.get("serverSideType"));
            msg.setOnProductTime(Long.parseLong(jsonMap.get("onProductTime").toString()));
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;

        }
        return msg;
    }

    /**

     */
    public static InnoProductMsg initInnoProductMsg(Map<String, Object> jsonMap) {
        InnoProductMsg msg = new InnoProductMsg();
        msg.setAlias((String) jsonMap.get("alias"));
        msg.setUserId((int) jsonMap.get("userId"));
        msg.setUserName((String) jsonMap.get("userName"));
        msg.setImgUrl((String) jsonMap.get("imgUrl"));
        msg.setServerSideType((int) jsonMap.get("serverSideType"));
        msg.setProductMulti(jsonMap.containsKey("productMulti")?
                (int)jsonMap.get("productMulti"):0);
        return msg;
    }

    /**

     */
    public static ResponseGeneralMsg initResponseGeneralMsg(String alias, int userId) {
        ResponseGeneralMsg msg = new ResponseGeneralMsg();
        msg.setAlias(alias);
        msg.setUserId(userId);
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());
        return msg;
    }

    /**

     */
    public static InnoGetCoinMsg initInnoGetCoinMsg(JSONObject jsonObject) {
        InnoGetCoinMsg msg = new InnoGetCoinMsg();
        msg.setAlias(jsonObject.getString("alias"));
        msg.setUserId(jsonObject.getInteger("userId"));
        msg.setUserName(jsonObject.getString("userName"));
        msg.setImgUrl(jsonObject.getString("imgUrl"));
        msg.setServerSideType(jsonObject.getInteger("serverSideType"));
        msg.setRetNum(jsonObject.getInteger("retNum"));
        return msg;
    }

    /**

     */
    public static InnoAwardLockMsg initInnoAwardLockMsg(Map<String, Object> jsonMap) {
        InnoAwardLockMsg msg = new InnoAwardLockMsg();
        msg.setAlias(jsonMap.get("alias").toString());
        msg.setUserId((int)jsonMap.get("userId"));
        msg.setServerSideType((int)jsonMap.get("serverSideType"));
        msg.setIsStart((int)jsonMap.get("isStart"));
        return msg;
    }

    /**

     */
    public static Map<Object, Object> initAutoPushCoinMsg(InnoAutoPushCoinMsg autoPushCoinMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("alias", autoPushCoinMsg.getAlias());
        paramsMap.put("userId", autoPushCoinMsg.getUserId());
        paramsMap.put("userName", autoPushCoinMsg.getUserName());
        paramsMap.put("imgUrl", autoPushCoinMsg.getImgUrl());
        paramsMap.put("serverSideType", autoPushCoinMsg.getServerSideType());
        paramsMap.put("requestTime", autoPushCoinMsg.getRequestTime());
        paramsMap.put("isStart", autoPushCoinMsg.getIsStart());
        return paramsMap;
    }

    /**

     */
    public static InnoAutoPushCoinMsg initInnoAutoPushCoinMsg(int productId, String alias, int userId, int isStart) {
        InnoAutoPushCoinMsg msg = new InnoAutoPushCoinMsg();
        msg.setProductId(productId);
        msg.setAlias(alias);
        msg.setUserId(userId);
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());
        msg.setRequestTime(TimeUtil.getNowTime());
        msg.setIsStart(isStart);
        return msg;
    }

    /**

     */
    public static InnoSettlementWindowMsg initInnoSettlementWindowMsg(Map<String, Object> jsonMap) {
        InnoSettlementWindowMsg msg = new InnoSettlementWindowMsg();
        msg.setAlias(jsonMap.get("alias").toString());
        msg.setUserId((int)jsonMap.get("userId"));
        msg.setServerSideType((int)jsonMap.get("serverSideType"));
        return msg;
    }

    /**

     */
    public static InnoAwardScoreMultiMsg initInnoAwardScoreMultiMsg(Map<String, Object> jsonMap) {
        InnoAwardScoreMultiMsg msg = new InnoAwardScoreMultiMsg();
        msg.setAlias(jsonMap.get("alias").toString());
        msg.setUserId((int)jsonMap.get("userId"));
        msg.setServerSideType((int)jsonMap.get("serverSideType"));
        msg.setAwardMulti((int)jsonMap.get("awardMulti"));
        return msg;
    }

    /**

     */
    public static InnoVoiceNoticeMsg initInnoVoiceNoticeMsg(Map<String, Object> jsonMap) {
        InnoVoiceNoticeMsg msg = new InnoVoiceNoticeMsg();
        msg.setAlias(jsonMap.get("alias").toString());
        msg.setUserId((int)jsonMap.get("userId"));
        msg.setServerSideType((int)jsonMap.get("serverSideType"));
        msg.setVoiceType((int)jsonMap.get("voiceType"));
        msg.setIsStart((int)jsonMap.get("isStart"));
        msg.setIsEndSwitch((int)jsonMap.get("isStart"));
        return msg;
    }

    /**

     */
    public static String loadStringParams(Map<String, Object> jsonMap, String paramsName) {
        return jsonMap.containsKey(paramsName)? jsonMap.get(paramsName).toString():"";
    }

    /**

     */
    public static int intParams(Map<String, Object> jsonMap, String paramsName) {
        return jsonMap.containsKey(paramsName)?Integer.parseInt(jsonMap.get(paramsName).toString()):0;
    }

    /**

     */
    public static JSONObject loadClientDirectMsg(JSONObject jsonMap) {
        JSONObject retMsg = new JSONObject();
        if(jsonMap.containsKey("data")){
            Map<String, Object> dataMap = (Map<String, Object>)jsonMap.get("data");
            if(dataMap!=null && dataMap.containsKey("detailMsg")){
                retMsg = (JSONObject) dataMap.get("detailMsg");
            }
        }
        return retMsg;
    }

    /**

     */
    public static InnoProductAwardMsg initInnoProductAwardMsg(JSONObject jsonObject) {
        InnoProductAwardMsg msg = new InnoProductAwardMsg();
        msg.setAlias(jsonObject.getString("alias"));
        msg.setUserId(jsonObject.getInteger("userId"));
        msg.setUserName(jsonObject.getString("userName"));
        msg.setImgUrl(jsonObject.getString("imgUrl"));
        msg.setServerSideType(jsonObject.getInteger("serverSideType"));
        msg.setAwardType(jsonObject.getInteger("awardType"));
        return msg;
    }

    /**

     */
    public static InnoDragonMsg initInnoDragonMsg(JSONObject jsonObject) {
        InnoDragonMsg msg = new InnoDragonMsg();
        msg.setAlias(jsonObject.getString("alias"));
        return msg;
    }

    /**

     */
    public static Map<Object, Object> initProductOperateMsg(int productId, ProductInfoEntity productInfoEntity,
            int userId, int awardType) {
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        InnoProductOperateMsg productOperateMsg = InnoParamsUtil.initInnoProductOperateMsg(productId, productInfoEntity.getAlias(), userId,
                productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.PUSH_COIN.getCode(), false);
        Map<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("alias", productOperateMsg.getAlias());
        paramsMap.put("userId", userId);
        paramsMap.put("userName", productOperateMsg.getUserName());
        paramsMap.put("imgUrl", productOperateMsg.getImgUrl());
        paramsMap.put("serverSideType", productOperateMsg.getServerSideType());
        paramsMap.put("requestTime", productOperateMsg.getRequestTime());
        paramsMap.put("innoProductOperateType", productOperateMsg.getInnoProductOperateType());
        paramsMap.put("onProductTime", productOperateMsg.getOnProductTime());
        paramsMap.put("productCost", productOperateMsg.getProductCost());
        paramsMap.put("productAwardType",awardType);
        return paramsMap;
    }
}
