package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**

 */
public class SolanaHttpCmdName {
    private static final String Prefix = ConfigMsg.SOLONA_ROUTE_PREFIX;

    public static final String CREATE_ARCX_ACCOUNT = Prefix+"/create_arcx_account";
    public static final String ARCX_ACCOUNT_BALANCE = Prefix+"/arcx_account_balance";
    public static final String ARCX_TRANSFER = Prefix+"/arcx_transfer";
    public static final String ARCX_TRANSFER_TRANSACTION = Prefix+"/arcx_transfer_transaction";

}
