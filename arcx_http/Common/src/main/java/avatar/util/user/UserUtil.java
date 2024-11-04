package avatar.util.user;

import avatar.data.crossServer.CrossServerSearchProductPrizeMsg;
import avatar.data.user.info.UserGrandPrizeSearchMsg;
import avatar.entity.basic.img.ImgProductGrandPrizeMsgEntity;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.info.UserMacMsgEntity;
import avatar.entity.user.info.UserRegisterIpEntity;
import avatar.entity.user.thirdpart.UserThirdPartUidMsgEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.enumMsg.user.SexEnum;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.basic.img.ImgProductGrandPrizeMsgDao;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.info.*;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.system.MD5Util;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**

 */
public class UserUtil {
    /**

     */
    public static int loadUserStatus(int userId) {
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null?-1:userInfoEntity.getStatus();
    }

    /**

     */
    public static void updateIpMsg(int userId, String userIp) {
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        if(!userInfoEntity.getIp().equals(userIp)){
            userInfoEntity.setIp(userIp);
            UserInfoDao.getInstance().update(userInfoEntity);
        }
    }

    /**

     */
    public static int tokenLoginRet(int userId, Map<String, Object> dataMap) {
        int status = ClientCode.SUCCESS.getCode();
        boolean updateFlag = updateUserTokenMsg(userId, dataMap);
        if(!updateFlag){
            status = ClientCode.FAIL.getCode();
        }
        return status;
    }

    /**

     */
    private static boolean updateUserTokenMsg(int userId, Map<String, Object> dataMap) {
        UserTokenMsgEntity tokenMsgEntity = UserTokenMsgDao.getInstance().update(userId);
        if(tokenMsgEntity!=null){
            dataMap.put("aesTkn", tokenMsgEntity.getAccessToken());
            dataMap.put("aesOt", tokenMsgEntity.getAccessOutTime());
            dataMap.put("refTkn", tokenMsgEntity.getRefreshToken());
            dataMap.put("refOt", tokenMsgEntity.getRefreshOutTime());
            return true;
        }else {
            return false;
        }
    }

    /**

     */
    public static UserMacMsgEntity initUserMacMsgEntity(int userId, String mac, int isRegister) {
        UserMacMsgEntity entity = new UserMacMsgEntity();
        entity.setUserId(userId);
        entity.setMac(mac);
        entity.setIsRegister(isRegister);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static UserInfoEntity initUserInfoEntity(String nickName, String imgUrl, String userIp, String email,
            int loginWayType, int mobilePlatformType) {
        UserInfoEntity entity = new UserInfoEntity();
        entity.setNickName(nickName);
        entity.setImgUrl(imgUrl);
        entity.setIp(userIp);
        entity.setLoginWay(loginWayType);
        entity.setMobilePlatformType(mobilePlatformType);
        entity.setSex(SexEnum.MALE.getCode());
        entity.setEmail(email);
        entity.setPassword("");
        entity.setStatus(UserStatusEnum.NORMAL.getCode());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }

    /**

     */
    public static String loadDefaultImg(){
        List<String> list = UserDefaultHeadImgDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            Collections.shuffle(list);
            return list.get(0);
        }else{

            return "";
        }
    }

