package avatar.util.solana;

import avatar.task.solana.ConnectSolanaWebsocketTask;
import avatar.util.LogUtil;
import avatar.util.trigger.SchedulerSample;

/**

 */
public class SolanaDealUtil {
    /**

     */
    public static void socketOpen(String host, int port) {

        
        SolanaDealUtil.addLogsSubscribe();
    }

    /**

     */
    public static void socketClose() {

        
        SchedulerSample.delayed(3000, new ConnectSolanaWebsocketTask());
    }

    /**

     */
    public static void socketError(String host, int port) {

    }

    /**

     */
    public static void dealMsg(String jsonStr) {
        if(jsonStr.contains("logsNotification")) {

            SolanaSubUtil.logsNotification(jsonStr);
        }
    }

    /**

     */
    public static void addLogsSubscribe() {
        String jsonStr = "{" +
                "  \"jsonrpc\": \"2.0\"," +
                "  \"id\": 1," +
                "  \"method\": \"logsSubscribe\"," +
                "  \"params\": [" +
                "    {\n" +
                "      \"mentions\": [ \"xxAccount\" ]" +
                "    }," +
                "    {" +
                "      \"commitment\": \"finalized\"" +
                "    }" +
                "  ]" +
                "}";
        SolanaClient client = SolanaConnectUtil.webSocket.get("111");
        if(client!=null && client.isOpen()){
            
            jsonStr = jsonStr.replaceAll("xxAccount", SolanaMsgUtil.solSubAcccount());

            
            jsonStr = jsonStr.replaceAll("xxAccount", SolanaMsgUtil.axcSubAcccount());

            
            jsonStr = jsonStr.replaceAll("xxAccount", SolanaMsgUtil.usdtSubAcccount());

            client.send(jsonStr);

        }
    }

}
