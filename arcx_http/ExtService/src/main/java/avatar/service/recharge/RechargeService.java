package avatar.service.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.RechargeCheckParamsUtil;
import avatar.util.recharge.RechargePropertyUtil;
import avatar.util.recharge.RechargeUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class RechargeService {

    /**

     */
    public static void shoppingMall(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        int userId = ParamsUtil.userId(map);
        
        RechargeUtil.shoppingMall(userId, dataMap);
        
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }

    /**

     */
    public static void refreshMallProperty(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = CheckParamsUtil.checkAccessToken(map);
        if (ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    status = RechargePropertyUtil.refreshMallProperty(userId);
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
    public static void commodityDirectPurchase(Map<String, Object> map, Session session) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        
        int status = RechargeCheckParamsUtil.commodityDirectPurchase(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.RECHARGE_LOCK+"_"+userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = RechargeUtil.commodityDirectPurchase(map, retList);
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
        
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);

    }
}
