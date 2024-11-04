package avatar.module.user.thirdpart;

import avatar.entity.user.thirdpart.UserThirdPartUidMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class UserThirdPartUidMsgDao {
    private static final UserThirdPartUidMsgDao instance = new UserThirdPartUidMsgDao();
    public static final UserThirdPartUidMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadByUid(String uid){
        int userId = loadCache(uid);
        if(userId==0){
            
            userId = loadDbByUid(uid);
            if(userId>0){
                setCache(uid, userId);
            }
        }
        return userId;
    }

    //=========================cache===========================


    /**

     */
    private int loadCache(String uid){
        Object obj = GameData.getCache().get(UserPrefixMsg.USER_UID_MSG+"_"+uid);
        return obj==null?0:(int) obj;
    }

    /**

     */
    private void setCache(String uid, int userId){
        GameData.getCache().set(UserPrefixMsg.USER_UID_MSG+"_"+uid, userId);
    }

    //=========================db===========================

    /**

     */
    private int loadDbByUid(String uid) {
        String sql = "select user_id from user_third_part_uid_msg where uid=? ";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{uid});
        return StrUtil.listNum(list)>0?list.get(0):0;
    }

    /**

     */
    public void insert(UserThirdPartUidMsgEntity entity) {
        boolean flag = GameData.getDB().insert(entity);
        if(flag){
            
            setCache(entity.getUid(), entity.getUserId());
        }
    }
}
