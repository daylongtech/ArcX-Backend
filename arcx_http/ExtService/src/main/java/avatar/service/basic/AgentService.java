package avatar.service.basic;

import avatar.data.basic.agent.AgentConnectMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.basic.AgentMsgUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class AgentService {
    /**

     */
    public static void agentMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        String userIp = ParamsUtil.ip(map);

        
        List<AgentConnectMsg> agentWebsocketList = new ArrayList<>();
        AgentMsgUtil.loadAgentWebsocketList(userIp, agentWebsocketList, dataMap);
        
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }
}
