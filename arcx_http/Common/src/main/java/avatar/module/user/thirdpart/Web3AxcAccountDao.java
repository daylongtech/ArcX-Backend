package avatar.module.user.thirdpart;

import avatar.entity.user.thirdpart.Web3AxcAccountEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**

 */
public class Web3AxcAccountDao {
    private static final Web3AxcAccountDao instance = new Web3AxcAccountDao();
    public static final Web3AxcAccountDao getInstance(){
        return instance;
    }

    /**

     */
    public Web3AxcAccountEntity loadByMsg(int userId){
        Web3AxcAccountEntity entity = loadCache(userId);
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
    private Web3AxcAccountEntity loadCache(int userId){
        return (Web3AxcAccountEntity) GameData.getCache().get(UserPrefixMsg.WEB3_AXC_ACCOUNT+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, Web3AxcAccountEntity entity){
        GameData.getCache().set(UserPrefixMsg.WEB3_AXC_ACCOUNT+"_"+userId, entity);
    }

    //=========================db===========================

    /**

     */
    private Web3AxcAccountEntity loadDbByUserId(int userId) {
        String sql = "select * from web3_axc_account where user_id=? ";
        return GameData.getDB().get(Web3AxcAccountEntity.class, sql, new Object[]{userId});
    }

    /**

     */
    public void insert(Web3AxcAccountEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            
            setCache(entity.getUserId(), entity);
        }
    }
}
