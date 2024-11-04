package avatar.module.basic.agent;

import avatar.data.basic.agent.AgentConnectMsg;
import avatar.entity.basic.agent.AgentAreaMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.basic.AgentMsgUtil;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class AgentConnectListDao {
    private static final AgentConnectListDao instance = new AgentConnectListDao();
    public static final AgentConnectListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<AgentConnectMsg> loadMsg() {
        
        List<AgentConnectMsg> list = loadCache();
        if(list==null){
            
            list = loadDbAll();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<AgentConnectMsg> loadCache(){
        return (List<AgentConnectMsg>) GameData.getCache().get(PrefixMsg.AGENT_CONNECT_LIST);
    }

    /**

     */
    private void setCache(List<AgentConnectMsg> list){
        GameData.getCache().set(PrefixMsg.AGENT_CONNECT_LIST, list);
    }


    //=========================db===========================

    /**

     */
    private List<AgentConnectMsg> loadDbAll() {
        List<AgentConnectMsg> retList = new ArrayList<>();
        String sql = "select * from agent_area_msg";
        List<AgentAreaMsgEntity> list = GameData.getDB().list(AgentAreaMsgEntity.class, sql, new Object[]{});
        
        AgentMsgUtil.initAgentConnectMsg(list, retList);
        return retList;
    }
}
