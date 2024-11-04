package avatar.util.checkParams;

import avatar.global.enumMsg.basic.commodity.RechargeCommodityTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.util.recharge.RechargeUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**

 */
public class RechargeCheckParamsUtil {
    /**

     */
    public static int commodityDirectPurchase(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int userId = ParamsUtil.userId(map);
                int rechargeType = ParamsUtil.intParmasNotNull(map, "rcgCmdTp");
                int commodityId = ParamsUtil.intParmas(map, "cmdId");
                if(StrUtil.checkEmpty(RechargeCommodityTypeEnum.getNameByCode(rechargeType))){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(RechargeUtil.commodityCheckEmpty(userId, rechargeType, commodityId)){
                    status = ClientCode.INVALID_COMMODITY.getCode();
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }
}
