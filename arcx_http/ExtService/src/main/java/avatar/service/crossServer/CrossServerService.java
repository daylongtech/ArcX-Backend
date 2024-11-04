package avatar.service.crossServer;

import avatar.global.enumMsg.system.ClientCode;
import avatar.net.session.Session;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class CrossServerService {

    /**

     */
    public static void crossServerUserMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        int csUserId = ParamsUtil.intParmas(map, "csUserId");
        if (csUserId > 0) {
            dataMap.put("userMsg", CrossServerMsgUtil.initUserMsg(csUserId));
        }
        
        SendMsgUtil.sendCrossBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }

}
