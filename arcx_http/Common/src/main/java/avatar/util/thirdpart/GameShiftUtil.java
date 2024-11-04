package avatar.util.thirdpart;

import avatar.data.thirdpart.Web3WalletMsg;
import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.GameShiftConfigMsg;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.LogUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import com.squareup.okhttp.*;

import java.util.Map;

/**

 */
public class GameShiftUtil {
    /**

     */
    private static String loadDomain(){
        if(CheckUtil.isTestEnv()){
            return GameShiftConfigMsg.localHttpName;
        }else{
            return GameShiftConfigMsg.onlineHttpName;
        }
    }

    /**

     */
    private static boolean register(int userId, String email){
        boolean flag = false;
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"referenceId\":\""+userId+"\",\"email\":\""+email+"\"}");
            Request request = new Request.Builder()
                    .url(loadDomain()+"v2/users")
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .addHeader("content-type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            int code = response.code();
            if(code==200 || code==201){
                flag = true;
            }


        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**

     */
    private static String loadWalletAddress(int userId){
        String address = "";
        try {
            OkHttpClient client = new OkHttpClient();
            String domain = loadDomain()+"users/"+userId+"/wallet-address";
            Request request = new Request.Builder()
                    .url(domain)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code()==200) {
                ResponseBody responseBody = response.body();
                address = responseBody.string();

                        response.code(), address);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return address;
    }

    /**

     */
    public static double getBalance(int userId){
        double balance = 0;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.gameshift.dev/v2/users/"+userId+"/balances")
                    .get()
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .build();

            Response response = client.newCall(request).execute();
            int code = response.code();
            String jsonStr = response.body().string();

            if(code==200){
                
                balance = dealBalanceResponse(jsonStr);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return balance;
    }

    /**

     */
    private static double dealBalanceResponse(String jsonStr) {
        double balance = 0;
        try {
            if (!StrUtil.checkEmpty(jsonStr) && jsonStr.contains("sol")) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if(jsonMap.containsKey("balances")){
                    Map<String, Object> balanceMap = (Map<String, Object>) jsonMap.get("balances");
                    if(balanceMap.containsKey("sol")){
                        balance = StrUtil.truncateNmDecimal(Double.parseDouble(balanceMap.get("sol").toString()), 9);
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return balance;
    }

    /**

     */
    public static void addGameShiftAccount(int userId, String email) {
        
        Web3GameShiftAccountEntity entity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        if(entity==null) {
            
            boolean flag = GameShiftUtil.register(userId, email);
            String wallet;
            if(flag){
                
                wallet = GameShiftUtil.loadWalletAddress(userId);
            }else{

                wallet = GameShiftUtil.loadWalletAddress(userId);
            }
            if (!StrUtil.checkEmpty(wallet)) {
                
                Web3WalletMsg walletMsg = SolanaUtil.createAccount(wallet, SolanaUtil.axcMintPubkey());
                String axcAccount = walletMsg.getAta();
                
                walletMsg = SolanaUtil.createAccount(wallet, SolanaUtil.usdtMintPubkey());
                String usdtAccount = walletMsg.getAta();
                Web3GameShiftAccountDao.getInstance().insert(initWeb3GameShiftAccountEntity(
                        userId, wallet, axcAccount, usdtAccount));
            }
        }
    }

    /**

     */
    private static Web3GameShiftAccountEntity initWeb3GameShiftAccountEntity(int userId, String wallet,
            String axcAccount, String usdtAccount) {
        Web3GameShiftAccountEntity entity = new Web3GameShiftAccountEntity();
        entity.setUserId(userId);
        entity.setWallet(wallet);
        entity.setAxcAccount(axcAccount);
        entity.setUsdtAccount(usdtAccount);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static String signTransaction(int userId, String transaction){
        String url = "";
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,
                    "{\"serializedTransaction\":\""+transaction+"\",\"onBehalfOf\":\""+userId+"\"}");
            Request request = new Request.Builder()
                    .url("https://api.gameshift.dev/transactions/sign")
                    .post(body)
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .addHeader("content-type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            int code = response.code();
            if(code==200 || code==201){
                
                String responseMsg = response.body().string();
                if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("url")){
                    Map<String, Object> responseMap = JsonUtil.strToMap(responseMsg);
                    if(responseMap.containsKey("url")){
                        url = responseMap.get("url").toString();
                    }
                }
            }


        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return url;
    }
}
