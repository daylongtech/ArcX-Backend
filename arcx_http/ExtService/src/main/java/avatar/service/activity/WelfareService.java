package avatar.service.activity;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.activity.WelfareUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class WelfareService {
    /**

     */
    public static void signMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            int userId = ParamsUtil.userId(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.WELFARE_AWARD_LOCK+"_"+userId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    WelfareUtil.signMsg(userId, dataMap);
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void receiveSignAward(Map<String, Object> map, Session session) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.WELFARE_AWARD_LOCK+"_"+userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = WelfareUtil.receiveSignBonus(userId, retList);
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
