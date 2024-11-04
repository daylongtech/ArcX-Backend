package avatar.module.crossServer;

import avatar.entity.crossServer.CrossServerDomainEntity;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;

/**

 */
public class CrossServerDomainDao {
    private static final CrossServerDomainDao instance = new CrossServerDomainDao();
    public static final CrossServerDomainDao getInstance(){
        return instance;
    }

    /**

     */
    public CrossServerDomainEntity loadByMsg(int serverSideType){
        
        CrossServerDomainEntity entity = loadCache(serverSideType);
        if(entity==null){
            
            entity = loadDbByMsg(serverSideType);
            if(entity!=null) {
                
                setCache(serverSideType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private CrossServerDomainEntity loadCache(int serverSideType){
        return (CrossServerDomainEntity) GameData.getCache().get(
                CrossServerPrefixMsg.CROSS_SERVER_DOMAIN+"_"+serverSideType);
    }

    /**

     */
    public void setCache(int serverSideType, CrossServerDomainEntity msg){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_DOMAIN+"_"+serverSideType, msg);
    }

    //=========================db===========================

    /**

     */
    private CrossServerDomainEntity loadDbByMsg(int serverSideType) {
        String sql = "select * from cross_server_domain where server_type=?";
        return GameData.getDB().get(CrossServerDomainEntity.class, sql, new Object[]{serverSideType});
    }
}
