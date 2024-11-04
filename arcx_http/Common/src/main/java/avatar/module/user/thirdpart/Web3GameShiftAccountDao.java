package avatar.module.user.thirdpart;

import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**

 */
public class Web3GameShiftAccountDao {
    private static final Web3GameShiftAccountDao instance = new Web3GameShiftAccountDao();
    public static final Web3GameShiftAccountDao getInstance(){
        return instance;
    }

    /**

     */
    public Web3GameShiftAccountEntity loadByMsg(int userId){
        Web3GameShiftAccountEntity entity = loadCache(userId);
        if(entity==null){
            
            entity = loadDbByUserId(userId);
            if(entity!=null) {
                
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================


    /**

     */
    private Web3GameShiftAccountEntity loadCache(int userId){
        return (Web3GameShiftAccountEntity) GameData.getCache().get(UserPrefixMsg.WEB3_GAME_SHIFT_ACCOUNT+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, Web3GameShiftAccountEntity entity){
        GameData.getCache().set(UserPrefixMsg.WEB3_GAME_SHIFT_ACCOUNT+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private Web3GameShiftAccountEntity loadDbByUserId(int userId) {
        String sql = "select * from web3_game_shift_account where user_id=? ";
        return GameData.getDB().get(Web3GameShiftAccountEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    public void insert(Web3GameShiftAccountEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            
            setCache(entity.getUserId(), entity);
        }
    }

    /**

     */
    public void update(Web3GameShiftAccountEntity entity) {
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            
            setCache(entity.getUserId(), entity);
        }
    }

}
