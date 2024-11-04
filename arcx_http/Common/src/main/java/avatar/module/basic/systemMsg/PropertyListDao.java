package avatar.module.basic.systemMsg;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class PropertyListDao {
    private static final PropertyListDao instance = new PropertyListDao();
    public static final PropertyListDao getInstance() {
        return instance;
    }

    /**

     */
    public List<Integer> loadMsg() {
        
        List<Integer> list = loadCache();
        if (list==null) {
            
            list = loadDbMsg();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<Integer> loadCache() {
        return (List<Integer>) GameData.getCache().get(PrefixMsg.PROPERTY_LIST);
    }

    /**

     *
     */
    private void setCache(List<Integer> list) {
        GameData.getCache().set(PrefixMsg.PROPERTY_LIST, list);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbMsg() {
        String sql = "select property_type from property_msg where active_flag=? order by sequence,property_type";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{YesOrNoEnum.YES.getCode()});
        return StrUtil.retList(list);
    }
}
