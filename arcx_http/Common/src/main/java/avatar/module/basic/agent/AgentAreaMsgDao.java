package avatar.module.basic.agent;

import avatar.entity.basic.agent.AgentAreaMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class AgentAreaMsgDao {
    private static final AgentAreaMsgDao instance = new AgentAreaMsgDao();
    public static final AgentAreaMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public AgentAreaMsgEntity loadByArea(int areaId) {
        
        AgentAreaMsgEntity entity = loadCache(areaId);
        if(entity==null){
            
            entity = loadDbById(areaId);
            if(entity!=null) {
                
                setCache(areaId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private AgentAreaMsgEntity loadCache(int areaId){
        return (AgentAreaMsgEntity) GameData.getCache().get(PrefixMsg.AGENT_AREA_MSG+"_"+areaId);
    }

    /**

     */
    private void setCache(int areaId, AgentAreaMsgEntity entity){
        GameData.getCache().set(PrefixMsg.AGENT_AREA_MSG+"_"+areaId, entity);
    }

    //=========================db===========================

    /**

     */
    private AgentAreaMsgEntity loadDbById(int id) {
        return GameData.getDB().get(AgentAreaMsgEntity.class, id);
    }

}
