package avatar.module.user.online;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class UserOnlineMsgDao {
    private static final UserOnlineMsgDao instance = new UserOnlineMsgDao();
    public static final UserOnlineMsgDao getInstance(){
        return instance;
    }

    /**

     */
    public List<UserOnlineMsgEntity> loadByUserId(int userId){
        List<UserOnlineMsgEntity> list = loadCache(userId);
        if(list==null){
            
            list = loadDbByUserId(userId);
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<UserOnlineMsgEntity> loadCache(int userId){
        return (List<UserOnlineMsgEntity>)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, List<UserOnlineMsgEntity> list){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId, list);
    }

    /**

     */
    private void removeCache(int userId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId);
    }

    //=========================db===========================

    /**

     */
    private List<UserOnlineMsgEntity> loadDbByUserId(int userId) {
        String sql = "select * from user_online_msg where user_id=? order by create_time desc";
        List<UserOnlineMsgEntity> list = GameData.getDB().list(UserOnlineMsgEntity.class, sql, new Object[]{userId});
        return list==null?new ArrayList<>():list;
    }

    /**

     */
    public UserOnlineMsgEntity insert(UserOnlineMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            removeCache(entity.getUserId());
            
            UserOnlineListDao.getInstance().removeCache();
            int productId = entity.getProductId();
            
            if(productId>0){
                UserProductOnlineListDao.getInstance().removeCache(productId);
            }
            return entity;
        }else{
            return null;
        }
    }

    /**

     */
    public boolean update(int oriProductId, UserOnlineMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            removeCache(entity.getUserId());
            int productId = entity.getProductId();
            
            if(productId>0){
                UserProductOnlineListDao.getInstance().removeCache(productId);
            }
            if(oriProductId>0){
                UserProductOnlineListDao.getInstance().removeCache(oriProductId);
            }
        }
        return flag;
    }

    /**

     */
    public void delete(UserOnlineMsgEntity entity){
        boolean flag = GameData.getDB().delete(entity);
        if (flag) {
            
            removeCache(entity.getUserId());
            
            UserOnlineListDao.getInstance().removeCache();
            int productId = entity.getProductId();
            
            if(productId>0){
                UserProductOnlineListDao.getInstance().removeCache(productId);
            }
            
            SqlUtil.resetAutoId("user_online_msg");
        }
    }

    /**

     */
    public void delete(int userId, int productId){

        
        List<UserOnlineMsgEntity> list = loadByUserId(userId);
        if(list.size()>0) {
            list.forEach(entity->{
                if(entity.getProductId()==productId) {
                    boolean flag = GameData.getDB().delete(entity);
                    if (flag) {
                        
                        removeCache(entity.getUserId());
                        
                        UserOnlineListDao.getInstance().removeCache();
                        
                        if (productId > 0) {
                            UserProductOnlineListDao.getInstance().removeCache(productId);
                        }
                        
                        SqlUtil.resetAutoId("user_online_msg");
                    }
                }
            });
        }
    }

    /**

     */
    public List<UserOnlineMsgEntity> loadDbAll() {
        String sql = SqlUtil.loadList("user_online_msg", Collections.singletonList("create_time")).toString();
        return GameData.getDB().list(UserOnlineMsgEntity.class, sql, new Object[]{});
    }
}
