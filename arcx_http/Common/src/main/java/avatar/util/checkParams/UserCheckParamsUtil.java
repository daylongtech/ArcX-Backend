package avatar.util.checkParams;

import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.RechargeConfigMsg;
import avatar.global.basicConfig.basic.UserConfigMsg;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.SexEnum;
import avatar.global.enumMsg.user.UserAttributeTypeEnum;
import avatar.module.user.info.EmailUserDao;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.LogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**

 */
public class UserCheckParamsUtil {
    /**

     */
    public static int updateUserInfo(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                String nickName = ParamsUtil.stringParmasNotNull(map, "plyNm");
                int sex = ParamsUtil.intParmasNotNull(map, "sex");
                if(StrUtil.checkEmpty(nickName) || StrUtil.checkEmpty(SexEnum.getNameByCode(sex))){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(nickName.length()>UserConfigMsg.nickLength){
                    status = ClientCode.LENGTH_LIMIT.getCode();
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static int updateUserPassword(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int userId = ParamsUtil.userId(map);
                String email = ParamsUtil.stringParmasNotNull(map, "email");
                String verifyCode = ParamsUtil.stringParmasNotNull(map, "vfyCd");
                String password = ParamsUtil.stringParmasNotNull(map, "pwd");
                int emailUser = EmailUserDao.getInstance().loadMsg(email);
                if(StrUtil.checkEmpty(email) || StrUtil.checkEmpty(verifyCode) ||
                        StrUtil.checkEmpty(password)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(password.length()>UserConfigMsg.passwordLength){
                    status = ClientCode.LENGTH_LIMIT.getCode();
                }else if(emailUser>0 && emailUser!=userId){
                    status = ClientCode.MSG_NOT_FIT.getCode();
                }else{
                    
                    status = LoginUtil.verifyEmailCodeOutTime(email, verifyCode);
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static int userOpinion(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                String content = ParamsUtil.stringParmasNotNull(map, "fbCnt");
                if(StrUtil.checkEmpty(content)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**

     */
    public static int communicateMsg(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();
        try {
            String email = ParamsUtil.stringParmasNotNull(map, "email");
            if(StrUtil.checkEmpty(email)){
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        } catch(Exception e){
            status = ClientCode.PARAMS_ERROR.getCode();
            ErrorDealUtil.printError(e);
        }
        return status;
    }

    /**

     */
    public static int upgradeAttribute(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int atbTp = ParamsUtil.intParmasNotNull(map, "atbTp");
                if(StrUtil.checkEmpty(UserAttributeTypeEnum.getNameByCode(atbTp))){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**

     */
    public static int useProperty(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int pptTp = ParamsUtil.intParmasNotNull(map, "pptTp");
                if(StrUtil.checkEmpty(PropertyTypeEnum.getNameByCode(pptTp))){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**

     */
    public static int chainWallet(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**

     */
    public static int walletWithdraw(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");
                int amount = ParamsUtil.intParmasNotNull(map, "amt");
                
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(amount<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();
                }else if(!RechargeConfigMsg.centerTokens.contains(tokenType+"")){
                    status = ClientCode.TOKENS_TYPE_ERROR.getCode();
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**

     */
    public static int walletRecharge(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");
                int amount = ParamsUtil.intParmasNotNull(map, "amt");
                
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(amount<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();
                }else if(!RechargeConfigMsg.centerTokens.contains(tokenType+"")){
                    status = ClientCode.TOKENS_TYPE_ERROR.getCode();
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**

     */
    public static int transferTokens(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");
                double amount = ParamsUtil.doubleParmasNotNull(map, "amt");
                String address = ParamsUtil.stringParmasNotNull(map, "ads");
                
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(amount<=0 || StrUtil.checkEmpty(address)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();
                }else if(!RechargeConfigMsg.centerTokens.contains(tokenType+"")){
                    status = ClientCode.TOKENS_TYPE_ERROR.getCode();
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }
}
