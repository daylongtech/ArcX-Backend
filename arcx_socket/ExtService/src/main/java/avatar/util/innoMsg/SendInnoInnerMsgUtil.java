package avatar.util.innoMsg;

import avatar.util.product.ProductUtil;
import avatar.util.system.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class SendInnoInnerMsgUtil {
    /**

     */
    public static void sendClientMsg(SyncInnoClient client, int cmd, int hostId,
                                     Map<Object, Object> paramsMap) {
        
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("cmd", cmd);
        msgMap.put("userId", hostId);
        msgMap.put("param", paramsMap);
        
        InnerEnCodeUtil.objectEncodeDeal(paramsMap);
        String jsonStr = JsonUtil.mapToJson(msgMap);
        
        client.send(jsonStr);
    }

    /**

     */
    public static void sendClientMsg(int productId, int cmd, Map<Object, Object> paramsMap) {
        String ip = ProductUtil.productIp(productId);
        int port = ProductUtil.productSocketPort(productId);
        String linkMsg = SyncInnoConnectUtil.linkMsg(ip, port);
        
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(ip, port, linkMsg);
        if (client != null && client.isOpen()) {
            int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
            if(hostId>0) {
                
                Map<String, Object> msgMap = new HashMap<>();
                msgMap.put("cmd", cmd);
                msgMap.put("userId", hostId);
                msgMap.put("param", paramsMap);
                
                InnerEnCodeUtil.objectEncodeDeal(paramsMap);
                String jsonStr = JsonUtil.mapToJson(msgMap);
                
                client.send(jsonStr);
            }
        }
    }

}
