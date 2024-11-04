package avatar.module.crossServer;

import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;
import avatar.util.crossServer.CrossServerMsgUtil;

/**

 */
public class CrossServerUserMsgDao {
    private static final CrossServerUserMsgDao instance = new CrossServerUserMsgDao();
    public static final CrossServerUserMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public GeneralCrossServerUserMsg loadByMsg(int serverSideType, int userId){
        
        GeneralCrossServerUserMsg CrossServerUserMsg = loadCache(serverSideType, userId);
        if(CrossServerUserMsg==null){
            
            CrossServerUserMsg = CrossServerMsgUtil.loadGeneralCrossServerUserMsg(
                    serverSideType,userId);
            
            setCache(serverSideType, userId, CrossServerUserMsg);
        }
        
        CrossServerMsgUtil.dealCrossServerUserMsg(CrossServerUserMsg);
        return CrossServerUserMsg;
    }

    //=========================cache===========================

    /**

     */
    private GeneralCrossServerUserMsg loadCache(int serverSideType, int userId){
        return (GeneralCrossServerUserMsg) GameData.getCache().get(
                CrossServerPrefixMsg.CROSS_SERVER_USER_MSG+"_"+serverSideType+"_"+userId);
    }

    /**

     */
    public void setCache(int serverSideType, int userId, GeneralCrossServerUserMsg msg){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_USER_MSG+"_"+serverSideType+"_"+userId, msg);
    }

}
