package avatar.util.log;

import avatar.entity.log.UserOperateLogEntity;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.log.UserOperateTypeEnum;
import avatar.module.log.UserOperateLogDao;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserPropertyUtil;
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
    public static void costBalance(long num, int userId, int productId, int commodityType, String msg) {
        if (userId != 0 && num != 0) {
            int userOperateType = 0;
            if (commodityType == CommodityTypeEnum.GOLD_COIN.getCode()) {
                
                userOperateType = UserOperateTypeEnum.COST_GOLG_COIN.getCode();
            } else if (commodityType == CommodityTypeEnum.AXC.getCode()) {
                //axc
                userOperateType = UserOperateTypeEnum.COST_AXC.getCode();
            }
            String log = "【" + UserOperateTypeEnum.getNameByCode(userOperateType) + "】";
            
            if (num > 0) {
                log += "【+" + StrUtil.toMoneySize(num) + "】";
            } else {
                log += "【" + StrUtil.toMoneySize(num) + "】";
            }
            if(!StrUtil.checkEmpty(msg)){
                
                log += "【" + msg + "】";
            } else if (productId > 0) {
                
                log += "【" + ProductUtil.productLog(productId) + "】";
            }
            
            long balance = UserBalanceUtil.getUserBalance(userId, commodityType);
            log += "【" + CommodityTypeEnum.getNameByCode(commodityType) + StrUtil.toMoneySize(balance) + "】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**


     */
    public static void settlement(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            
            String log = "【" + UserOperateTypeEnum.SETTLEMENT_GAME.getName() + "】";
            log += "【" + ProductUtil.productLog(productId) + "】";
            log += "【" + CommodityTypeEnum.GOLD_COIN.getName() + StrUtil.toMoneySize(UserBalanceUtil.getUserBalance(userId,
                    CommodityTypeEnum.GOLD_COIN.getCode())) + "】";
            
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, UserOperateTypeEnum.SETTLEMENT_GAME.getCode(),
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**


     */
    public static void startGame(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            String log = "【" + UserOperateTypeEnum.START_GAME.getName() + "】";
            log += "【" + ProductUtil.productLog(productId) + "】";
            if (ProductUtil.isInnoProduct(productId)) {
                

            }
            long userBalance = UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()) + ProductUtil.productCost(productId);

            
            UserOperateLogDao.getInstance().insert(
                    initUserOperateLogEntity(userId, UserOperateTypeEnum.START_GAME.getCode(),
                            log, UserUtil.loadUserIp(userId)));
        }
    }

    /**


     */
    public static void joinProduct(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            String log = "【" + UserOperateTypeEnum.JOIN_PRODUCT.getName() + "】";
            log += "【" + ProductUtil.productLog(productId) + "】";
            
            UserOperateLogDao.getInstance().insert(
                    initUserOperateLogEntity(userId, UserOperateTypeEnum.JOIN_PRODUCT.getCode(),
                            log, UserUtil.loadUserIp(userId)));
        }
    }

    /**


     */
    public static void existProduct(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            String log = "【" + UserOperateTypeEnum.EXIT_PRODUCT.getName() + "】";
            log += "【" + ProductUtil.productLog(productId) + "】";
            
            UserOperateLogDao.getInstance().insert(
                    initUserOperateLogEntity(userId, UserOperateTypeEnum.EXIT_PRODUCT.getCode(),
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
}
