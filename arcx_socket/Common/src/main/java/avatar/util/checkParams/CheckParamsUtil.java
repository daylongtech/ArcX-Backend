package avatar.util.checkParams;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.LogUtil;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.basic.general.CheckUtil;
import avatar.util.product.InnoProductUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.ForbidUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

/**

 */
public class CheckParamsUtil {
    /**

     */
    public static int checkProductOperation(String accessToken, JSONObject jsonObject, JSONObject dataJson) {
        int status = WebsocketEncodeUtil.checkEncode(accessToken, true, jsonObject);
        if(ParamsUtil.isSuccess(status)){
            try{
                int userId = UserUtil.loadUserIdByToken(accessToken);
                int productId = jsonObject.getInteger("devId");
                int operateState = jsonObject.getInteger("hdlTp");
                int coinMulti = jsonObject.containsKey("gdMul")?jsonObject.getInteger("gdMul"):0;
                String version = jsonObject.containsKey("vsCd")?jsonObject.getString("vsCd"):"";
                boolean unlockFlag = InnoProductUtil.isUnlockVersion(version);
                
                ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
                if(productId<=0 || operateState<0){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(CheckUtil.isSystemMaintain() && operateState== ProductOperationEnum.START_GAME.getCode()){
                    status = ClientCode.SYSTEM_MAINTAIN.getCode();
                }else if(entity==null){
                    status = ClientCode.PRODUCT_NO_EXIST.getCode();
                }else if(entity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
                    status = ClientCode.PRODUCT_EXCEPTION.getCode();
                }else if(operateState==ProductOperationEnum.START_GAME.getCode() &&
                        UserUtil.isAccountForbid(userId)){
                    status = ClientCode.ACCOUNT_DISABLED.getCode();

                }else if(coinMulti>0 && !ProductUtil.isCoinMultiExist(productId, coinMulti)){

                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(!unlockFlag && InnoProductUtil.isCoinMultiLowerLimit(userId, coinMulti, productId)){
                    status = ClientCode.MULTI_LOCK.getCode();
                }else{
                    status = ForbidUtil.checkGamingForbidProduct(userId, productId, unlockFlag);
                    if(!ParamsUtil.isSuccess(status)){

                    }
                }
            }catch (Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }
}
