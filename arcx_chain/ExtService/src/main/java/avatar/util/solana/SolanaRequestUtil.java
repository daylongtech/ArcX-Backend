package avatar.util.solana;

import avatar.global.enumMsg.system.TokensTypeEnum;
import avatar.global.linkMsg.http.SolanaHttpCmdName;
import avatar.global.lockMsg.LockMsg;
import avatar.module.solana.SolanaSignMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class SolanaRequestUtil {
    /**

     */
    public static boolean repeatDescribe(String signature){
        boolean flag = true;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK,
                2000);
        try {
            if (lock.lock()) {
                flag = SolanaSignMsgDao.getInstance().loadDbBySign(signature);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return flag;
    }


    /**

     */
    public static void loadTransaction(String signature){
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("signature", signature);
        String jsonMsg = JsonUtil.mapToJson(paramsMap);
        String responseMsg = HttpClientUtil.sendHttpPostJson(SolanaMsgUtil.domainName() +
                SolanaHttpCmdName.LOAD_TRANSACTION, jsonMsg);

        
        balanceChange(responseMsg);
    }

    /**

     */
    private static void balanceChange(String jsonStr){
        long transferNum = 0;
        String accountMsg = "";
        String receiveAccountMsg = "";
        try {
            if (jsonStr.contains("postBalances") && jsonStr.contains("preBalances") &&
                    jsonStr.contains("accountKeys")) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                Map<String, Object> metaMap = (Map<String, Object>) jsonMap.get("meta");
                
                JSONArray postBalancesArr = (JSONArray) metaMap.get("postBalances");
                
                JSONArray preBalancesArr = (JSONArray) metaMap.get("preBalances");
                transferNum = Long.parseLong(postBalancesArr.get(1).toString())-Long.parseLong(preBalancesArr.get(1).toString());
                
                Map<String, Object> transactionMap = (Map<String, Object>) jsonMap.get("transaction");
                Map<String, Object> messageMap = (Map<String, Object>) transactionMap.get("message");
                JSONArray accountArr = (JSONArray) messageMap.get("accountKeys");
                accountMsg = accountArr.getString(0);
                receiveAccountMsg = accountArr.getString(1);
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
        }
        if(!StrUtil.checkEmpty(accountMsg)){
            double realNum = StrUtil.truncateNineDecimal(transferNum*1.0/1000000000);
            int accountType = loadAccountType(receiveAccountMsg);
            if(accountType==TokensTypeEnum.AXC.getCode()){
                
                realNum = transferNum;
            }

                    accountMsg, realNum);
            
            SolanaUtil.dealUserBalance(accountType, accountMsg, realNum);
        }
    }

    /**

     */
    private static int loadAccountType(String receiveAccountMsg) {
        int accountType = 0;
        if(receiveAccountMsg.equals(SolanaMsgUtil.solSubAcccount())){
            //solana
            accountType = TokensTypeEnum.SOLANA.getCode();
        }else if(receiveAccountMsg.equals(SolanaMsgUtil.axcSubAcccount())){
            //axc
            accountType = TokensTypeEnum.AXC.getCode();
        } else if(receiveAccountMsg.equals(SolanaMsgUtil.usdtSubAcccount())){
            //usdt
            accountType = TokensTypeEnum.USDT.getCode();
        }
        return accountType;
    }

}
