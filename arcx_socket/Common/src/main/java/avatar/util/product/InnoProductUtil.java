package avatar.util.product;

import avatar.data.product.gamingMsg.ProductCostCoinMsg;
import avatar.entity.product.innoMsg.InnoHighLevelCoinWeightEntity;
import avatar.entity.product.innoMsg.InnoProductCoinWeightEntity;
import avatar.entity.product.innoMsg.InnoProductSpecifyCoinWeightEntity;
import avatar.entity.product.innoNaPay.InnoNaPayCoinWeightEntity;
import avatar.entity.product.innoNaPay.InnoNaPaySpecifyCoinWeightEntity;
import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductCostCoinMsgDao;
import avatar.module.product.innoMsg.*;
import avatar.module.product.innoNaPay.InnoNaPayCoinWeightDao;
import avatar.module.product.innoNaPay.InnoNaPaySpecifyCoinWeightDao;
import avatar.module.user.product.UserWeightNaInnoDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;

import java.util.List;
import java.util.Map;

/**

 */
public class InnoProductUtil {
    /**

     */
    public static int userCoinWeight(int userId, int productId){
        int level = 1;
        long naNum = loadUserInnoProductNaNum(userId, productId);
        int secondType = ProductUtil.loadSecondType(productId);
        if(naNum>0){
            boolean payFlag = InnoNaPayUtil.isPay(userId);
            
            int highLevel = loadHighNaLevel(naNum, secondType, payFlag);
            if(highLevel>0){
                level = highLevel;
            }else {
                if (InnoNaPayUtil.isPay(userId)) {
                    
                    level = loadPayNaLevel(naNum, secondType);
                } else {
                    
                    level = loadNormalNaLevel(naNum, secondType);
                }
            }
        }
        return level;
    }

    /**

     */
    public static long loadUserInnoProductNaNum(int userId, int productId){
        long basicNaNum = stackInnoWeightNa(userId);
        
        long onlineNaNum = loadUserOnlineNa(productId);
        return basicNaNum + onlineNaNum;
    }

