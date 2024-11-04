package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**

 */
public class NoticeHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_SERVER_ROUTE_PREFIX;

    /**

     */
    public static final String REFRESH_USER_BALANCE_NOTICE = Prefix+"/refresh_user_balance_notice";
    public static final String REFRESH_USER_ENERGY_NOTICE = Prefix+"/refresh_user_energy_notice";
}
