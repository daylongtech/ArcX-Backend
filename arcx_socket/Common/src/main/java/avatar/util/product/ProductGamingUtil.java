package avatar.util.product;

import avatar.data.product.gamingMsg.*;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.innoMsg.InnoProductMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.info.GamingStateEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.crossServer.ServerSideUserMsgDao;
import avatar.module.product.gaming.*;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.innoMsg.InnoPushCoinWindowDao;
import avatar.module.product.productType.LotteryCoinPercentDao;
import avatar.module.user.product.UserLotteryMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.AddUserGameExpTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserNoticePushUtil;

import java.util.Collections;
import java.util.List;

/**

 */
public class ProductGamingUtil {
    /**

     */
    public static ProductRoomMsg initProductRoomMsg(int productId) {

        ProductRoomMsg msg = new ProductRoomMsg();
        msg.setProductId(productId);
        msg.setGamingUserId(0);
        msg.setOnProductTime(0);
        msg.setPushCoinOnTime(0);
        return msg;
    }

    /**

     */
    public static DollGamingMsg initDollGamingMsg(int productId) {
        DollGamingMsg msg = new DollGamingMsg();
        msg.setProductId(productId);
        msg.setTime(0);
        msg.setInitalization(false);
        msg.setCatch(false);
        msg.setGamingState(GamingStateEnum.NO_PROPLE.getCode());
        
        DollGamingMsgDao.getInstance().setCache(productId, msg);
        return msg;
    }

    /**

     */
    public static void updateInnoProductSettlementMsg(int productId) {
        InnoProductOffLineMsg msg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
        msg.setOffLineTime(TimeUtil.getNowTime());
        msg.setMulti(loadMultiLevel(productId));
        ProductSettlementMsgDao.getInstance().setCache(productId, msg);
    }

    /**

     */
    private static void updateInnoProductSettlementMsg(int productId, int productMulti) {
        InnoProductOffLineMsg msg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
        msg.setOffLineTime(TimeUtil.getNowTime());
        msg.setMulti(productMulti);
        ProductSettlementMsgDao.getInstance().setCache(productId, msg);
    }

    /**

     */
    public static InnoProductOffLineMsg initInnoProductOffLineMsg(int productId) {
        InnoProductOffLineMsg msg = new InnoProductOffLineMsg();
        msg.setProductId(productId);
        msg.setOffLineTime(0);
        msg.setMulti(loadMultiLevel(productId));
        return msg;
    }

    /**

     */
    public static int loadMultiLevel(int productId) {
        int multiLevel = 1;
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        int coinMul = msg.getCoinMulti();
        if(coinMul>0){
            
            List<Integer> multiList = InnoPushCoinMultiDao.getInstance().loadBySecondType(
                    ProductUtil.loadSecondType(productId));
            if(multiList.size()>0){
                Collections.sort(multiList);
                for(int i=0;i<multiList.size();i++){
                    if(coinMul==multiList.get(i)){
                        multiLevel += i;
                        break;
                    }
                }
            }
        }
        
        multiLevel = Math.min(multiLevel, ProductConfigMsg.maxCoinMultiLevel);
        return multiLevel;
    }

    /**

     */
    public static ProductAwardLockMsg initProductAwardLockMsg(int productId) {
        ProductAwardLockMsg msg = new ProductAwardLockMsg();
        msg.setProductId(productId);
        msg.setIsAwardLock(YesOrNoEnum.NO.getCode());
        msg.setLockTime(0);
        return msg;
    }

    /**

     */
    public static void reInitProductAwardLockMsg(int productId) {
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        msg.setProductId(productId);
        msg.setIsAwardLock(YesOrNoEnum.NO.getCode());
        msg.setLockTime(0);
        ProductAwardLockDao.getInstance().setCache(productId,msg);
    }

    /**

     */
    public static ProductGamingUserMsg initProductGamingUserMsg(int productId) {
        ProductGamingUserMsg msg = new ProductGamingUserMsg();
        msg.setServerSideType(0);
        msg.setProductId(productId);
        msg.setUserId(0);
        msg.setUserName("");
        msg.setImgUrl("");
        return msg;
    }

