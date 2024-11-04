package avatar.module.user.token;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class UserAccessTokenDao {
    private static final UserAccessTokenDao instance = new UserAccessTokenDao();
    public static final UserAccessTokenDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadByToken(String token){
        
        int userId = loadCache(token);
        if(userId==0){
            
            userId = loadDbByAccessToken(token);
            if(userId>0){
                
                setCache(token, userId);
            }
        }
        return userId;
    }

    //=========================cache===========================

    /**

     */
    private int loadCache(String token){
        if(GameData.getCache().get(UserPrefixMsg.USER_ACCESS_TOKEN+"_"+token)==null){
            return 0;
        }else{
            return (Integer) GameData.getCache().get(UserPrefixMsg.USER_ACCESS_TOKEN+"_"+token);
        }
    }

    /**

     */
    public void setCache(String token, int userId){
        GameData.getCache().set(UserPrefixMsg.USER_ACCESS_TOKEN+"_"+token, userId);
    }


    //=========================db===========================

    /**

     */
    private int loadDbByAccessToken(String token) {
        String sql = "select user_id from user_token_msg where access_token=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{token});
        return StrUtil.listNum(list);
    }
}
