package avatar.module.crossServer;

import avatar.entity.crossServer.CrossServerMediaPrefixEntity;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;

/**

 */
public class CrossServerMediaPrefixDao {
    private static final CrossServerMediaPrefixDao instance = new CrossServerMediaPrefixDao();
    public static final CrossServerMediaPrefixDao getInstance(){
        return instance;
    }

    /**

     */
    public CrossServerMediaPrefixEntity loadByServerType(int serverType){
        
        CrossServerMediaPrefixEntity entity = loadCache(serverType);
        if(entity==null){
            
            entity = loadDbByServerType(serverType);
            if(entity!=null) {
                
                setCache(serverType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private CrossServerMediaPrefixEntity loadCache(int serverType){
        return (CrossServerMediaPrefixEntity) GameData.getCache().get(CrossServerPrefixMsg.CROSS_SERVER_MEDIA_PREFIX+"_"+serverType);
    }

    /**

     */
    public void setCache(int serverType, CrossServerMediaPrefixEntity msg){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_MEDIA_PREFIX+"_"+serverType, msg);
    }

    //=========================db===========================

    /**

     */
    private CrossServerMediaPrefixEntity loadDbByServerType(int serverType) {
        String sql = "select * from cross_server_media_prefix where server_type=?";
        return GameData.getDB().get(CrossServerMediaPrefixEntity.class, sql, new Object[]{serverType});
    }
}
