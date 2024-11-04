package avatar.util.checkParams;

import avatar.global.enumMsg.basic.system.LoginWayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.util.LogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**

 */
public class LoginCheckParamsUtil {
    /**

     */
    public static int checkRefreshToken(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String refreshToken = map.get("refTkn").toString();

                if(StrUtil.checkEmpty(refreshToken)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static int userLogin(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();
        try {
            int loginWayType = ParamsUtil.loginWayType(map);
            String mac = ParamsUtil.macId(map);
            String code = ParamsUtil.stringParmas(map, "iosTkn");
            String email = ParamsUtil.stringParmas(map, "email");
            String vfyCd = ParamsUtil.stringParmas(map, "vfyCd");
            String pwd = ParamsUtil.stringParmas(map, "pwd");
            if(StrUtil.checkEmpty(LoginWayTypeEnum.getNameByCode(loginWayType)) || StrUtil.checkEmpty(mac)){
                status = ClientCode.PARAMS_ERROR.getCode();
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && StrUtil.checkEmpty(email)){
                status = ClientCode.PARAMS_ERROR.getCode();
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && StrUtil.checkEmpty(vfyCd) &&
                    StrUtil.checkEmpty(pwd)){
                status = ClientCode.PARAMS_ERROR.getCode();
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && !StrUtil.checkEmpty(vfyCd)){
                
                status = LoginUtil.verifyEmailCodeOutTime(email, vfyCd);
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && !StrUtil.checkEmpty(pwd)){
                
                status = LoginUtil.checkEmailUser(email, pwd);
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    public static int emailVerifyCode(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();
        try {
            String email = ParamsUtil.stringParmasNotNull(map, "email");
            if(StrUtil.checkEmpty(email)){
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();
        }
        return status;
    }
}
