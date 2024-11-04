package avatar.util.recharge;

import avatar.global.enumMsg.basic.recharge.PayStatusEnum;

/**

 */
public class RechargeUtil {
    /**

     */
    public static String loadSuccessPayStr(){
        
        String payTypeStr = "";
        payTypeStr += (PayStatusEnum.ALREADY_PAY.getCode()+",");
        payTypeStr += (PayStatusEnum.HAND_NO_CALL.getCode()+",");
        payTypeStr += (PayStatusEnum.HAND_AFTER_CALL.getCode());
        return payTypeStr;
    }

}
