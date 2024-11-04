package avatar.util.user;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.user.info.ConciseUserMsg;
import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.system.MobilePlatformTypeEnum;
import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.user.info.UserGrandPrizeMsgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class UserUtil {
    /**

     */
    public static String loadAccessToken(int userId){
        
        UserTokenMsgEntity entity = UserTokenMsgDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getAccessToken();
    }

    /**

     */
    public static int loadUserIdByToken(String accessToken) {
        int userId = 0;
        if(!StrUtil.checkEmpty(accessToken)){
            
            userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
        }
        return userId;
    }

    /**

     */
    public static boolean existUser(int userId) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity!=null && entity.getStatus()== UserStatusEnum.NORMAL.getCode();
    }

    /**

     */
    public static int checkAccessToken(String accessToken) {
        int status = ClientCode.SUCCESS.getCode();
        if(!StrUtil.checkEmpty(accessToken)){
            
            int userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
            if(userId==0){
                status = ClientCode.ACCESS_TOKEN_ERROR.getCode();
            }else if(userId>0 && UserUtil.loadUserStatus(userId)!= UserStatusEnum.NORMAL.getCode()){
                status = ClientCode.ACCOUNT_DISABLED.getCode();
            }else{
                
                UserTokenMsgEntity tokenMsgEntity = UserTokenMsgDao.getInstance().loadByUserId(userId);
                if(tokenMsgEntity==null || tokenMsgEntity.getAccessOutTime()<=TimeUtil.getNowTime()){
                    status = ClientCode.ACCESS_TOKEN_OUT_TIME.getCode();
                }
            }
        }
        return status;
    }

    /**

     */
    private static int loadUserStatus(int userId) {
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null?-1:userInfoEntity.getStatus();
    }

    /**

     */
    public static boolean isTourist(String accessToken){
        return accessToken.equals(ConfigMsg.touristAccessToken);
    }

    /**

     */
    public static String loadUserIp(int userId){
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getIp();
    }

    /**

     */
    public static ProductGamingUserMsg initProductGamingUserMsg(int userId, int serverSideType) {
        ProductGamingUserMsg msg = new ProductGamingUserMsg();
        msg.setServerSideType(serverSideType);
        msg.setProductId(0);
        msg.setUserId(userId);
        msg.setUserName("");
        msg.setImgUrl("");
        return msg;
    }

    /**

     */
    public static ConciseUserMsg initConciseUserMsg(int userId) {
        ConciseUserMsg msg = new ConciseUserMsg();
        msg.setPlyId(userId);
        
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setPlyNm(entity==null?"":entity.getNickName());
        msg.setPlyPct(entity==null?"": MediaUtil.getMediaUrl(entity.getImgUrl()));
        return msg;
    }

    /**

     */
    public static void updateUserGrandPrizeMsg(int userId, int awardType, int productId) {
        
        UserGrandPrizeMsgEntity entity = UserGrandPrizeMsgDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            if(awardType== ProductAwardTypeEnum.DRAGON_BALL.getCode()){
                
                entity.setDragonBall(entity.getDragonBall()+1);
            }else if(awardType==ProductAwardTypeEnum.FREE_GAME.getCode()){
                
                entity.setFreeGame(entity.getFreeGame()+1);
            }else if(awardType==ProductAwardTypeEnum.GRM.getCode()){
                
                entity.setGem(entity.getGem()+1);
            }else if(awardType==ProductAwardTypeEnum.PRIZE_WHEEL_GRAND.getCode()){
                
                entity.setPrizeWheelGrand(entity.getPrizeWheelGrand()+1);
            }else if(awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MAJOR.getCode()){
                
                entity.setPrizeWheelMajor(entity.getPrizeWheelMajor()+1);
            }else if(awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MINOR.getCode()){
                
                entity.setPrizeWheelMinor(entity.getPrizeWheelMinor()+1);
            }else if(awardType==ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode()){
                
                entity.setAgyptBox(entity.getAgyptBox()+1);
            }else if(awardType==ProductAwardTypeEnum.GOSSIP.getCode()){
                
                entity.setGossip(entity.getGossip()+1);
            }else if(awardType==ProductAwardTypeEnum.HERO_BATTLE.getCode()){
                
                entity.setHeroBattle(entity.getHeroBattle()+1);
            }else if(awardType==ProductAwardTypeEnum.THUNDER.getCode()){
                
                entity.setThunder(entity.getThunder()+1);
            }else if(awardType==ProductAwardTypeEnum.WHISTLE.getCode()){
                
                entity.setWhistle(entity.getWhistle()+1);
            }
            
            if(isJackpotAward(awardType)){
                int secondType = ProductUtil.loadSecondType(productId);
                if(secondType== ProductSecondTypeEnum.KING_KONG.getCode()){
                    
                    entity.setJackpotKingkong(entity.getJackpotKingkong()+1);
                }
            }
            UserGrandPrizeMsgDao.getInstance().update(entity);
        }else{

        }
    }

    /**

     */
    private static boolean isJackpotAward(int awardType) {
        return awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MINOR.getCode() ||
                awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MAJOR.getCode() ||
                awardType==ProductAwardTypeEnum.PRIZE_WHEEL_GRAND.getCode();
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
    public static boolean isAccountForbid(int userId) {
        
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(entity!=null && entity.getStatus()!=UserStatusEnum.NORMAL.getCode()){
            return true;
        }
        return false;
    }

    /**

     */
    public static boolean isIosUser(int userId) {
        
        
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null || userInfoEntity.getMobilePlatformType()==MobilePlatformTypeEnum.APPLE.getCode();
    }
}
