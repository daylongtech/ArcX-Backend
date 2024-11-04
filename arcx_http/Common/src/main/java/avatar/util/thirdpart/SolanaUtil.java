package avatar.util.thirdpart;

import avatar.data.thirdpart.WalletAccountMsg;
import avatar.data.thirdpart.Web3WalletMsg;
import avatar.entity.user.thirdpart.Web3AxcAccountEntity;
import avatar.global.basicConfig.basic.LocalSolanaConfigMsg;
import avatar.global.basicConfig.basic.OnlineSolanaConfigMsg;
import avatar.global.linkMsg.SolanaHttpCmdName;
import avatar.module.user.thirdpart.Web3AxcAccountDao;
import avatar.util.LogUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class SolanaUtil {
    /**

     */
    public static String axcMintPubkey(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.axcMintPubkey;
        }else{
            
            return OnlineSolanaConfigMsg.axcMintPubkey;
        }
    }

    /**

     */
    public static String usdtMintPubkey(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.usdtMintPubkey;
        }else{
            
            return OnlineSolanaConfigMsg.usdtMintPubkey;
        }
    }

    /**

     */
    private static String transferToken(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.transferToken;
        }else{
            
            return OnlineSolanaConfigMsg.transferToken;
        }
    }

    /**

     */
    private static String feePayer(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.feePayer;
        }else{
            
            return OnlineSolanaConfigMsg.feePayer;
        }
    }

    /**

     */
    private static String domainName(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.domain;
        }else{
            
            return OnlineSolanaConfigMsg.domain;
        }
    }

    /**

     */
    private static String axcCompanyAccount(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.axcCompantAccount;
        }else{
            
            return OnlineSolanaConfigMsg.axcCompantAccount;
        }
    }

    /**

     */
    private static String usdtCompanyAccount(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.usdtCompantAccount;
        }else{
            
            return OnlineSolanaConfigMsg.usdtCompantAccount;
        }
    }

    /**

     */
    private static String companySk(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.compantSk;
        }else{
            
            return OnlineSolanaConfigMsg.compantSk;
        }
    }

    /**



     */
    public static Web3WalletMsg createAccount(String walletAccount, String minPubkey){
        Web3WalletMsg walletMsg = new Web3WalletMsg();
        Map<String, String> paramsMap = new HashMap<>();
        if(!StrUtil.checkEmpty(walletAccount)){
            paramsMap.put("gameShiftWallet", walletAccount);
        }
        paramsMap.put("mintPubkey", minPubkey);
        paramsMap.put("feePayer", feePayer());
        String jsonMsg = JsonUtil.mapToJson(paramsMap);
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName()+
                SolanaHttpCmdName.CREATE_ARCX_ACCOUNT, jsonMsg);

        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("ATA")){
            if(responseMsg.contains("account")){
                
                dealCreateAxcAccount(responseMsg, walletMsg);

                        walletMsg.getAta(), walletMsg.getAccount().getPk(), walletMsg.getAccount().getSk());
            }else{
                
                walletMsg.setAta(dealCreateGameShiftAxcAccount(responseMsg));

                        walletAccount, walletMsg.getAta());
            }
        }else{

        }
        return walletMsg;
    }

    /**

     */
    private static void dealCreateAxcAccount(String responseMsg, Web3WalletMsg walletMsg) {
        if(responseMsg.contains("data")){
            Map<String, Object> resonseMap = JsonUtil.strToMap(responseMsg);
            if(resonseMap!=null && resonseMap.size()>0 && resonseMap.containsKey("data")){
                Map<String, Object> dataMap = JsonUtil.strToMap(resonseMap.get("data").toString());
                if(dataMap!=null && dataMap.size()>0) {
                    
                    if (dataMap.containsKey("ATA")) {
                        walletMsg.setAta(dataMap.get("ATA").toString());
                    } else {

                    }
                    
                    if (dataMap.containsKey("account")) {
                        WalletAccountMsg accountMsg = new WalletAccountMsg();
                        Map<String, Object> accountMap = JsonUtil.strToMap(dataMap.get("account").toString());
                        
                        if(accountMap.containsKey("pk")){
                            accountMsg.setPk(accountMap.get("pk").toString());
                        }else {

                        }
                        
                        if(accountMap.containsKey("sk")){
                            accountMsg.setSk(accountMap.get("sk").toString());
                        }else {

                        }
                        walletMsg.setAccount(accountMsg);
                    } else {

                    }
                }else{

                }
            }else{

            }
        }else{

        }
    }

    /**


     */
    public static double accountBalance(String tokenAccount){
        double amount = -1;
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("tokenAccount", tokenAccount);
        String jsonMsg = JsonUtil.mapToJson(paramsMap);
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName()+
                SolanaHttpCmdName.ARCX_ACCOUNT_BALANCE, jsonMsg);

        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("amount")){
            amount = dealAxcAccountAmount(responseMsg);
        }

        return amount==-1?0:amount;
    }

    /**

     */
    private static double dealAxcAccountAmount(String responseMsg) {
        double retAmount = -1;
        if(responseMsg.contains("value")){
            Map<String, Object> dataMap = JsonUtil.strToMap(responseMsg);
            if(dataMap!=null && dataMap.size()>0 && dataMap.containsKey("value")){
                Map<String, Object> valueMap = JsonUtil.strToMap(dataMap.get("value").toString());
                if(valueMap!=null && valueMap.size()>0 && valueMap.containsKey("uiAmountString")){
                    retAmount = Double.parseDouble(valueMap.get("uiAmountString").toString());
                }else{

                }

//                if(valueMap!=null && valueMap.size()>0 && valueMap.containsKey("decimals")){
//                    decimals = Integer.parseInt(valueMap.get("decimals").toString());
//                }else{

//                }

//                if(valueMap!=null && valueMap.size()>0 && valueMap.containsKey("uiAmountString")){
//                    uiAmountString = valueMap.get("uiAmountString").toString();
//                }else{

//                }
//                if(decimals>0 && !"0".equals(uiAmountString)){
//                    retAmount += (Double.parseDouble(uiAmountString));
//                    retAmount = StrUtil.truncateNmDecimal(retAmount, decimals);
//                }
            }else{

            }
        }else{

        }
        return retAmount;
    }

    /**

     */
    public static void costAxcBalance(int userId, int costNum){
        
        Web3AxcAccountEntity entity = Web3AxcAccountDao.getInstance().loadByMsg(userId);
        if(entity!=null) {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("publicKey", entity.getAccountSk());
            paramsMap.put("tokenAccount1Pubket", entity.getAccountPk());
            paramsMap.put("tokenAccount2Pubket", axcCompanyAccount());
            paramsMap.put("mintPubkey", transferToken());
            paramsMap.put("amount", costNum+"");
            paramsMap.put("decimals", "0");
            String jsonMsg = JsonUtil.mapToJson(paramsMap);
            String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                    SolanaHttpCmdName.ARCX_TRANSFER, jsonMsg);

        }else{

        }
    }

    /**

     */
    public static boolean transferAxcBalance(int userId, int costNum, String account){
        boolean flag = false;
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", companySk());
        paramsMap.put("tokenAccount1Pubket", axcCompanyAccount());
        paramsMap.put("tokenAccount2Pubket", account);
        paramsMap.put("mintPubkey", axcMintPubkey());
        paramsMap.put("amount", costNum+"");
        paramsMap.put("decimals", "0");
        String jsonMsg = JsonUtil.mapToJson(paramsMap);
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER, jsonMsg);

        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("message")){
            flag = loadTransferResult(responseMsg);
        }
        return flag;
    }

    /**

     */
    private static boolean loadTransferResult(String responseMsg) {
        boolean flag = false;
        Map<String, Object> responseMap = JsonUtil.strToMap(responseMsg);
        if(responseMap.containsKey("message")){
            String result = responseMap.get("message").toString();
            flag = result.equalsIgnoreCase("success");
        }
        return flag;
    }

    /**

     */
    public static boolean transferUsdtBalance(int userId, int costNum, String account){
        boolean flag = false;
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", companySk());
        paramsMap.put("tokenAccount1Pubket", usdtCompanyAccount());
        paramsMap.put("tokenAccount2Pubket", account);
        paramsMap.put("mintPubkey", usdtMintPubkey());
        paramsMap.put("amount", costNum+"");
        paramsMap.put("decimals", "0");
        String jsonMsg = JsonUtil.mapToJson(paramsMap);
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER, jsonMsg);

        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("message")){
            flag = loadTransferResult(responseMsg);
        }
        return flag;
    }

    /**

     */
    private static String dealCreateGameShiftAxcAccount(String responseMsg) {
        String ata = "";
        if(responseMsg.contains("data")){
            Map<String, Object> resonseMap = JsonUtil.strToMap(responseMsg);
            if(resonseMap!=null && resonseMap.size()>0 && resonseMap.containsKey("data")){
                Map<String, Object> dataMap = JsonUtil.strToMap(resonseMap.get("data").toString());
                if(dataMap!=null && dataMap.size()>0) {
                    
                    if (dataMap.containsKey("ATA")) {
                        ata = dataMap.get("ATA").toString();
                    } else {

                    }
                }else{

                }
            }else{

            }
        }else{

        }
        return ata;
    }

    /**

     */
    public static String loadAxcTransferTransaction(int userId, int costNum, int decimal, String wallet,
            String account, String toAccount){
        String txHash = "";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", wallet);
        paramsMap.put("tokenAccountXPubkey", account);
        paramsMap.put("tokenAccountYPubkey", StrUtil.checkEmpty(toAccount)?axcCompanyAccount():toAccount);
        paramsMap.put("mintPubkey", axcMintPubkey());
        paramsMap.put("amount", costNum+"");
        paramsMap.put("decimals", decimal+"");
        String jsonMsg = JsonUtil.mapToJson(paramsMap);
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER_TRANSACTION, jsonMsg);

        if(responseMsg!=null && responseMsg.contains("txhash")){
            Map<String, Object> jsonMap = JsonUtil.strToMap(responseMsg);
            if(jsonMap.containsKey("txhash")){
                txHash = jsonMap.get("txhash").toString();
            }
        }
        return txHash;
    }

    /**

     */
    public static String loadUsdtTransferTransaction(int userId, int costNum, int decimal, String wallet,
            String account, String toAccount){
        String txHash = "";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", wallet);
        paramsMap.put("tokenAccountXPubkey", account);
        paramsMap.put("tokenAccountYPubkey", StrUtil.checkEmpty(toAccount)?usdtCompanyAccount():toAccount);
        paramsMap.put("mintPubkey", usdtMintPubkey());
        paramsMap.put("amount", costNum+"");
        paramsMap.put("decimals", decimal+"");
        String jsonMsg = JsonUtil.mapToJson(paramsMap);
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER_TRANSACTION, jsonMsg);

        if(responseMsg!=null && responseMsg.contains("txhash")){
            Map<String, Object> jsonMap = JsonUtil.strToMap(responseMsg);
            if(jsonMap.containsKey("txhash")){
                txHash = jsonMap.get("txhash").toString();
            }
        }
        return txHash;
    }
}