    /**

     */
    public static ProductCostCoinMsg initProductCostCoinMsg(int productId) {
        ProductCostCoinMsg msg = new ProductCostCoinMsg();
        msg.setProductId(productId);
        msg.setSumAddCoin(0);
        msg.setSumCostCoin(0);
        return msg;
    }

    /**

     */
    public static long costCostCoin(int productId, int cost){
        long oriSumCostCoin = 0;
        RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_PUSH_COIN_LOCK+"_"+productId,
                2000);
        try {
            if (pushCoinLock.lock()) {
                ProductCostCoinMsg msg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                if (msg != null) {
                    oriSumCostCoin = msg.getSumCostCoin();
                    long sumCostCoin = Math.max(0, (msg.getSumCostCoin() - cost));
                    msg.setSumCostCoin(sumCostCoin);
                    ProductCostCoinMsgDao.getInstance().setCache(productId, msg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            pushCoinLock.unlock();
        }
        return oriSumCostCoin;
    }

    /**

     */
    public static void addCostCoin(int productId, int cost){
        if(cost>0) {
            RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_PUSH_COIN_LOCK + "_" + productId,
                    2000);
            try {
                if (pushCoinLock.lock()) {
                    ProductCostCoinMsg msg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                    if (msg != null) {
                        msg.setSumCostCoin(msg.getSumCostCoin() + cost);
                        ProductCostCoinMsgDao.getInstance().setCache(productId, msg);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                pushCoinLock.unlock();
            }
        }
    }

    /**

     */
    public static int loadGamingUserId(int productId) {
        int userId = 0;
        
        ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(gamingUserMsg!=null && gamingUserMsg.getUserId()>0 &&
                CrossServerMsgUtil.isArcxServer(gamingUserMsg.getServerSideType())){
            userId = gamingUserMsg.getUserId();
        }
        return userId;
    }

    /**

     */
    public static int loadInnoLastMultiLevel(int productId){
        int multiLevel = 0;
        long coolingTime = loadMultiCollingTime(productId);
        if(coolingTime>0) {
            
            InnoProductOffLineMsg msg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
            if(msg!=null && (TimeUtil.getNowTime()-msg.getOffLineTime())<coolingTime){
                multiLevel = msg.getMulti();
            }
        }
        return multiLevel;
    }

    /**

     */
    private static long loadMultiCollingTime(int productId) {
        long collTime = 0;
        int secondType = ProductUtil.loadSecondType(productId);
        if(secondType>0){
            
            InnoPushCoinWindowMsgEntity entity = InnoPushCoinWindowDao.getInstance().loadBySecondType(secondType);
            collTime = entity==null?0:entity.getMultiCoolingTime();
        }
        return collTime*1000;
    }

    /**

     */
    public static int loadCoinMulti(int productId, int multiLevel) {
        int coinMulti = 0;
        List<Integer> multiList = InnoPushCoinMultiDao.getInstance().loadBySecondType(
                ProductUtil.loadSecondType(productId));
        if(multiList.size()>0){
            if(multiLevel>multiList.size()){
                
                coinMulti = multiList.get(0);
            }else{
                for(int i=0;i<multiList.size();i++){
                    if((multiList.size()-i)==multiLevel){
                        coinMulti = multiList.get(i);
                        break;
                    }
                }
            }
        }
        return coinMulti;
    }

    /**

     */
    public static boolean isProductAwardLock(int productId){
        boolean flag = false;
        
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        if(msg.getIsAwardLock()==YesOrNoEnum.YES.getCode()){
            
            
            InnoPushCoinWindowMsgEntity windowMsgEntity = InnoPushCoinWindowDao.getInstance().
                    loadBySecondType(ProductUtil.loadSecondType(productId));
            long outTime = windowMsgEntity==null?0:windowMsgEntity.getAwardLockOutTime()*1000;
            if((TimeUtil.getNowTime()-msg.getLockTime())>=outTime){

                        productId, outTime/1000);
                
                updateProductAwardLockMsg(productId, YesOrNoEnum.NO.getCode());
            }else{
                flag = true;
            }
        }
        return flag;
    }

    /**

     */
    public static void updateProductAwardLockMsg(int productId, int isStart) {
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        msg.setIsAwardLock(isStart);
        if(isStart==YesOrNoEnum.YES.getCode()){
            msg.setLockTime(TimeUtil.getNowTime());
        }else{
            msg.setLockTime(0);
        }
        ProductAwardLockDao.getInstance().setCache(productId, msg);
    }

    /**

     */
    public static int loadUserStartGameMulti(int userId){
        return UserStartGameMultiMsgDao.getInstance().loadByUserId(userId).getCoinMulti();
    }

    /**

     */
    public static UserStartGameMultiMsg initUserStartGameMultiMsg(int userId) {
        UserStartGameMultiMsg msg = new UserStartGameMultiMsg();
        msg.setUserId(userId);
        msg.setCoinMulti(0);
        return msg;
    }

    /**

     */
    public static void updateProductAwardLockMultiMsg(int productId, int coinMulti) {
        if(coinMulti>0) {
            ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
            msg.setCoinMulti(coinMulti);
            ProductAwardLockDao.getInstance().setCache(productId, msg);
        }
    }

    /**

     */
    public static void updateGamingUserMsg(int productId, InnoProductMsg innoProductMsg) {
        boolean emptyFlag = false;
        
        ProductGamingUserMsg msg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        int productMulti = innoProductMsg==null?0:innoProductMsg.getProductMulti();
        if(innoProductMsg==null){
            
            msg.setServerSideType(0);
            msg.setUserName("");
            msg.setImgUrl("");
            emptyFlag = true;
        }else {
            
            int serverSideType = innoProductMsg.getServerSideType();
            msg.setServerSideType(serverSideType);
            msg.setUserId(innoProductMsg.getUserId());
            msg.setUserName(innoProductMsg.getUserName());
            msg.setImgUrl(CrossServerMsgUtil.loadCrossUserImg(serverSideType, innoProductMsg.getImgUrl()));
        }
        ProductGamingUserMsgDao.getInstance().setCache(productId, msg);
        
        if(msg.getUserId()>0 && !CrossServerMsgUtil.isArcxServer(msg.getServerSideType())){
            ServerSideUserMsgDao.getInstance().setCache(msg.getUserId(), msg.getServerSideType(), msg);
        }
        if(productMulti>0){
            
            ProductGamingUtil.updateInnoProductSettlementMsg(productId, productMulti);
        }
        
        if(emptyFlag) {
            initProductCache(productId);
        }
    }

    /**

     */
    private static void initProductCache(int productId) {
        
        ProductRoomMsg msg = ProductRoomDao.getInstance().loadByProductId(productId);
        if(msg!=null && msg.getGamingUserId()>0){
            ProductRoomDao.getInstance().delUser(productId, msg.getGamingUserId());
        }
    }

    /**

     */
    public static void updateGamingUserMsg(int productId, ResponseGeneralMsg responseGeneralMsg) {
        boolean emptyFlag = false;
        
        ProductGamingUserMsg msg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(responseGeneralMsg==null){
            
            msg.setServerSideType(0);
            msg.setUserId(0);
            msg.setUserName("");
            msg.setImgUrl("");
            emptyFlag = true;
        }else {
            
            int serverSideType = responseGeneralMsg.getServerSideType();
            msg.setServerSideType(serverSideType);
            msg.setUserId(responseGeneralMsg.getUserId());
            msg.setUserName(responseGeneralMsg.getUserName());
            msg.setImgUrl(CrossServerMsgUtil.loadCrossUserImg(
                    responseGeneralMsg.getServerSideType(), responseGeneralMsg.getImgUrl()));
        }
        ProductGamingUserMsgDao.getInstance().setCache(productId, msg);
        
        if(msg.getUserId()>0 && !CrossServerMsgUtil.isArcxServer(msg.getServerSideType())){
            ServerSideUserMsgDao.getInstance().setCache(msg.getUserId(), msg.getServerSideType(), msg);
        }
        
        if(emptyFlag) {
            initProductCache(productId);
        }
    }

    /**

     */
    public static void addEarnCoin(int userId, int productId, long result) {
        
        RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_COST_COIN_LOCK+"_"+productId,
                2000);
        try {
            if (pushCoinLock.lock()) {
                
                ProductCostCoinMsg costCoinMsg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                if (costCoinMsg != null) {
                    costCoinMsg.setSumAddCoin(costCoinMsg.getSumAddCoin() + result);
                    ProductCostCoinMsgDao.getInstance().setCache(productId, costCoinMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            pushCoinLock.unlock();
        }
        
        UserBalanceUtil.addUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), result);
        
        SchedulerSample.delayed(10, new AddUserGameExpTask(userId, result));
    }

    /**

     */
    public static PileTowerMsg initPileTowerMsg(int productId) {
        PileTowerMsg msg = new PileTowerMsg();
        msg.setProductId(productId);
        msg.setTillTime(0);
        msg.setPileTime(0);
        return msg;
    }

    /**

     */
    public static UserLotteryMsg initUserLotteryMsg(int userId, int secondLevelType) {
        UserLotteryMsg msg = new UserLotteryMsg();
        msg.setUserId(userId);
        msg.setSecondLevelType(secondLevelType);
        msg.setLotteryNum(0);
        return msg;
    }

    /**

     */
    public static void addLotteryResult(int userId, int result, int productId) {
        int addCoinNum = 0;
        int cost = ProductUtil.productCost(productId);
        int secondLevelType = ProductUtil.loadSecondType(productId);
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_COST_DEAL_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                UserLotteryMsg lotteryMsgEntity = UserLotteryMsgDao.getInstance().loadByMsg(userId, secondLevelType);
                if(lotteryMsgEntity!=null){
                    
                    addCoinNum = dealUserLotteryPercent(result, secondLevelType, lotteryMsgEntity);
                }else{

                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        int addScore = addCoinNum*cost;
        if(addCoinNum>0) {
            
            addEarnCoin(userId, productId, addScore);
        }
        
        UserNoticePushUtil.pushLotteryNotice(productId, secondLevelType, userId, result, addScore);
    }

    /**

     */
    private static int dealUserLotteryPercent(int lotteryNum, int secondLevelType, UserLotteryMsg lotteryMsg) {
        int addCoinNum = 0;
        int num = lotteryMsg.getLotteryNum() + lotteryNum;
        
        int percent = LotteryCoinPercentDao.getInstance().loadBySecondLevelType(secondLevelType);
        percent = percent > 0 ? percent : ProductConfigMsg.lotteryCoinExchange;
        if (num >= percent) {
            addCoinNum = num / percent;
        }
        num = num % percent;
        lotteryMsg.setLotteryNum(num);
        UserLotteryMsgDao.getInstance().setCache(lotteryMsg.getUserId(), secondLevelType, lotteryMsg);
        return addCoinNum;
    }

    /**

     */
    public static void flushPushCoinTime(int productId) {
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        roomMsg.setPushCoinOnTime(TimeUtil.getNowTime());
        ProductRoomDao.getInstance().setCache(roomMsg.getProductId(), roomMsg);
    }

    /**

     */
    public static DollAwardCommodityMsg loadDollAwardMsg(int productId){
        DollAwardCommodityMsg awardMsg = null;
        
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        int commodityType = productInfoEntity.getCommodityType();
        int awardId = productInfoEntity.getAwardId();
        int awardNum = StrUtil.loadInterValNum(productInfoEntity.getAwardMinNum(), productInfoEntity.getAwardMaxNum());
        if(awardNum>0){
            awardMsg = new DollAwardCommodityMsg();
            awardMsg.setCommodityType(commodityType);
            awardMsg.setAwardId(awardId);
            awardMsg.setAwardImgId(productInfoEntity.getImgProductPresentId());
            awardMsg.setAwardNum(awardNum);
        }
        return awardMsg;
    }

    /**

     */
    public static void updateUserStartGameMultiMsg(int userId, int coinMulti) {
        if(coinMulti>0) {
            UserStartGameMultiMsg msg = UserStartGameMultiMsgDao.getInstance().loadByUserId(userId);
            msg.setCoinMulti(coinMulti);
            UserStartGameMultiMsgDao.getInstance().setCache(userId, msg);
        }
    }

}
