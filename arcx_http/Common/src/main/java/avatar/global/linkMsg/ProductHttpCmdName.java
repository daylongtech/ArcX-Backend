package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**

 */
public class ProductHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;

    /**

     */
    public static final String PRODUCT_MSG = Prefix+"/dev_ifo";
    public static final String PRODUCT_LIST = Prefix+"/dev_tbln";
    public static final String FAST_JOIN_PRODUCT = Prefix+"/fs_jn_dev";
    public static final String GAMING_PRODUCT = Prefix+"/gm_dev";

    /**

     */
    public static final String REPAIR_PRODUCT = Prefix+"/dev_rp";
}
