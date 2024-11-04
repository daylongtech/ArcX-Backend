package avatar.util.normalProduct;

import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.system.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class SendNormalProductInnerMsgUtil {
    /**

     */
    public static void sendClientMsg(InnerNormalProductClient client, int cmd, int hostId, Map<String, Object> paramsMap) {
        
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("cmd", cmd);
        msgMap.put("userId", hostId);
        msgMap.put("param", paramsMap);
        
        InnerEnCodeUtil.encodeDeal(paramsMap);
        String jsonStr = JsonUtil.mapToJson(msgMap);
        
        client.send(jsonStr);
    }

}
