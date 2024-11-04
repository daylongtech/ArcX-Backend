package avatar.util.innoMsg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**

 */
public class SyncInnoOperateUtil {
    
    public static ConcurrentMap<String, SyncInnoClient> webSocket = new ConcurrentHashMap<>();

    /**

     */
    public static SyncInnoClient loadClient(String ip, int port, String linkMsg) {
        SyncInnoClient myClient = webSocket.get(linkMsg);
        if(myClient==null){
            
            SyncInnoDealUtil.socketClose(ip, port);
        }
        return myClient;
    }

    /**

     */
    public static SyncInnoClient loadClient(String ip, int port) {
        String linkMsg = SyncInnoConnectUtil.linkMsg(ip, port);
        SyncInnoClient myClient = webSocket.get(linkMsg);
        if(myClient==null || !myClient.isOpen()) {
            try {
                if (SyncInnoConnectUtil.isOutTimeLink(linkMsg)) {
                    
                    myClient = new SyncInnoClient("ws://" + ip + ":" + port + "/websocket");
                    
                    myClient.connect();
                    
                    webSocket.put(linkMsg, myClient);
                }
            } catch (Exception e) {
                myClient = null;
            }
        }
        return myClient;
    }

}
