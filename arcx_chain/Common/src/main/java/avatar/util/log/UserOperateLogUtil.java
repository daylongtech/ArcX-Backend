package avatar.util.log;


import avatar.entity.log.UserOperateLogEntity;
import avatar.global.enumMsg.basic.CommodityTypeEnum;
import avatar.global.enumMsg.log.UserOperateTypeEnum;
import avatar.module.log.UserOperateLogDao;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserBalanceUtil;
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
    public static void costBalance(long num, int userId, int commodityType, String msg){
        if(userId!=0 && num!=0){
            int userOperateType = 0;
            if(commodityType==CommodityTypeEnum.AXC.getCode()){
                //axc
                userOperateType = UserOperateTypeEnum.COST_AXC.getCode();
            }
            String log = "【"+UserOperateTypeEnum.getNameByCode(userOperateType)+"】";
            
            if(num>0){
                log += "【+"+ StrUtil.toMoneySize(num)+"】";
            }else{
                log += "【"+StrUtil.toMoneySize(num)+"】";
            }
            if(!StrUtil.checkEmpty(msg)){
                
                log += "【"+msg+"】";
            }
            
            long balance = UserBalanceUtil.getUserBalance(userId, commodityType);
            log += "【"+CommodityTypeEnum.getNameByCode(commodityType)+StrUtil.toMoneySize(balance)+"】";
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

}
