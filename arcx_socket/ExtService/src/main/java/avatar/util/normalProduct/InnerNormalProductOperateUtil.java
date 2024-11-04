package avatar.util.normalProduct;

import avatar.util.checkParams.ErrorDealUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**

 */
public class InnerNormalProductOperateUtil {
    
    public static ConcurrentMap<String, InnerNormalProductClient> webSocket = new ConcurrentHashMap<>();

    /**




     */
    public static InnerNormalProductClient loadClient(String ip, int port, String linkMsg) {
        InnerNormalProductClient myClient = webSocket.get(linkMsg);
        if(myClient==null){
            
            InnerNormalProductDealUtil.socketClose(ip, port);
        }
        return myClient;
    }

    /**



     */
    public static InnerNormalProductClient loadClient(String ip, int port) {
        int uId = InnerNormalProductConnectUtil.loadUid(ip, port);
        String linkMsg = ip+port+uId;
        InnerNormalProductClient myClient = webSocket.get(linkMsg);
        if(myClient==null || !myClient.isOpen()) {
            try {
                if (InnerNormalProductConnectUtil.isOutTimeLink(linkMsg)) {
                    
                    myClient = new InnerNormalProductClient("ws://" + ip + ":" + port + "/websocket");
                    
                    myClient.connect();
                    
                    webSocket.put(linkMsg, myClient);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                myClient = null;
            }
        }
        return myClient;
    }
}
