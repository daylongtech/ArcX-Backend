package avatar.module.crossServer;

import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class CrossServerListDao {
    private static final CrossServerListDao instance = new CrossServerListDao();
    public static final CrossServerListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadAll(){
        
        List<Integer> list = loadCache();
        if(list==null){
            
            list = loadDbAll();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache(){
        return (List<Integer>) GameData.getCache().get(CrossServerPrefixMsg.CROSS_SERVER_LIST);
    }

    /**

     */
    public void setCache(List<Integer> list){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_LIST, list);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbAll() {
        String sql = "select server_type from cross_server_domain";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
