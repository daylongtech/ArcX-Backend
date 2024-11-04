package avatar.service.user;

import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.WalletConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.net.session.Session;
import avatar.util.basic.CommodityUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.UserCheckParamsUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.GameShiftUtil;
import avatar.util.thirdpart.SolanaUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUsdtUtil;
import avatar.util.user.WalletUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class WalletService {
    /**

     */
    public static void walletSpending(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            
            dataMap.put("usdt", UserUsdtUtil.usdtBalance(userId));
            
            dataMap.put("axc", UserBalanceUtil.getUserBalance(userId, CommodityUtil.axc()));
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void chainWallet(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.chainWallet(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            
            Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
            dataMap.put("wlAds", accountEntity.getWallet());
            
            dataMap.put("usdt", SolanaUtil.accountBalance(accountEntity.getUsdtAccount()));
            
            dataMap.put("axc", SolanaUtil.accountBalance(accountEntity.getAxcAccount()));
            
            dataMap.put("sol", GameShiftUtil.getBalance(userId));
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void walletWithdraw(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.walletWithdraw(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");
            int amount = ParamsUtil.intParmasNotNull(map, "amt");
            
            boolean flag = UserCostLogUtil.costWalletWithdraw(userId, tokenType, amount);
            if(flag) {
                
                WalletUtil.walletWithdraw(tokenType, amount, userId);
            }else{
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void walletRecharge(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.walletRecharge(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");
            int amount = ParamsUtil.intParmasNotNull(map, "amt");
            dataMap.put("url", WalletUtil.walletRecharge(tokenType, amount, userId));
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void transferTokens(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.transferTokens(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");
            double amount = StrUtil.truncateNmDecimal(ParamsUtil.doubleParmasNotNull(map, "amt"), 9);
            String address = ParamsUtil.stringParmasNotNull(map, "ads");
            String tokenAccount = WalletUtil.loadTransferTokens(tokenType, address);
            if(!StrUtil.checkEmpty(tokenAccount)){
                dataMap.put("url", WalletUtil.transferTokens(tokenType, amount, userId, tokenAccount));
            }else{
                status = ClientCode.FAIL.getCode();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void walletConfig(Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("minIfo", WalletUtil.minInfo());
        dataMap.put("maxIfo", WalletUtil.maxIfo());
        dataMap.put("feeIfo", WalletUtil.feeIfo());
        dataMap.put("exFee", WalletConfigMsg.extraFee);
        
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }
}
