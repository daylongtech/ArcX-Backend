package avatar.service.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.*;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.info.ProductAliasDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.SyncInnoOccpuyCheckTask;
import avatar.task.product.innoMsg.InnoAwardLockDealTask;
import avatar.task.product.innoMsg.InnoAwardScoreMultiDealTask;
import avatar.task.product.innoMsg.InnoSettlementWindowTask;
import avatar.task.product.innoMsg.InnoVoiceNoticeTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.innoMsg.InnoProductOperateUtil;
import avatar.util.innoMsg.InnoSendWebsocketUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**

 */
public class InnoReceiveMsgService {

    /**

     */
    public static void startGame(Map<String, Object> jsonMap) {

                JsonUtil.mapToJson(jsonMap));
        InnoReceiveStartGameMsg startGameMsg = InnoParamsUtil.startGameMsg(jsonMap);
        if(startGameMsg!=null){
            int status = InnoParamsUtil.loadClientCode(startGameMsg.getStatus());
            int userId = startGameMsg.getUserId();
            int productId = ProductAliasDao.getInstance().loadByAlias(startGameMsg.getAlias());
            
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    if (!ParamsUtil.isSuccess(status)) {
                        
                        InnoSendWebsocketUtil.sendWebsocketMsg(userId,
                                ProductOperationEnum.START_GAME.getCode(), status, 0,
                                productId);
                    } else {
                        
                        InnoProductOperateUtil.startGame(startGameMsg, productId);
                    }
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
    }

    /**

     */
    public static void productOperate(Map<String, Object> jsonMap) {

                JsonUtil.mapToJson(jsonMap));
        InnoReceiveProductOperateMsg productOperateMsg = InnoParamsUtil.productOperateMsg(jsonMap);
        if(productOperateMsg!=null){
            int status = InnoParamsUtil.loadClientCode(productOperateMsg.getStatus());
            int operateType = productOperateMsg.getInnoProductOperateType();
            int userId = productOperateMsg.getUserId();
            int productId = ProductAliasDao.getInstance().loadByAlias(productOperateMsg.getAlias());
            if(!ParamsUtil.isSuccess(status) && InnoParamsUtil.loadProductOperateType(operateType)!=-1) {
                
                if(operateType== InnoProductOperateTypeEnum.PUSH_COIN.getCode()){
                    
                    ProductDealUtil.pushCoinFailDeal(userId, productId, productOperateMsg);
                }
            }else{
                
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                        2000);
                try {
                    if (lock.lock()) {
                        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                        if(roomMsg.getGamingUserId()==userId &&
                                roomMsg.getOnProductTime()==productOperateMsg.getOnProductTime()){
                            
                            InnoProductOperateUtil.productOperate(productOperateMsg, productId);
                        }else{


                                    productId, roomMsg.getOnProductTime(), productOperateMsg.getOnProductTime(),
                                    roomMsg.getGamingUserId(), userId);
                        }
                    }
                }catch (Exception e){
                    ErrorDealUtil.printError(e);
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    /**

     */
    public static void startGmmeOccopyCheck(Map<String, Object> jsonMap) {

                JsonUtil.mapToJson(jsonMap));
        InnoStartGameOccupyMsg startGameOccopyMsg = InnoParamsUtil.startGameOccupyMsg(jsonMap);
        if(startGameOccopyMsg!=null) {
            int userId = startGameOccopyMsg.getUserId();
            int productId = ProductAliasDao.getInstance().loadByAlias(startGameOccopyMsg.getAlias());
            
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                    int gamingUserId = roomMsg.getGamingUserId();
                    if(gamingUserId==0){
                        


                        SchedulerSample.delayed(1, new SyncInnoOccpuyCheckTask(startGameOccopyMsg));
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
    public static void productMsg(Map<String, Object> jsonMap) {

                JsonUtil.mapToJson(jsonMap));
        String alias = jsonMap.get("alias").toString();
        int productId = ProductAliasDao.getInstance().loadByAlias(alias);
        if(productId>0) {
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    ProductGamingUtil.updateGamingUserMsg(productId, InnoParamsUtil.initInnoProductMsg(jsonMap));
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
    public static void awardLockDealMsg(Map<String, Object> jsonMap) {

                JsonUtil.mapToJson(jsonMap));
        InnoAwardLockMsg innoAwardLockMsg = InnoParamsUtil.initInnoAwardLockMsg(jsonMap);
        int productId = ProductAliasDao.getInstance().loadByAlias(innoAwardLockMsg.getAlias());
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardLockMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardLockMsg.getServerSideType())){
                    SchedulerSample.delayed(1,
                            new InnoAwardLockDealTask(innoAwardLockMsg, productId));
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**

     */
    public static void settlementWindowMsg(Map<String, Object> jsonMap) {

                JsonUtil.mapToJson(jsonMap));
        InnoSettlementWindowMsg innoSettlementWindowMsg = InnoParamsUtil.initInnoSettlementWindowMsg(jsonMap);
        int productId = ProductAliasDao.getInstance().loadByAlias(innoSettlementWindowMsg.getAlias());
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoSettlementWindowMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoSettlementWindowMsg.getServerSideType())){
                    SchedulerSample.delayed(1,
                            new InnoSettlementWindowTask(innoSettlementWindowMsg, productId));
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**

     */
    public static void awardScoreMulti(Map<String, Object> jsonMap) {

                JsonUtil.mapToJson(jsonMap));
        InnoAwardScoreMultiMsg innoAwardScoreMultiMsg = InnoParamsUtil.initInnoAwardScoreMultiMsg(jsonMap);
        int productId = ProductAliasDao.getInstance().loadByAlias(innoAwardScoreMultiMsg.getAlias());
        
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardScoreMultiMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardScoreMultiMsg.getServerSideType())){
                    SchedulerSample.delayed(1,
                            new InnoAwardScoreMultiDealTask(innoAwardScoreMultiMsg, productId));
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**

     */
    public static void voiceNotice(JSONObject jsonMap) {
        String alias = InnoParamsUtil.loadStringParams(jsonMap, "alias");
        if(!StrUtil.checkEmpty(alias)) {
            int productId = ProductAliasDao.getInstance().loadByAlias(alias);
            if(productId>0) {
                SchedulerSample.delayed(1,
                        new InnoVoiceNoticeTask(jsonMap, productId));
            }
        }else{

        }
    }
}
