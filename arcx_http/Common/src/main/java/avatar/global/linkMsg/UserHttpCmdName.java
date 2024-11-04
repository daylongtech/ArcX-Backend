package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**

 */
public class UserHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;

    /**

     */
    public static final String USER_INFO = Prefix+"/ply_ifo";
    public static final String UPDATE_USER_INFO = Prefix+"/ud_ply_ifo";
    public static final String UPDATE_USER_PASSWORD = Prefix+"/ud_ply_pwd";

    /**

     */
    public static final String USER_OPINION = Prefix+"/ply_fb";

    /**

     */
    public static final String COMMUNICATE_MSG = Prefix+"/cmc_ifo";

    /**

     */
    public static final String PROPERTY_KNAPSACK = Prefix+"/ppt_kpk";

    /**

     */
    public static final String UPGRADE_ATTRIBUTE = Prefix+"/upg_atb";
    public static final String USE_PROPERTY = Prefix+"/use_ppt";

    /**

     */
    public static final String WALLET_SPENDING = Prefix+"/wl_spd";
    public static final String CHAIN_WALLET = Prefix+"/chn_wl";
    public static final String WALLET_WITHDRAW = Prefix+"/wl_wtd";
    public static final String WALLET_RECHARGE = Prefix+"/wl_rcg";
    public static final String TRANSFER_TOKENS = Prefix+"/tsf_tks";
    public static final String WALLET_CONFIG = Prefix+"/wl_cfg";

}
