package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**

 */
public class NftHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;

    /**

     */
    public static final String OPERATE_SELL_GOLD_MACHINE = Prefix+"/opr_sel_gd_mch";
    public static final String EXCHANGE_NFT_GOLD = Prefix+"/exc_nft_gd";
    /**

     */
    public static final String NFT_KNAPSACK = Prefix+"/nft_kpk";
    public static final String NFT_MSG = Prefix+"/nft_ifo";
    public static final String UPGRADE_NFT = Prefix+"/upg_nft";
    public static final String SELL_GOLD_MACHINE_ADD_GOLD = Prefix+"/sel_gd_mch_ad_gd";
    public static final String SELL_GOLD_MACHINE_REPAIR_DURABILITY = Prefix+"/sel_gd_mch_rp_drblt";
    public static final String SELL_GOLD_MACHINE_OPERATE = Prefix+"/sel_gd_mch_opr";
    public static final String SELL_GOLD_MACHINE_STOP_OPERATE = Prefix+"/sel_gd_mch_stp_opr";
    public static final String SELL_GOLD_MACHINE_LIST_MARKET = Prefix+"/sel_gd_mch_lst_mk";
    public static final String SELL_GOLD_MACHINE_CANCEL_MARKET = Prefix+"/sel_gd_mch_cacl_mk";
    public static final String NFT_REPORT = Prefix+"/nft_rp";

    /**

     */
    public static final String MARKET_NFT_LIST = Prefix+"/mk_nft_lst";
    public static final String MARKET_NFT_MSG = Prefix+"/mk_nft_ifo";
    public static final String BUY_NFT = Prefix+"/buy_nft";


}