    /**

     */
    public static UserRegisterIpEntity initUserRegisterIpEntity(int userId, String userIp, Map<String, Object> paramsMap) {
        int loginWayType = ParamsUtil.loginWayType(paramsMap);
        int mobilePlatform = ParamsUtil.loadMobilePlatform(paramsMap);
        String versionCode = ParamsUtil.versionCode(paramsMap);
        UserRegisterIpEntity entity = new UserRegisterIpEntity();
        entity.setUserId(userId);
        entity.setIp(userIp);
        entity.setRegisterVersion(versionCode);
        entity.setLoginWayType(loginWayType);
        entity.setLoginPlatform(mobilePlatform);
        entity.setLastIp(userIp);
        entity.setLastLoginWay(loginWayType);
        entity.setLastLoginPlatform(mobilePlatform);
        entity.setLastVersion(versionCode);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void addRegisterUserMacMsg(int userId, String mac) {
        UserMacMsgEntity entity = initUserMacMsgEntity(userId, mac, YesOrNoEnum.YES.getCode());
        UserMacMsgDao.getInstance().insert(entity);
    }

    /**

     */
    public static void addUserThirdPartMsg(int userId, String thirdPartUid) {
        UserThirdPartUidMsgEntity entity = initUserThirdPartUidMsgEntity(userId, thirdPartUid);
        UserThirdPartUidMsgDao.getInstance().insert(entity);
    }

    /**

     */
    private static UserThirdPartUidMsgEntity initUserThirdPartUidMsgEntity(int userId, String thirdPartUid) {
        UserThirdPartUidMsgEntity entity = new UserThirdPartUidMsgEntity();
        entity.setUserId(userId);
        entity.setUid(thirdPartUid);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static String loadUserIp(int userId){
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getIp();
    }

    /**

     */
    public static void updateUserLoginMsg(int userId, Map<String, Object> paramsMap) {
        int loginWayType = ParamsUtil.loginWayType(paramsMap);
        int mobilePlatform = ParamsUtil.loadMobilePlatform(paramsMap);
        String versionCode = ParamsUtil.versionCode(paramsMap);
        String userIp = ParamsUtil.ip(paramsMap);
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(entity.getMobilePlatformType()!=mobilePlatform || entity.getLoginWay()!=loginWayType){
            entity.setMobilePlatformType(mobilePlatform);
            entity.setLoginWay(loginWayType);
            UserInfoDao.getInstance().update(entity);
        }
        
        UserRegisterIpEntity registerIpEntity = UserRegisterIpDao.getInstance().loadDbByUserId(userId);
        if(registerIpEntity!=null){
            registerIpEntity.setLastIp(userIp);
            registerIpEntity.setLastLoginWay(loginWayType);
            registerIpEntity.setLastLoginPlatform(mobilePlatform);
            registerIpEntity.setLastVersion(versionCode);
            UserRegisterIpDao.getInstance().update(registerIpEntity);
        }else{
            
            UserRegisterIpDao.getInstance().insert(UserUtil.initUserRegisterIpEntity(userId, userIp, paramsMap));
        }
    }

    /**

     */
    public static List<UserGrandPrizeSearchMsg> initProductGrandPrize(CrossServerSearchProductPrizeMsg productPrizeMsg) {
        List<UserGrandPrizeSearchMsg> retList = new ArrayList<>();
        if(productPrizeMsg!=null) {
            
            List<ImgProductGrandPrizeMsgEntity> imgList = ImgProductGrandPrizeMsgDao.getInstance().loadAll();
            if (imgList.size() > 0) {
                imgList.forEach(imgEntity -> {
                    int prizeNum = dealCrossPrizeNum(productPrizeMsg, imgEntity.getEnConciseName());
                    if (prizeNum != -1) {
                        retList.add(initUserGrandPrizeSearchMsg(prizeNum, imgEntity));
                    }
                });
            }
        }
        return retList;
    }

    /**

     */
    private static UserGrandPrizeSearchMsg initUserGrandPrizeSearchMsg(int prizeNum, ImgProductGrandPrizeMsgEntity imgEntity) {
        UserGrandPrizeSearchMsg msg = new UserGrandPrizeSearchMsg();
        msg.setPzPic(MediaUtil.getMediaUrl(imgEntity.getImgUrl()));
        msg.setPzQt(prizeNum);
        return msg;
    }

    /**

     */
    private static int dealCrossPrizeNum(CrossServerSearchProductPrizeMsg msg, String name) {
        int retNum = -1;
        switch (name) {
            case "gossip":
                
                retNum = msg==null?0:msg.getGossip();
                break;
            case "gem":
                
                retNum = msg==null?0:msg.getGem();
                break;
            case "pileTower":
                
                retNum = msg==null?0:msg.getPileTower();
                break;
            case "agyptBox":
                
                retNum = msg==null?0:msg.getAgyptBox();
                break;
            case "allDragon":
                
                retNum = msg==null?0:msg.getAllDragon();
                break;
            case "heroJackpot":
                
                retNum = msg==null?0:msg.getHeroJackpot();
                break;
            case "freeGame":
                
                retNum = msg==null?0:msg.getFreeGame();
                break;
            case "jackpotMinor":
                
                retNum = msg==null?0:msg.getJackpotMinor();
                break;
            case "jackpotMajor":
                
                retNum = msg==null?0:msg.getJackpotMajor();
                break;
            case "jackpotGrand":
                
                retNum = msg==null?0:msg.getJackpotGrand();
                break;
            case "thunder":
                
                retNum = msg==null?0:msg.getThunder();
                break;
            case "collectCard":
                
                retNum = msg==null?0:msg.getCollectCard();
                break;
            case "whistle":
                
                retNum = msg==null?0:msg.getWhistle();
                break;
            case "kingkongJackpot":
                
                retNum = msg==null?0:msg.getKingkongJackpot();
                break;
        }
        return retNum;
    }

    /**

     */
    public static UserGrandPrizeMsgEntity initUserGrandPrizeMsgEntity(int userId) {
        UserGrandPrizeMsgEntity entity = new UserGrandPrizeMsgEntity();
        entity.setUserId(userId);
        entity.setPileTower(0);
        entity.setDragonBall(0);
        entity.setRoomRankFirst(0);
        entity.setRoomRankSecond(0);
        entity.setRoomRankThird(0);
        entity.setFreeGame(0);
        entity.setGem(0);
        entity.setPrizeWheelGrand(0);
        entity.setPrizeWheelMajor(0);
        entity.setPrizeWheelMinor(0);
        entity.setAgyptBox(0);
        entity.setGossip(0);
        entity.setHeroBattle(0);
        entity.setThunder(0);
        entity.setJackpotKingkong(0);
        entity.setJackpotHero(0);
        entity.setWhistle(0);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void addRegisterWelfare(int userId) {
        int presentCoin = LoginUtil.registerPresentCoin();
        if(presentCoin>0){
            
            boolean flag = UserBalanceUtil.addUserBalance(userId, CommodityUtil.gold(), presentCoin);
            if(flag) {
                
                UserCostLogUtil.registerWelfare(userId, presentCoin);
            }else{

            }
        }
        
        UserUsdtUtil.addUsdtBalance(userId, 10000);
    }

    /**

     */
    public static boolean existUser(int userId) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity!=null && entity.getStatus()== UserStatusEnum.NORMAL.getCode();
    }

    /**

     */
    public static void updateUserInfo(int userId, String nickName, int sex) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        entity.setNickName(nickName);
        entity.setSex(sex);
        UserInfoDao.getInstance().update(entity);
    }

    /**

     */
    public static void updateUserPassword(int userId, String email, String password) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        try {
            boolean emailBindFlag = StrUtil.checkEmpty(entity.getEmail());
            entity.setEmail(email);
            entity.setPassword(MD5Util.MD5(password));
            boolean flag = UserInfoDao.getInstance().update(entity);
            if(flag && emailBindFlag){
                
                EmailUserDao.getInstance().setCache(email, userId);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }

    /**

     */
    public static void loadUserInfo(int userId, Map<String, Object> dataMap) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        dataMap.put("plyNm", entity.getNickName());
        dataMap.put("plyPct", MediaUtil.getMediaUrl(entity.getImgUrl()));
        dataMap.put("email", entity.getEmail());
        dataMap.put("sex", entity.getSex());
        
        UserAttributeMsgEntity userAttribute = UserAttributeMsgDao.getInstance().loadMsg(userId);
        if(userAttribute!=null) {
            dataMap.put("atbTbln", UserAttributeUtil.userAttributeMsg(userAttribute));
        }else{

        }
    }

    /**

     */
    public static void updateUserEmail(int userId, String email) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(email!=null){
            entity.setEmail(email);
            boolean flag = UserInfoDao.getInstance().update(entity);
            if(flag){
                EmailUserDao.getInstance().setCache(email, userId);
            }
        }
    }

    /**

     */
    public static String loadUserEmail(int userId) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getEmail();
    }
}
