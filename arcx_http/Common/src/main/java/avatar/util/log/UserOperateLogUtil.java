package avatar.util.log;

import avatar.entity.log.UserOperateLogEntity;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.log.UserOperateTypeEnum;
import avatar.module.log.UserOperateLogDao;
import avatar.util.basic.CommodityUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserPropertyUtil;
import avatar.util.user.UserUsdtUtil;
import avatar.util.user.UserUtil;

/**

 */
public class UserOperateLogUtil {
    /**

     */
    private static UserOperateLogEntity initUserOperateLogEntity(int userId, int operateType, String log, String ip) {
        UserOperateLogEntity entity = new UserOperateLogEntity();
        entity.setUserId(userId);
        entity.setOperateType(operateType);
        entity.setOperateLog(log);
        entity.setOperateIp(ip);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void register(int userId) {
        String log = "【"+UserOperateTypeEnum.REGISTER.getName()+"】";
        
        UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, UserOperateTypeEnum.REGISTER.getCode(),
                log, UserUtil.loadUserIp(userId)));
    }

    /**




     */
    public static void costBalance(long num, int userId, int productId, int commodityType, String msg){
        if(userId!=0 && num!=0){
            int userOperateType = 0;
            if(commodityType== CommodityUtil.gold()){
                
                userOperateType = UserOperateTypeEnum.COST_GOLG_COIN.getCode();
            }else if(commodityType==CommodityUtil.axc()){
                //axc
                userOperateType = UserOperateTypeEnum.COST_AXC.getCode();
            }
            String log = "【"+UserOperateTypeEnum.getNameByCode(userOperateType)+"】";
            
            if(num>0){
                log += "【+"+StrUtil.toMoneySize(num)+"】";
            }else{
                log += "【"+StrUtil.toMoneySize(num)+"】";
            }
            if(!StrUtil.checkEmpty(msg)){
                
                log += "【"+msg+"】";
            }else if(productId>0){
                
                log += "【"+ ProductUtil.productLog(productId)+"】";
            }
            
            long balance = UserBalanceUtil.getUserBalance(userId, commodityType);
            log += "【"+CommodityTypeEnum.getNameByCode(commodityType)+StrUtil.toMoneySize(balance)+"】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**

     */
    public static void costProperty(long num, int userId, int propertyType, String msg){
        if(userId!=0 && num!=0){
            int userOperateType = UserOperateTypeEnum.COST_PROPERTY.getCode();
            
            String log = "【"+ PropertyTypeEnum.getNameByCode(propertyType)+"】";
            
            if(num>0){
                log += "【+"+ StrUtil.toMoneySize(num)+"】";
            }else{
                log += "【"+StrUtil.toMoneySize(num)+"】";
            }
            if(!StrUtil.checkEmpty(msg)){
                log += "【"+msg+"】";
            }
            
            long balance = UserPropertyUtil.getUserProperty(userId, propertyType);
            log += "【"+PropertyTypeEnum.getNameByCode(propertyType)+StrUtil.toMoneySize(balance)+"】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**

     */
    public static void costUsdt(double num, int userId, String msg){
        if(userId!=0 && num!=0){
            int userOperateType = UserOperateTypeEnum.COST_USDT.getCode();
            String log = "";
            
            if(num>0){
                log += "【+"+ StrUtil.toFourMoneySize(num)+"】";
            }else{
                log += "【"+StrUtil.toFourMoneySize(num)+"】";
            }
            if(!StrUtil.checkEmpty(msg)){
                log += "【"+msg+"】";
            }
            
            double balance = UserUsdtUtil.usdtBalance(userId);
            log += "【"+StrUtil.toFourMoneySize(balance)+"】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**

     */
    public static void sign(int userId) {
        String log = "【"+UserOperateTypeEnum.SIGN.getName()+"】";
        
        UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, UserOperateTypeEnum.SIGN.getCode(),
                log, UserUtil.loadUserIp(userId)));
    }

    /**

     */
    public static void logoutAccount(int userId) {
        String log = "【"+ UserOperateTypeEnum.LOGOUT_ACCOUNT.getName()+"】";
        UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, UserOperateTypeEnum.LOGOUT_ACCOUNT.getCode(),
                log, UserUtil.loadUserIp(userId)));
    }

    /**

     */
    public static void repairProduct(int userId, int productId) {
        String log = "【"+UserOperateTypeEnum.REPAIR_PRODUCT.getName()+"】";
        log += "【"+ ProductUtil.productLog(productId)+"】";
        UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, UserOperateTypeEnum.REPAIR_PRODUCT.getCode(),
                log, UserUtil.loadUserIp(userId)));
    }

}
