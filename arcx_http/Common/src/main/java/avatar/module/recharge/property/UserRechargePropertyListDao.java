package avatar.module.recharge.property;

import avatar.data.recharge.UserRechargePropertyMsg;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.recharge.RechargePropertyUtil;

/**

 */
public class UserRechargePropertyListDao {
    private static final UserRechargePropertyListDao instance = new UserRechargePropertyListDao();
    public static final UserRechargePropertyListDao getInstance(){
        return instance;
    }

    /**

     */
    public UserRechargePropertyMsg loadMsg(int userId) {
        
        UserRechargePropertyMsg msg = loadCache(userId);
        if(msg==null){
            msg = RechargePropertyUtil.initUserRechargePropertyMsg(userId);
            
            setCache(userId, msg);
        }
        if(msg.getPropertyList().size()>0){
            
            RechargePropertyUtil.dealUserRetPropertyMsg(msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private UserRechargePropertyMsg loadCache(int userId){
        return (UserRechargePropertyMsg) GameData.getCache().get(RechargePrefixMsg.USER_RECHARGE_PROPERTY_LIST+"_"+userId);
    }

    /**

     */
    public void setCache(int userId, UserRechargePropertyMsg msg){
        GameData.getCache().set(RechargePrefixMsg.USER_RECHARGE_PROPERTY_LIST+"_"+userId, msg);
    }
}
