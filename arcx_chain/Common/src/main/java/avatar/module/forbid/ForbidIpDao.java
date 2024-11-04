package avatar.module.forbid;

import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**

 */
public class ForbidIpDao {
    private static final ForbidIpDao instance = new ForbidIpDao();
    public static final ForbidIpDao getInstance() {
        return instance;
    }

    /**

     */
    public List<String> loadAll() {
        
        List<String> list = loadCache();
        if (list == null || list.size()==0) {
            
            list = loadDbAll();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<String> loadCache() {
        return (List<String>)
                GameData.getCache().get(PrefixMsg.FORBID_IP);
    }

    /**

     *
     */
    private void setCache(List<String> list) {
        GameData.getCache().set(PrefixMsg.FORBID_IP, list);
    }

    //=========================db===========================

    /**

     */
    private List<String> loadDbAll() {
        String sql = SqlUtil.appointListSql("forbid_ip", "ip", new HashMap<>(),
                Collections.singletonList("create_time")).toString();
        List<String> list = GameData.getDB().listString(sql, new Object[]{});
        if(StrUtil.strListSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }
}
