package avatar.module.basic.agent;

import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class AgentNationAreaMsgDao {
    private static final AgentNationAreaMsgDao instance = new AgentNationAreaMsgDao();
    public static final AgentNationAreaMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadByNation(String nation) {
        
        int areaId = loadCache(nation);
        if(areaId==-1){
            
            areaId = loadDbByNation(nation);
            
            setCache(nation, areaId);
        }
        return areaId;
    }

    //=========================cache===========================

    /**

     */
    private int loadCache(String nation){
        Object obj = GameData.getCache().get(PrefixMsg.AGENT_NATION_AREA_MSG+"_"+nation);
        return obj==null?-1:((int) obj);
    }

    /**

     */
    private void setCache(String nation, int areaId){
        GameData.getCache().set(PrefixMsg.AGENT_NATION_AREA_MSG+"_"+nation, areaId);
    }

    //=========================db===========================

    /**

     */
    private int loadDbByNation(String nation) {
        String sql = "select area_id from agent_area_nation_msg where nation_name=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{nation});
        return StrUtil.listSize(list)>0?list.get(0):0;
    }

}
