package avatar.service.nft;

import avatar.data.nft.NftKnapsackMsg;
import avatar.data.nft.NftReportMsg;
import avatar.entity.nft.SellGoldMachineGoldHistoryEntity;
import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.lockMsg.LockMsg;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.module.nft.record.SellGoldMachineGoldHistoryDao;
import avatar.module.nft.user.UserNftListDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.NftCheckParamsUtil;
import avatar.util.nft.NftUtil;
import avatar.util.nft.SellGoldMachineUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ListUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class NftKnapsackService {
    /**

     */
    public static void nftKnapsack(Map<String, Object> map, Session session) {
        List<NftKnapsackMsg> retList = new ArrayList<>();
        
        int status = CheckParamsUtil.checkAccessTokenPage(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            
            List<String> list = ListUtil.getPageList(ParamsUtil.pageNum(map),
                    ParamsUtil.pageSize(map), UserNftListDao.getInstance().loadMsg(userId));
            if(list.size()>0){
                list.forEach(nftCode-> {
                    NftKnapsackMsg msg = NftUtil.initNftKnapsackMsg(nftCode);
                    if(msg!=null){
                        retList.add(msg);
                    }
                });
            }
        }
        
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
    }

    /**

     */
    public static void nftMsg(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.nftMsg(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            
            status = NftUtil.initNftMsg(nftCode, dataMap);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void upgradeNft(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.upgradeNft(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int attributeType = ParamsUtil.intParmasNotNull(map, "atbTp");
                    int userId = ParamsUtil.userId(map);
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkNftUpgrade(userId, attributeType, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.dealNftUpgrade(attributeType, entity);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void sellGoldMachineAddCoin(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineAddCoin(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    long goldAmount = ParamsUtil.longParmasNotNull(map, "gdAmt");
                    int userId = ParamsUtil.userId(map);
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkSellGoldMachineAddCoin(userId, goldAmount, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.dealSellGoldMachineAddCoin(goldAmount, entity);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void sellGoldMachineRepairDurability(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineRepairDurability(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkSellGoldMachineRepairDurability(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.dealSellGoldMachineRepairDurability(entity);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void sellGoldMachineOperate(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineOperate(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);
                    double salePrice = StrUtil.truncateTwoDecimal(
                            ParamsUtil.doubleParmasNotNull(map, "slPrc"));
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkSellGoldMachineOperate(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.dealSellGoldMachineOperate(salePrice, entity);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void sellGoldMachineStopOperate(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineStopOperate(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkSellGoldMachineStopOperate(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.dealSellGoldMachineStopOperate(entity);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void sellGoldMachineListMarket(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineListMarket(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);
                    long salePrice = ParamsUtil.longParmasNotNull(map, "slPrc");
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkSellGoldMachineListMarket(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.dealSellGoldMachineListMarket(salePrice, entity);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void sellGoldMachineCalcelMarket(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineCancelMarket(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkSellGoldMachineCancelMarket(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.dealSellGoldMachineCancelMarket(entity);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void nftReport(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.nftReport(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            List<NftReportMsg> reportList = new ArrayList<>();
            
            double earnAmount = SellGoldMachineGoldHistoryDao.getInstance().loadDbEarn(nftCode);
            
            List<SellGoldMachineGoldHistoryEntity> list = SellGoldMachineGoldHistoryDao.getInstance().
                    loadDbReport(nftCode, ParamsUtil.pageNum(map), ParamsUtil.pageSize(map));
            if(list.size()>0){
                list.forEach(entity-> reportList.add(SellGoldMachineUtil.initNftReportMsg(entity)));
            }
            dataMap.put("ttAmt", earnAmount);
            dataMap.put("opTbln", list);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }
}
