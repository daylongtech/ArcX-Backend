package avatar.module.user.info;

import avatar.entity.user.info.UserInfoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class UserInfoDao {
    private static final UserInfoDao instance = new UserInfoDao();
    public static final UserInfoDao getInstance(){
        return instance;
    }

    /**

     */
    public UserInfoEntity loadByUserId(int userId){
        
        UserInfoEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByUserId(userId);
            
            if(entity!=null){
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private UserInfoEntity loadCache(int userId){
        return (UserInfoEntity) GameData.getCache().get(UserPrefixMsg.USER_INFO+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, UserInfoEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_INFO+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private UserInfoEntity loadDbByUserId(int userId) {
        return GameData.getDB().get(UserInfoEntity.class, userId);
    }

    /**

     */
    public int insert(UserInfoEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            
            TotalUserListDao.getInstance().removeCache();
            if(!StrUtil.checkEmpty(entity.getEmail())){
                
                EmailUserDao.getInstance().setCache(entity.getEmail(), id);
            }
        }
        return id;
    }

    /**

     */
    public boolean update(UserInfoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getId(), entity);
        }
        return flag;
    }

    /**

     */
    public UserInfoEntity loadDbByNickName(String nickName) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("nick_name", nickName);
        String sql = SqlUtil.getSql("user_info", paramsMap).toString();
        return GameData.getDB().get(UserInfoEntity.class, sql, new Object[]{});
    }

    /**

     */
    public List<Integer> loadDbUserList(String nickName) {
        String sql = "select id from user_info where nick_name=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{nickName});
        return StrUtil.retList(list);
    }

    /**

     */
    public List<Integer> loadDbByIp(String forbidIp) {
        String sql = "select id from user_info where ip=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{forbidIp});
        return StrUtil.listNum(list)>0?list:new ArrayList<>();
    }

}
