package avatar.module.basic.agent;

import avatar.entity.basic.agent.AgentDefaultMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class AgentDefaultMsgDao {
    private static final AgentDefaultMsgDao instance = new AgentDefaultMsgDao();
    public static final AgentDefaultMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public AgentDefaultMsgEntity loadMsg(){
        AgentDefaultMsgEntity entity = loadCache();
        if(entity==null){
            
            entity = loadDbMsg();
            setCache(entity);
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private AgentDefaultMsgEntity loadCache(){
        return (AgentDefaultMsgEntity)
                GameData.getCache().get(PrefixMsg.AGENT_DEFAULT_MSG);
    }

    /**

     */
    private void setCache(AgentDefaultMsgEntity entity){
        GameData.getCache().set(PrefixMsg.AGENT_DEFAULT_MSG, entity);
    }

    //=========================db===========================

    /**

     */
    private AgentDefaultMsgEntity loadDbMsg() {
        String sql = "select * from agent_default_msg";
        return GameData.getDB().get(AgentDefaultMsgEntity.class, sql, new Object[]{});
    }

}
