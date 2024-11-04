package avatar.module.user.info;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class TotalUserListDao {
    private static final TotalUserListDao instance = new TotalUserListDao();
    public static final TotalUserListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<Integer> loadAll(){
        
        List<Integer>  list = loadCache();
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
        return (List<Integer> ) GameData.getCache().get(UserPrefixMsg.TOTAL_USER_LIST);
    }

    /**

     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.TOTAL_USER_LIST, list);
    }

    /**

     */
    public void removeCache(){
        GameData.getCache().removeCache(UserPrefixMsg.TOTAL_USER_LIST);
    }

    //=========================db===========================

    /**

     */
    private List<Integer> loadDbAll() {
        String sql = "select id from user_info where status=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{UserStatusEnum.NORMAL.getCode()});
        return StrUtil.retList(list);
    }
}
