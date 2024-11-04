package avatar.util.product;

import avatar.data.product.innoNaPay.InnoNaPayUserStatusMsg;
import avatar.entity.product.innoNaPay.InnoNaPayMoneyConfigEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.module.product.innoNaPay.InnoNaPayMoneyConfigDao;
import avatar.module.product.innoNaPay.InnoNaPayUserStatusDao;
import avatar.module.recharge.commodity.RechargeGoldOrderDao;
import avatar.util.system.TimeUtil;

/**

 */
public class InnoNaPayUtil {
    /**

     */
    public static boolean isPay(int userId){
        InnoNaPayUserStatusMsg msg = InnoNaPayUserStatusDao.getInstance().loadMsg(userId);
        return msg.isPayFlag();
    }

    /**

     */
    public static InnoNaPayUserStatusMsg initInnoNaPayUserStatusMsg(int userId) {
        InnoNaPayUserStatusMsg msg = new InnoNaPayUserStatusMsg();
        msg.setUserId(userId);
        msg.setPayFlag(loadPayFlag(userId));
        msg.setRefreshTime(TimeUtil.getNowTime());
        return msg;
    }

    /**

     */
    private static boolean loadPayFlag(int userId) {
        boolean flag;
        
        InnoNaPayMoneyConfigEntity entity = InnoNaPayMoneyConfigDao.getInstance().loadMsg();
        int timeRange = entity.getTimeRange();
        double money = entity.getMoney();
        double sumMoney = RechargeGoldOrderDao.getInstance().loadDbUserAmount(userId, timeRange);
        if(money>0){
            
            flag = sumMoney>money;
        }else{
            
            flag = sumMoney>0;
        }
        return flag;
    }

    /**

     */
    public static InnoNaPayUserStatusMsg dealInnoNaPayUserStatusMsg(InnoNaPayUserStatusMsg msg) {
        if((TimeUtil.getNowTime()-msg.getRefreshTime())>=ProductConfigMsg.inoNaPayCheckTime) {
            int userId = msg.getUserId();
            msg.setPayFlag(loadPayFlag(userId));
            msg.setRefreshTime(TimeUtil.getNowTime());
            InnoNaPayUserStatusDao.getInstance().setCache(userId, msg);
        }
        return msg;
    }
}