    /**

     */
    private static long stackInnoWeightNa(int userId) {
        long naNum = 0;
        
        List<UserWeightNaInnoEntity> list = UserWeightNaInnoDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserWeightNaInnoEntity entity : list){
                naNum += entity.getNaNum();
            }
        }
        return naNum;
    }

    /**

     */
    private static long loadUserOnlineNa(int productId) {
        long naNum = 0;
        ProductCostCoinMsg productPushCoinMsg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
        if(productPushCoinMsg!=null) {
            long plusScore = productPushCoinMsg.getSumAddCoin()-productPushCoinMsg.getSumCostCoin();
            naNum = Math.max(plusScore, 0);
        }
        return naNum;
    }

    /**

     */
    private static int loadHighNaLevel(long naNum, int secondType, boolean payFlag) {
        int retLevel = 0;
        
        List<InnoHighLevelCoinWeightEntity> list = InnoHighLevelCoinWeightDao.getInstance().
                loadByMsg(secondType, payFlag? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
        if(list.size()>0 && list.get(0).getNaNum()<=naNum){
            for(int i=0;i<list.size();i++){
                InnoHighLevelCoinWeightEntity entity = list.get(i);
                if(entity.getNaNum()>naNum){
                    retLevel = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    retLevel = entity.getLevel()+1;
                }
            }
        }
        return retLevel;
    }

    /**

     */
    private static int loadPayNaLevel(long naNum, int secondType) {
        int level = loadPaySpecifyLevel(naNum, secondType);
        if (level == 0) {
            
            level = loadPayGeneralLevel(naNum);
        }
        if (level == 0) {
            level = 1;
        }
        return level;
    }

    /**

     */
    private static int loadPayGeneralLevel(long naNum) {
        int level = 0;
        
        List<InnoNaPayCoinWeightEntity> list = InnoNaPayCoinWeightDao.getInstance().loadMsg();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoNaPayCoinWeightEntity entity = list.get(i);
                if(entity.getNaNum()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**

     */
    private static int loadPaySpecifyLevel(long naNum, int secondType) {
        int level = 0;
        
        List<InnoNaPaySpecifyCoinWeightEntity> list = InnoNaPaySpecifyCoinWeightDao.getInstance().
                loadBySecondType(secondType);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoNaPaySpecifyCoinWeightEntity entity = list.get(i);
                if(entity.getNaNum()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**

     */
    private static int loadNormalNaLevel(long naNum, int secondType) {
        
        int level = loadSpecifyLevel(naNum, secondType);
        if (level == 0) {
            
            level = loadGeneralLevel(naNum);
        }
        if (level == 0) {
            level = 1;
        }
        return level;
    }

    /**

     */
    private static int loadGeneralLevel(long naNum) {
        int level = 0;
        
        List<InnoProductCoinWeightEntity> list = InnoProductCoinWeightDao.getInstance().loadMsg();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoProductCoinWeightEntity entity = list.get(i);
                if(entity.getCoin()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**

     */
    private static int loadSpecifyLevel(long naNum, int secondType) {
        int level = 0;
        
        List<InnoProductSpecifyCoinWeightEntity> list = InnoProductSpecifyCoinWeightDao.getInstance().
                loadBySecondType(secondType);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoProductSpecifyCoinWeightEntity entity = list.get(i);
                if(entity.getCoin()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**

     */
    public static boolean isInnoFreeCoin(int productId){
        boolean flag = false;
        
        int secondType = ProductUtil.loadSecondType(productId);
        if(secondType== ProductSecondTypeEnum.AGYPT.getCode()){
            
            flag = isInnoSpecialAwardLink(productId, ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode());
        }else if(secondType==ProductSecondTypeEnum.CLOWN_CIRCUS.getCode()){
            
            flag = isInnoSpecialAwardLink(productId, ProductAwardTypeEnum.WHISTLE.getCode());
        }else if(secondType==ProductSecondTypeEnum.PIRATE.getCode()){
            
            flag = isInnoSpecialAwardLink(productId, ProductAwardTypeEnum.PIRATE_CANNON.getCode());
        }
        return flag;
    }

    /**

     */
    private static boolean isInnoSpecialAwardLink(int productId, int awardType){
        boolean flag = false;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNO_SPECIAL_AWARD_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                Map<Integer, Long> awardTypeMap = SelfSpecialAwardMsgDao.getInstance().loadByProductId(productId);
                if(awardTypeMap.containsKey(awardType)){
                    long time = awardTypeMap.get(awardType);
                    if((TimeUtil.getNowTime()-time)<innoSpecialLinkTillTime(awardType)*1000){
                        flag = true;
                    }else{
                        
                        awardTypeMap.remove(awardType);
                        SelfSpecialAwardMsgDao.getInstance().setCache(productId, awardTypeMap);
                    }
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
        return flag;
    }

    /**

     */
    private static long innoSpecialLinkTillTime(int awardType){
        long time = 0;
        if(awardType==ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode()){
            
            time = ProductConfigMsg.agyptOpenBoxTillTime;
        }else if(awardType==ProductAwardTypeEnum.WHISTLE.getCode()){
            
            time = ProductConfigMsg.clownCircusFerruleTillTime;
        }else if(awardType==ProductAwardTypeEnum.PIRATE_CANNON.getCode()){
            
            time = ProductConfigMsg.pirateCannonTillTime;
        }
        return time;
    }

    /**

     */
    public static boolean isCoinMultiLowerLimit(int userId, int coinMulti, int productId) {
        int lastMultiLevel = ProductGamingUtil.loadInnoLastMultiLevel(productId);
        boolean flag = lastMultiLevel>0 && coinMulti>ProductGamingUtil.loadCoinMulti(productId, lastMultiLevel);
        if(flag) {

        }
        return flag;
    }

    /**

     */
    public static boolean isUnlockVersion(String version){
        boolean flag = false;
        List<String> list = InnoProductUnlockVersionDao.getInstance().loadMsg();
        if(list.size()>0){
            flag = list.contains(version);
        }
        return flag;
    }

}
