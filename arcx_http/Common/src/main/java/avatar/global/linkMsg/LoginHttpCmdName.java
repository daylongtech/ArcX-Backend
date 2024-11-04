package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**

 */
public class LoginHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;

    public static final String LOGOUT_ACCOUNT = Prefix+"/lgo_act";
    public static final String TOKEN_REFRESH = Prefix+"/ref_voucher";
    public static final String USER_LOGIN = Prefix+"/ply_log_on";
    public static final String EMAIL_VERIFY_CODE = Prefix+"/em_vrf_cd";


}
