package avatar.global.linkMsg.http;

import avatar.global.code.basicConfig.ConfigMsg;

/**

 */
public class NoticeHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;

    /**

     */
    public static final String SYSTEM_NOTICE = Prefix+"/system_notice";

    /**

     */
    public static final String KICK_OUT_PRODUCT_NOTICE = Prefix+"/kick_out_product_notice";
    public static final String REFRESH_ROOM_PUSH = Prefix+"/refresh_room_push";

    /**

     */
    public static final String REFRESH_USER_BALANCE_NOTICE = Prefix+"/refresh_user_balance_notice";
    public static final String REFRESH_USER_ENERGY_NOTICE = Prefix+"/refresh_user_energy_notice";
}
