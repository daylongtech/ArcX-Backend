package avatar.util.user;

import avatar.data.user.wallet.RetWalletConfigMsg;
import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.WalletConfigMsg;
import avatar.global.enumMsg.basic.recharge.TokensTypeEnum;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.basic.CommodityUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.GameShiftUtil;
import avatar.util.thirdpart.SolanaUtil;

/**

 */
public class WalletUtil {

    /**

     */
    public static void walletWithdraw(int tokenType, int amount, int userId) {
        
        Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        
        boolean flag = false;
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            flag = SolanaUtil.transferAxcBalance(userId, amount, accountEntity.getAxcAccount());
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //usdt
            flag = SolanaUtil.transferUsdtBalance(userId, amount, accountEntity.getUsdtAccount());
        }
        if(!flag){
            
            UserCostLogUtil.backTransferFail(userId, tokenType, amount);
        }
    }

    /**

     */
    public static String walletRecharge(int tokenType, int amount, int userId) {
        
        Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        String txHash = "";
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            txHash = SolanaUtil.loadAxcTransferTransaction(userId, amount, 0, accountEntity.getWallet(),
                    accountEntity.getAxcAccount(),"");
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //usdt
            txHash = SolanaUtil.loadUsdtTransferTransaction(userId, amount, 0, accountEntity.getWallet(),
                    accountEntity.getUsdtAccount(),"");
        }
        return StrUtil.checkEmpty(txHash)? "": GameShiftUtil.signTransaction(userId, txHash);
    }

    /**

     */
    public static String transferTokens(int tokenType, double amount, int userId, String address) {
        
        Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        String txHash = "";
        int decimals = StrUtil.loadDecimals(amount);
        int resultAmount = StrUtil.dealNumByDecimals(amount, decimals);
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            txHash = SolanaUtil.loadAxcTransferTransaction(userId, resultAmount, decimals, accountEntity.getWallet(),
                    accountEntity.getAxcAccount(),address);
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //usdt
            txHash = SolanaUtil.loadUsdtTransferTransaction(userId, resultAmount, decimals, accountEntity.getWallet(),
                    accountEntity.getUsdtAccount(),address);
        }
        return StrUtil.checkEmpty(txHash)? "": GameShiftUtil.signTransaction(userId, txHash);
    }

    /**

     */
    public static String loadTransferTokens(int tokenType, String address) {
        String accountToken = "";
        try {
            if (tokenType == TokensTypeEnum.AXC.getCode()) {
                //AXC
                accountToken = SolanaUtil.createAccount(address, SolanaUtil.axcMintPubkey()).getAta();
            } else if (tokenType == TokensTypeEnum.USDT.getCode()) {
                //USDT
                accountToken = SolanaUtil.createAccount(address, SolanaUtil.usdtMintPubkey()).getAta();
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
        }
        return accountToken;
    }

    /**

     */
    public static RetWalletConfigMsg minInfo() {
        RetWalletConfigMsg msg = new RetWalletConfigMsg();
        msg.setSol(WalletConfigMsg.minSolNum);//SOL
        msg.setUsdt(WalletConfigMsg.minUsdtNum);//USDT
        msg.setAxc(WalletConfigMsg.minAxcNum);//AXC
        msg.setArcx(WalletConfigMsg.minArcxNum);//ARCX
        return msg;
    }

    /**

     */
    public static RetWalletConfigMsg maxIfo() {
        RetWalletConfigMsg msg = new RetWalletConfigMsg();
        msg.setSol(WalletConfigMsg.maxSolNum);//SOL
        msg.setUsdt(WalletConfigMsg.maxUsdtNum);//USDT
        msg.setAxc(WalletConfigMsg.maxAxcNum);//AXC
        msg.setArcx(WalletConfigMsg.maxArcxNum);//ARCX
        return msg;
    }

    /**

     */
    public static RetWalletConfigMsg feeIfo() {
        RetWalletConfigMsg msg = new RetWalletConfigMsg();
        msg.setSol(WalletConfigMsg.solFee);//SOL
        msg.setUsdt(WalletConfigMsg.usdtFee);//USDT
        msg.setAxc(WalletConfigMsg.axcFee);//AXC
        msg.setArcx(WalletConfigMsg.arcxFee);//ARCX
        return msg;
    }
}
