package avatar.util.solana;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**

 */
public class SolanaConnectUtil {
    
    public static ConcurrentMap<String, SolanaClient> webSocket = new ConcurrentHashMap<>();

    /**

     */
    public static void connectSolanaServer() {
        try {
            
            SolanaClient myClient = new SolanaClient("ws://3.90.29.127:8900");
            
            myClient.connect();
            
            webSocket.put("111", myClient);
        }catch (Exception e){

        }
    }
}
