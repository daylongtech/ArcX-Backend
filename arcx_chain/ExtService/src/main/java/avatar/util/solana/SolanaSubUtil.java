package avatar.util.solana;

import avatar.util.LogUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**

 */
public class SolanaSubUtil {
    /**

     */
    public static void logsNotification(String jsonStr) {
        String signature = "";
        if(jsonStr.contains("signature")){
            Map<String, Object> paramsMap = (Map<String, Object>) (JsonUtil.strToMap(jsonStr)).get("params");
            if(paramsMap.containsKey("result")){
                Map<String, Object> resultMap = (Map<String, Object>) paramsMap.get("result");
                if(resultMap.containsKey("value")){
                    Map<String, Object> valueMap = (Map<String, Object>) resultMap.get("value");
                    if(valueMap.containsKey("signature")){
                        signature = valueMap.get("signature").toString();
                    }
                }
            }
        }

        if(!StrUtil.checkEmpty(signature)){
            
            if(!SolanaRequestUtil.repeatDescribe(signature)) {
                SolanaRequestUtil.loadTransaction(signature);
            }
        }
    }
}
