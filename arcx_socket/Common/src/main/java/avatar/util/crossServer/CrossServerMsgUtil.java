package avatar.util.crossServer;

import avatar.data.crossServer.CrossServerSearchProductPrizeMsg;
import avatar.data.crossServer.CrossServerUserSearchMsg;
import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.data.crossServer.ServerTypeUserMsg;
import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.entity.crossServer.CrossServerDomainEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.system.ServerSideTypeEnum;
import avatar.module.crossServer.*;
import avatar.module.user.info.UserDefaultHeadImgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.task.crossServer.UpdateGeneralCrossServerUserMsgTask;
import avatar.util.basic.general.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.*;
import avatar.util.trigger.SchedulerSample;

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
    public static String imgDeal(int serverSideType, String imgUrl) {
        return (isMetaPusherServer(serverSideType) || imgUrl.contains("default"))?
                UserCrossPlatformImgDao.getInstance().loadMsg():imgUrl;
    }

    /**

     */
    public static boolean isMetaPusherServer(int serverSideType){
        return serverSideType==metaPusherServer();
    }

    /**

     */
    public static int metaPusherServer(){
        return ServerSideTypeEnum.META_PUSHER.getCode();
    }

    /**

     */
    public static ServerTypeUserMsg initServerTypeUserMsg(int userId, int serverSideType) {
        ServerTypeUserMsg userMsg = new ServerTypeUserMsg();
        userMsg.setSvTp(serverSideType);
        userMsg.setPlyId(userId);
        if(isArcxServer(serverSideType)){
            
            
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            userMsg.setPlyNm(userInfoEntity==null?"":userInfoEntity.getNickName());
            userMsg.setPlyPct(userInfoEntity==null?"":MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        }else{
            if(isGeneralCrossServer(serverSideType)){
                
                GeneralCrossServerUserMsg csUserMsg = CrossServerUserMsgDao.getInstance().loadByMsg(serverSideType, userId);
                userMsg.setPlyNm(csUserMsg==null?"":csUserMsg.getNickName());
                userMsg.setPlyPct(csUserMsg==null?crossDefaultImg():
                        loadCrossUserImg(csUserMsg.getServerSideType(),csUserMsg.getImgUrl()));
            }else{
                
                ProductGamingUserMsg gamingUserMsg = ServerSideUserMsgDao.getInstance().loadByMsg(userId, serverSideType);
                userMsg.setPlyNm(gamingUserMsg==null?"":gamingUserMsg.getUserName());
                userMsg.setPlyPct(gamingUserMsg==null?crossDefaultImg():
                        loadCrossUserImg(gamingUserMsg.getServerSideType(), gamingUserMsg.getImgUrl()));
            }
        }
        return userMsg;
    }

    /**

     */
    public static boolean isGeneralCrossServer(int serverSideType){
        return CrossServerListDao.getInstance().loadAll().contains(serverSideType);
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
    private static String loadUserMsg(int userId, String domainMsg) {
        String retMsg = "";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("csUserId", userId);
        
        String httpRequest = ParamsUtil.httpRequestStr(domainMsg, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            
            retMsg = HttpClientUtil.sendHttpGet(httpRequest);
        }
        return retMsg;
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
