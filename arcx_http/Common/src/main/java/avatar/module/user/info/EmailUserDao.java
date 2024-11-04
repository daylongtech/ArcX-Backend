package avatar.module.user.info;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class EmailUserDao {
    private static final EmailUserDao instance = new EmailUserDao();
    public static final EmailUserDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadMsg(String email){
        
        int userId = loadCache(email);
        if(userId==-1){
            
            userId = loadDbByEmail(email);
            
            setCache(email, userId);
        }
        return userId;
    }

    //=========================cache===========================

    /**

     */
    private int loadCache(String email){
        Object obj = GameData.getCache().get(UserPrefixMsg.EMAIL_USER+"_"+email);
        return obj==null?-1:(int) obj;
    }

    /**

     */
    public void setCache(String email, int userId){
        GameData.getCache().set(UserPrefixMsg.EMAIL_USER+"_"+email, userId);
    }

    //=========================db===========================

    /**

     */
    private int loadDbByEmail(String email) {
        String sql = "select id from user_info where email=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{email});
        return StrUtil.listNum(list);
    }
}
