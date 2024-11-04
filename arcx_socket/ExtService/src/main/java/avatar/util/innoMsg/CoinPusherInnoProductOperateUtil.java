package avatar.util.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoReceiveProductOperateMsg;
import avatar.data.product.innoMsg.InnoReceiveStartGameMsg;
import avatar.entity.product.innoMsg.InnoProductWinPrizeMsgEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductBreakTypeEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.innoMsg.InnoProductWinPrizeMsgDao;
import avatar.module.product.innoMsg.SelfProductAwardMsgDao;
import avatar.module.product.innoMsg.SelfSpecialAwardMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.InnoProductAwardRefreshTimeTask;
import avatar.task.innoMsg.InnoReturnCoinTask;
import avatar.task.innoMsg.SyncInnoEndGameTask;
import avatar.task.innoMsg.SyncInnoProductOperateTask;
import avatar.task.product.innoMsg.InnoProductDragonWinPrizeDealTask;
import avatar.task.product.innoMsg.InnoProductWinPrizeDealTask;
import avatar.task.product.innoMsg.InnoProductWinPrizeNormalDealTask;
import avatar.task.product.innoMsg.InnoSpecialAwardTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.product.*;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**

 */
public class CoinPusherInnoProductOperateUtil {
    /**

     */
    public static void startGame(InnoReceiveStartGameMsg startGameMsg, int productId) {
        int status = ClientCode.SUCCESS.getCode();
        int userId = startGameMsg.getUserId();
        
        int userStartGameMulti = ProductGamingUtil.loadUserStartGameMulti(userId);
        if(userStartGameMulti>0){
            ProductGamingUtil.updateProductAwardLockMultiMsg(productId, userStartGameMulti);
        }
        
        int cost = ProductUtil.productCost(productId);
        boolean flag = UserBalanceUtil.costUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), cost);
        if(flag){
            int preCoinWeight = InnoProductUtil.userCoinWeight(userId, productId);
            
            initSelfProductAward(productId);
            
            ProductGamingUtil.initProductCostCoinMsg(productId);
            
            ProductGamingUtil.addCostCoin(productId, cost);
            
            InnerStartGameUtil.startInitMsg(productId, userId,
                    InnoParamsUtil.initResponseGeneralMsg(ProductUtil.loadProductAlias(productId), userId));
            
            InnerOffLineUtil.offLineTask(userId, productId);
            
            pushCoinOperate(userId, productId);
            
            SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
        }
        
        InnoSendWebsocketUtil.sendWebsocketMsg(userId,
                ProductOperationEnum.START_GAME.getCode(), status, 0,
                productId);
        if(ParamsUtil.isSuccess(status)){
            
            UserOperateLogUtil.startGame(userId, productId);
        }else{
            
            SchedulerSample.delayed(5, new SyncInnoEndGameTask(
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnoParamsUtil.initInnoEndGameMsg(productId, ProductUtil.loadProductAlias(productId), userId)));
        }
    }

    /**

     */
    private static void initSelfProductAward(int productId) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELF_PRODUCT_AWARD_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                
                SelfProductAwardMsgDao.getInstance().setCache(productId, new ArrayList<>());
                
                SelfSpecialAwardMsgDao.getInstance().setCache(productId, new HashMap<>());
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**

     */
    private static void pushCoinOperate(int userId, int productId) {
        
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(5, new SyncInnoProductOperateTask(
                ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                InnoParamsUtil.initInnoProductOperateMsg(productId, ProductUtil.loadProductAlias(productId), userId,
                        productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.PUSH_COIN.getCode(), false)));
    }

    /**

     */
    public static void productOperate(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int operateType = productOperateMsg.getInnoProductOperateType();
        if(operateType== InnoProductOperateTypeEnum.PUSH_COIN.getCode()){
            
            pushCoin(productId);
        }else if(operateType== InnoProductOperateTypeEnum.GET_COIN.getCode()){
            
            getCoin(productOperateMsg, productId);
        }else if(operateType== InnoProductOperateTypeEnum.WIN_PRIZE.getCode()){
            
            winPrize(productOperateMsg, productId);
        }else if(operateType== InnoProductOperateTypeEnum.BREAK_DOWN.getCode()){
            
            breakDown(productOperateMsg, productId);
        }
    }

    /**

     */
    private static void pushCoin(int productId) {

    }

    /**

     */
    private static void getCoin(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int coinNum = productOperateMsg.getCoinNum();

        if(coinNum>0){
            if(ProductUtil.isOutStartGameTime(productId)) {
                
                CoinPusherInnerReceiveDealUtil.getCoinDeal(productOperateMsg.getUserId(), productId, coinNum);
            }else{

                        ProductConfigMsg.startGameGetCoinTime);
            }
        }
    }

    /**

     */
    private static void winPrize(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int productAwardType = productOperateMsg.getAwardType();

                ProductAwardTypeEnum.getNameByCode(productAwardType));
        int userId = productOperateMsg.getUserId();
        int awardNum = productOperateMsg.getAwardNum();
        int isStart = productOperateMsg.getIsStart();
        if(productAwardType>0) {
            if(StrUtil.strToList(ProductConfigMsg.innoNoProcessAward).contains(productAwardType)){
                
                noProcessAwardDeal(userId, productId, awardNum, isStart, productAwardType);
            }if(StrUtil.strToList(ProductConfigMsg.innoJustDataAward).contains(productAwardType)){
                
                justDataAwardDeal(userId, productId, awardNum, isStart, productAwardType);
            }else{
                
                winPrizeDeal(userId, productId, productAwardType, awardNum, isStart);
            }
        }else{

                    productOperateMsg.getAlias(), userId, productAwardType);
        }
    }

    /**

     */
    private static void noProcessAwardDeal(int userId, int productId, int awardNum, int isStart, int productAwardType) {
        SchedulerSample.delayed(100, new InnoProductWinPrizeDealTask(
                productAwardType, productId, userId, awardNum, isStart));
    }

    /**

     */
    private static void justDataAwardDeal(int userId, int productId, int awardNum, int isStart, int productAwardType) {
        
        dealWinPrizeMsg(userId, productId, productAwardType, awardNum, isStart);
        
        SchedulerSample.delayed(100, new InnoProductWinPrizeDealTask(
                productAwardType, productId, userId, awardNum, isStart));
    }

    /**

     */
    private static long dealWinPrizeMsg(int userId, int productId, int productAwardType, int awardNum, int isStart) {
        long awardId = 0;
        boolean updateFlag = false;
        if(isStart== YesOrNoEnum.NO.getCode()){
            
            InnoProductWinPrizeMsgEntity entity = InnoProductWinPrizeMsgDao.getInstance().loadMsg(userId,
                    productId, productAwardType);
            if(entity!=null && entity.getIsStart()== YesOrNoEnum.YES.getCode()){
                
                entity.setAwardNum(awardNum);
                entity.setIsStart(isStart);
                InnoProductWinPrizeMsgDao.getInstance().update(entity);
                updateFlag = true;
                awardId = entity.getId();
            }
        }
        if(!updateFlag) {
            
            awardId = InnoProductWinPrizeMsgDao.getInstance().insert(ProductUtil.initInnoProductWinPrizeMsgEntity(
                    userId, productId, productAwardType, awardNum, isStart));
        }
        return awardId;
    }

    /**

     */
    private static void winPrizeDeal(int userId, int productId, int productAwardType, int awardNum, int isStart) {
        
        long awardId = dealWinPrizeMsg(userId, productId, productAwardType, awardNum, isStart);
        
        dealSelfProductAwardTask(userId, productId, isStart, awardId);
        
        if(StrUtil.strToList(ProductConfigMsg.innoSpecialAwardDeal).contains(productAwardType)) {
            SchedulerSample.delayed(5, new InnoSpecialAwardTask(
                    userId, productId, productAwardType, isStart));
        }
        if((productAwardType== ProductAwardTypeEnum.DRAGON_BALL.getCode() || isStart== YesOrNoEnum.YES.getCode())
                && productAwardType!=ProductAwardTypeEnum.AVERAGE_AWARD.getCode()) {
            
            SchedulerSample.delayed(100, new InnoProductWinPrizeNormalDealTask(
                    productId, userId, productAwardType));
        }
        if(productAwardType==ProductAwardTypeEnum.DRAGON_BALL.getCode()){
            
            SchedulerSample.delayed(100, new InnoProductDragonWinPrizeDealTask(
                    productId, userId));
        }else{
            
            SchedulerSample.delayed(100, new InnoProductWinPrizeDealTask(
                    productAwardType, productId, userId, awardNum, isStart));
        }
    }

    /**

     */
    private static void dealSelfProductAwardTask(int userId, int productId, int isStart, long awardId) {
        if(isStart== YesOrNoEnum.YES.getCode()) {
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELF_PRODUCT_AWARD_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    List<Long> list = SelfProductAwardMsgDao.getInstance().loadByProductId(productId);
                    boolean openTaskFlag = list.size()==0;
                    if(!list.contains(awardId)){
                        list.add(awardId);
                        
                        SelfProductAwardMsgDao.getInstance().setCache(productId, list);
                    }
                    if(openTaskFlag){
                        SchedulerSample.delayed(10, new InnoProductAwardRefreshTimeTask(productId, userId));
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
    }

    /**

     */
    private static void breakDown(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int breakType = productOperateMsg.getBreakType();

                InnoProductBreakTypeEnum.getNameByCode(breakType));
        if(breakType== InnoProductBreakTypeEnum.PUSH_COIN_ERROR.getCode()){
            
            SchedulerSample.delayed(5, new InnoReturnCoinTask(productId, productOperateMsg.getUserId(),
                    productOperateMsg.getOnProductTime()));
        }
        
        ProductUtil.addRepairMsg(productId, breakType);
    }
}
