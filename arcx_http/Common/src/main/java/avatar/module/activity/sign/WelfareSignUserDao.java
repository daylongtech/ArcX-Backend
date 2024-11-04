package avatar.module.activity.sign;

import avatar.entity.activity.sign.user.WelfareSignUserMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.activity.WelfareUtil;

/**

 */
public class WelfareSignUserDao {
    private static final WelfareSignUserDao instance = new WelfareSignUserDao();
    public static final WelfareSignUserDao getInstance(){
        return instance;
    }

    /**

     */
    public WelfareSignUserMsgEntity loadByUserId(int userId){
        
        WelfareSignUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByUserId(userId);
            if(entity==null){
                
                entity = insert(userId);
            }
            
            if(entity!=null){
                setCache(userId, entity);
            }
        }
        if(entity!=null) {
            
            WelfareUtil.dealUserSignMsg(entity);
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private WelfareSignUserMsgEntity loadCache(int userId) {
        return (WelfareSignUserMsgEntity) GameData.getCache().get(ActivityPrefixMsg.WELFARE_SIGN_USER_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, WelfareSignUserMsgEntity entity) {
        GameData.getCache().set(ActivityPrefixMsg.WELFARE_SIGN_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private WelfareSignUserMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from welfare_sign_user_msg where user_id=?";
        return GameData.getDB().get(WelfareSignUserMsgEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    public WelfareSignUserMsgEntity insert(int userId) {
        
        WelfareSignUserMsgEntity entity = WelfareUtil.initWelfareSignUserMsgEntity(userId);
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            
            setCache(userId, entity);
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(WelfareSignUserMsgEntity entity) {
        boolean flag = GameData.getDB().update(entity);
        
        if(flag){
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
