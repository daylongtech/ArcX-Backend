package avatar.service.nft;

import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.NftCheckParamsUtil;
import avatar.util.nft.SellGoldMachineUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class SellGoldMachineService {
    /**

     */
    public static void operateSellGoldMachine(Map<String, Object> map, Session session) {
        int status = CheckParamsUtil.checkAccessToken(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            
            String nftCode = SellGoldMachineUtil.loadOperateMachine();
            if (!StrUtil.checkEmpty(nftCode)) {
                SellGoldMachineUtil.dealSellGoldMachineMsg(userId, nftCode, dataMap);
            } else {
                status = ClientCode.NO_SELL_GOLD_MACHINE.getCode();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void exchangeNftGold(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.exchangeNftGold(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.stringParmasNotNull(map, "nftCd");
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    
                    status = SellGoldMachineUtil.checkExchangeNftGold(map, entity);
                    if(ParamsUtil.isSuccess(status)){
                        
                        SellGoldMachineUtil.exchangeNftGold(map, entity);
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
}
