package avatar.module.user.info;

import avatar.entity.user.info.UserMacMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class UserMacMsgDao {
    private static final UserMacMsgDao instance = new UserMacMsgDao();
    public static final UserMacMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadByMacId(String macId){
        int userId = loadCache(macId);
        if(userId==0){
            
            userId = loadDbByMacId(macId);
            if(userId>0){
                setCache(macId, userId);
            }
        }
        return userId;
    }

    //=========================cache===========================


    /**

     */
    private int loadCache(String mac){
        Object obj = GameData.getCache().get(UserPrefixMsg.USER_MAC_MSG+"_"+mac);
        return obj==null?0:(int) obj;
    }

    /**

     */
    private void setCache(String mac, int userId){
        GameData.getCache().set(UserPrefixMsg.USER_MAC_MSG+"_"+mac, userId);
    }

    /**

     */
    private void removeCache(String mac){
        GameData.getCache().removeCache(UserPrefixMsg.USER_MAC_MSG+"_"+mac);
    }

    //=========================db===========================

    /**

     */
    private int loadDbByMacId(String macId) {
        String sql = "select user_id from user_mac_msg where mac=? and is_register=? ";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{macId, YesOrNoEnum.YES.getCode()});
        return StrUtil.listNum(list)>0?list.get(0):0;
    }

    /**

     */
    public void insert(UserMacMsgEntity entity) {
        boolean flag = GameData.getDB().insert(entity);
        if(flag){
            
            removeCache(entity.getMac());
            
            MacUserListMsgDao.getInstance().remove(entity.getMac());
        }
    }
}
