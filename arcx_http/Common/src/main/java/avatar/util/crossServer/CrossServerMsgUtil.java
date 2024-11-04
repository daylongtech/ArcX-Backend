package avatar.util.crossServer;

import avatar.data.crossServer.ConciseServerUserMsg;
import avatar.data.crossServer.CrossServerSearchProductPrizeMsg;
import avatar.data.crossServer.CrossServerUserSearchMsg;
import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.entity.crossServer.CrossServerDomainEntity;
import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.basicConfig.basic.CrossServerConfigMsg;
import avatar.global.enumMsg.basic.system.ServerSideTypeEnum;
import avatar.module.crossServer.CrossServerDomainDao;
import avatar.module.crossServer.CrossServerListDao;
import avatar.module.crossServer.CrossServerUserMsgDao;
import avatar.module.crossServer.UserCrossPlatformImgDao;
import avatar.module.user.info.UserDefaultHeadImgDao;
import avatar.module.user.info.UserGrandPrizeMsgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.task.crossServer.UpdateGeneralCrossServerUserMsgTask;
import avatar.util.basic.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.*;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class CrossServerMsgUtil {
    /**

     */
    public static boolean isArcxServer(int serverSideType){
        return serverSideType==0 || serverSideType==arcxServer();
    }

    /**

     */
    public static int arcxServer() {
        return ServerSideTypeEnum.ARCX.getCode();
    }

    /**

     */
    private static int metaPusherServer(){
        return ServerSideTypeEnum.META_PUSHER.getCode();
    }

    /**

     */
    public static boolean isMetaPusherServer(int serverSideType){
        return serverSideType==metaPusherServer();
    }

    /**

     */
    public static boolean isGeneralCrossServer(int serverSideType){
        return CrossServerListDao.getInstance().loadAll().contains(serverSideType);
    }

    /**

     */
    public static ConciseServerUserMsg initConciseServerUserMsg(ProductGamingUserMsg gamingUserMsg) {
        int userId = gamingUserMsg.getUserId();
        if(isArcxServer(gamingUserMsg.getServerSideType())){
            
            return initConciseServerUserMsg(userId, gamingUserMsg.getProductId());
        }else {
            ConciseServerUserMsg msg = new ConciseServerUserMsg();
            msg.setPlyId(userId);
            msg.setPlyNm(gamingUserMsg.getUserName());
            msg.setPlyPct(loadCrossUserImg(gamingUserMsg.getServerSideType(), gamingUserMsg.getImgUrl()));
            msg.setSvTp(gamingUserMsg.getServerSideType());
            return msg;
        }
    }

    /**

     */
    private static ConciseServerUserMsg initConciseServerUserMsg(int userId, int productId) {
        ConciseServerUserMsg msg = new ConciseServerUserMsg();
        msg.setPlyId(userId);
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setPlyNm(entity==null?"":entity.getNickName());
        msg.setPlyPct(entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl()));
        msg.setSvTp(arcxServer());
        return msg;
    }

    /**

     */
    public static void initGeneralUserMsg(int userId, int serverSideType, Map<String, Object> dataMap) {
        
        GeneralCrossServerUserMsg userMsg = CrossServerUserMsgDao.getInstance().
                loadByMsg(serverSideType, userId);
        if(userMsg!=null) {
            dataMap.put("plyNm", userMsg.getNickName());
            dataMap.put("devPzTbln", UserUtil.initProductGrandPrize(userMsg.getProductPrizeMsg()));
            dataMap.put("plyPct", loadCrossUserImg(userMsg.getServerSideType(), userMsg.getImgUrl()));
        }
    }

    /**

     */
    public static GeneralCrossServerUserMsg loadGeneralCrossServerUserMsg(int serverSideType, int userId) {
        GeneralCrossServerUserMsg msg = null;
        try{
            String domainMsg = loadCrossServerUserDomain(serverSideType);
            if(!StrUtil.checkEmpty(domainMsg)){
                String dataMsg = loadUserMsg(userId, domainMsg);
                if(!StrUtil.checkEmpty(dataMsg)){
                    msg = dealRetGeneralCrossServerUserMsg(serverSideType, userId, dataMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;
        }
        return msg;
    }

    /**

     */
    private static GeneralCrossServerUserMsg dealRetGeneralCrossServerUserMsg(int serverSideType,
            int userId, String dataMsg) {
        GeneralCrossServerUserMsg msg = new GeneralCrossServerUserMsg();
        Map<String, Object> searchMap = JsonUtil.strToMap(dataMsg);
        Map<String, Object> dataMap = (Map<String, Object>) searchMap.get("data");
        Map<String, Object> userMap = (Map<String, Object>) dataMap.get("userMsg");
        CrossServerUserSearchMsg searchMsg = initDataUserMsg(userMap);
        msg.setServerSideType(serverSideType);
        msg.setUserId(userId);
        msg.setNickName(searchMsg.getNickName());
        msg.setImgUrl(loadCrossUserImg(serverSideType, searchMsg.getImgUrl()));
        msg.setNationCode(searchMsg.getNationCode());
        msg.setNationEn(searchMsg.getNationEn());
        msg.setUserLevel(searchMsg.getUserLevel());
        msg.setVipLevel(searchMsg.getVipLevel());
        msg.setProductPrizeMsg(searchMsg.getProductPrizeMsg());
        msg.setCreateTime(TimeUtil.getNowTime());
        return msg;
    }

    /**

     */
    private static CrossServerUserSearchMsg initDataUserMsg(Map<String, Object> dataMap) {
        CrossServerUserSearchMsg retMsg = new CrossServerUserSearchMsg();
        retMsg.setUserId((int) dataMap.get("userId"));
        retMsg.setNickName((String) dataMap.get("nickName"));
        retMsg.setImgUrl((String) dataMap.get("imgUrl"));
        retMsg.setNationCode((String) dataMap.get("nationCode"));
        retMsg.setNationEn((String) dataMap.get("nationEn"));
        retMsg.setVipLevel(dataMap.containsKey("vipLevel")?((int) dataMap.get("vipLevel")):1);
        retMsg.setUserLevel(dataMap.containsKey("userLevel")?((int) dataMap.get("userLevel")):1);
        retMsg.setProductPrizeMsg(initDataProductPrize(dataMap));
        return retMsg;
    }

    /**

     */
    private static CrossServerSearchProductPrizeMsg initDataProductPrize(Map<String, Object> dataMap) {
        Map<String, Integer> prizeMap = (Map<String, Integer>) dataMap.get("productPrizeMsg");
        CrossServerSearchProductPrizeMsg msg = new CrossServerSearchProductPrizeMsg();
        msg.setPileTower(prizeMap.get("pileTower"));
        msg.setAllDragon(prizeMap.get("allDragon"));
        msg.setRoomRankFirst(prizeMap.get("roomRankFirst"));
        msg.setRoomRankSecond(prizeMap.get("roomRankSecond"));
        msg.setRoomRankThird(prizeMap.get("roomRankThird"));
        msg.setFreeGame(prizeMap.get("freeGame"));
        msg.setGem(prizeMap.get("gem"));
        msg.setJackpotMinor(prizeMap.get("jackpotMinor"));
        msg.setJackpotMajor(prizeMap.get("jackpotMajor"));
        msg.setJackpotGrand(prizeMap.get("jackpotGrand"));
        msg.setCollectCard(prizeMap.get("collectCard"));
        msg.setAgyptBox(prizeMap.get("agyptBox"));
        msg.setGossip(prizeMap.get("gossip"));
        msg.setHeroBattle(prizeMap.get("heroBattle"));
        msg.setThunder(prizeMap.get("thunder"));
        msg.setKingkongJackpot(prizeMap.get("kingkongJackpot"));
        msg.setHeroJackpot(prizeMap.get("heroJackpot"));
        msg.setWhistle(prizeMap.get("whistle"));
        return msg;
    }

    /**

     */
    private static String loadUserMsg(int userId, String domainMsg) {
        String retMsg = "";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("csUserId", userId);
        
        String httpRequest = ParamsUtil.httpRequestDomain(domainMsg, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            
            retMsg = HttpClientUtil.sendHttpGet(httpRequest);
        }
        return retMsg;
    }

    /**

     */
    private static String loadCrossServerUserDomain(int serverSideType) {
        CrossServerDomainEntity entity = CrossServerDomainDao.getInstance().loadByMsg(serverSideType);
        return entity==null?"":entity.getDomainMsg();
    }

    /**

     */
    public static void dealCrossServerUserMsg(GeneralCrossServerUserMsg userMsg) {
        if(userMsg!=null && userMsg.getCreateTime()< TimeUtil.getTodayLongTime()){
            SchedulerSample.delayed(5, new UpdateGeneralCrossServerUserMsgTask(userMsg));
        }
    }

    /**

     */
    public static void initElseServerUserMsg(ProductGamingUserMsg msg, Map<String, Object> dataMap) {
        dataMap.put("usrNm", msg==null?"":msg.getUserName());
        dataMap.put("usrPic", msg==null?crossDefaultImg():loadCrossUserImg(msg.getServerSideType(), msg.getImgUrl()));
        dataMap.put("eqmPz", UserUtil.initProductGrandPrize(null));
    }

    /**

     */
    public static CrossServerUserSearchMsg initUserMsg(int userId) {
        CrossServerUserSearchMsg msg = new CrossServerUserSearchMsg();
        msg.setUserId(userId);
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            
            dealBasicUserMsg(msg, entity);
            
            dealProductPrizeMsg(userId, msg);
        }
        return msg;
    }

    /**

     */
    private static void dealBasicUserMsg(CrossServerUserSearchMsg msg, UserInfoEntity entity) {
        int userId = entity.getId();
        msg.setNickName(entity.getNickName());
        msg.setImgUrl(entity.getImgUrl());
        msg.setNationCode(CrossServerConfigMsg.defaultNationCode);
        msg.setNationEn(CrossServerConfigMsg.defaultNationEn);
        msg.setUserLevel(1);
        msg.setVipLevel(CrossServerConfigMsg.defaultVipLevel);
    }

    /**

     */
    private static void dealProductPrizeMsg(int userId, CrossServerUserSearchMsg msg) {
        CrossServerSearchProductPrizeMsg prizeMsg = new CrossServerSearchProductPrizeMsg();
        
        UserGrandPrizeMsgEntity entity = UserGrandPrizeMsgDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            prizeMsg.setPileTower(entity.getPileTower());
            prizeMsg.setAllDragon(entity.getDragonBall());
            prizeMsg.setRoomRankFirst(entity.getRoomRankFirst());
            prizeMsg.setRoomRankSecond(entity.getRoomRankSecond());
            prizeMsg.setRoomRankThird(entity.getRoomRankThird());
            prizeMsg.setFreeGame(entity.getFreeGame());
            prizeMsg.setGem(entity.getGem());
            prizeMsg.setJackpotGrand(entity.getPrizeWheelGrand());
            prizeMsg.setJackpotMajor(entity.getPrizeWheelMajor());
            prizeMsg.setJackpotMinor(entity.getPrizeWheelMinor());
            prizeMsg.setCollectCard(0);
            prizeMsg.setAgyptBox(entity.getAgyptBox());
            prizeMsg.setGossip(entity.getGossip());
            prizeMsg.setHeroBattle(entity.getHeroBattle());
            prizeMsg.setThunder(entity.getThunder());
            prizeMsg.setKingkongJackpot(entity.getJackpotKingkong());
            prizeMsg.setHeroJackpot(entity.getJackpotHero());
            prizeMsg.setWhistle(entity.getWhistle());
        }
        msg.setProductPrizeMsg(prizeMsg);
    }

    /**

     */
    private static String crossDefaultImg(){
        String imgUrl = UserCrossPlatformImgDao.getInstance().loadMsg();
        if(StrUtil.checkEmpty(imgUrl)) {
            List<String> list = UserDefaultHeadImgDao.getInstance().loadAll();
            imgUrl = MediaUtil.getMediaUrl(list.get(0));
        }
        return imgUrl;
    }

    /**

     */
    public static String loadCrossUserImg(int serverSideType, String imgUrl) {
        if(!StrUtil.checkEmpty(imgUrl)) {
            String url = isArcxServer(serverSideType) ? MediaUtil.getMediaUrl(imgUrl) :
                    MediaUtil.getCrossServerMediaUrl(serverSideType, imgUrl);
            if (StrUtil.checkEmpty(url)) {
                url = crossDefaultImg();
            }
            return url;
        }else{
            return "";
        }
    }
}
