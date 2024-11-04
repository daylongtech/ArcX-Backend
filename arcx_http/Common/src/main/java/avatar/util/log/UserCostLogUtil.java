package avatar.util.log;

import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.global.basicConfig.basic.UserCostMsg;
import avatar.global.basicConfig.basic.WalletConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.basic.nft.NftAttributeTypeEnum;
import avatar.global.enumMsg.basic.recharge.TokensTypeEnum;
import avatar.global.enumMsg.user.UserAttributeTypeEnum;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserPropertyUtil;
import avatar.util.user.UserUsdtUtil;

/**

 */
public class UserCostLogUtil {
    /**

     */
    public static void welfareSign(int userId, int commodityType, int awardNum) {
        String costMsg = UserCostMsg.signBonus;
        
        UserOperateLogUtil.costBalance(awardNum, userId, 0, commodityType, costMsg);
        
        UserOperateLogUtil.sign(userId);
    }

    /**

     */
    public static void registerWelfare(int userId, int awardNum) {
        
        UserOperateLogUtil.costBalance(awardNum, userId, 0, CommodityUtil.gold(), UserCostMsg.registerPresentGold);
    }

    /**

     */
    public static boolean costAttribute(int userId, int attributeType, long costAxc) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAxc);
        if(flag && costAxc>0){
            String logMsg = UserCostMsg.upgradeAttribute+"【"+ UserAttributeTypeEnum.getNameByCode(attributeType) +"】";
            
            UserOperateLogUtil.costBalance(costAxc*-1, userId, 0, commodityType, logMsg);
        }
        return flag;
    }

    /**

     */
    public static boolean refreshMallProperty(int userId, int costAxc) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAxc);
        if(flag && costAxc>0){
            
            UserOperateLogUtil.costBalance(costAxc*-1, userId, 0, commodityType, UserCostMsg.refreshMallProperty);
        }
        return flag;
    }

    /**

     */
    public static boolean costSuperPlayer(int userId) {
        int price = SuperPlayerUtil.loadPrice();
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, price);
        if(flag && price>0){
            
            UserOperateLogUtil.costUsdt(price*-1, userId, UserCostMsg.superPlayer);
        }
        return flag;
    }

    /**

     */
    public static void superPlayerGold(int userId, int awardNum) {
        int commodityType = CommodityUtil.gold();
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if(flag){
            
            UserOperateLogUtil.costBalance(awardNum, userId, 0, commodityType, UserCostMsg.superPlayer);
        }else{

        }
    }

    /**

     */
    public static void superPlayerProperty(int userId, int awardId, int awardNum) {
        boolean flag = UserPropertyUtil.addUserProperty(userId, awardId, awardNum);
        if(flag){
            
            UserOperateLogUtil.costProperty(awardNum, userId, awardId, UserCostMsg.superPlayer);
        }else{

                    PropertyTypeEnum.getNameByCode(awardId));
        }
    }

    /**

     */
    public static boolean costOfficialRechargeGold(int userId, int price) {
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, price);
        if(flag && price>0){
            
            UserOperateLogUtil.costUsdt(price*-1, userId, UserCostMsg.officialRechargeCoin);
        }
        return flag;
    }

    /**

     */
    public static void officialRechargeGold(int userId, long awardNum) {
        int commodityType = CommodityUtil.gold();
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if(flag){
            
            UserOperateLogUtil.costBalance(awardNum, userId, 0, commodityType, UserCostMsg.officialRechargeCoin);
        }else{

        }
    }

    /**

     */
    public static boolean costRechargeProperty(int userId, int price) {
        int commodityType = CommodityUtil.axc();//AXC
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, price);
        if(flag && price>0){
            
            UserOperateLogUtil.costBalance(price*-1, userId, 0, commodityType, UserCostMsg.buyProperty);
        }
        return flag;
    }

    /**

     */
    public static void rechargePropertyAward(int userId, RechargePropertyMsgEntity entity) {
        int awardId = entity.getPropertyType();
        int awardNum = entity.getNum();
        boolean flag = UserPropertyUtil.addUserProperty(userId, awardId, awardNum);
        if(flag){
            
            UserOperateLogUtil.costProperty(awardNum, userId, awardId, UserCostMsg.buyProperty);
        }else{

                    PropertyTypeEnum.getNameByCode(awardId));
        }
    }

    /**

     */
    public static boolean costWalletWithdraw(int userId, int tokenType, int amount) {
        String costMsg = UserCostMsg.withdrawChain;
        boolean flag = false;
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //AXC
            if(UserBalanceUtil.getUserBalance(userId, CommodityUtil.axc())>=(amount+WalletConfigMsg.axcFee)){
                flag = UserBalanceUtil.costUserBalance(userId, CommodityUtil.axc(), amount);
                if(flag){
                    
                    UserOperateLogUtil.costBalance(amount*-1, userId, 0, CommodityUtil.axc(), costMsg);
                }
                
                flag = UserBalanceUtil.costUserBalance(userId, CommodityUtil.axc(), WalletConfigMsg.axcFee);
                if(flag){
                    
                    UserOperateLogUtil.costBalance(WalletConfigMsg.axcFee*-1, userId, 0, CommodityUtil.axc(),
                            UserCostMsg.withdrawChainFee);
                }
            }
        }else if(tokenType== TokensTypeEnum.USDT.getCode()){
            //USDT
            if(UserUsdtUtil.usdtBalance(userId)>=(amount+WalletConfigMsg.usdtFee)){
                flag = UserUsdtUtil.costUsdtBalance(userId, amount);
                if(flag){
                    
                    UserOperateLogUtil.costUsdt(amount*-1, userId, costMsg);
                }
                
                flag = UserUsdtUtil.costUsdtBalance(userId,  WalletConfigMsg.usdtFee);
                if(flag){
                    
                    UserOperateLogUtil.costUsdt(WalletConfigMsg.usdtFee*-1, userId, UserCostMsg.withdrawChainFee);
                }
            }
        }
        return flag;
    }

    /**

     */
    public static boolean costNftGoldUsdt(int userId, String nftCode, long usdtAmount) {
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, usdtAmount);
        if(flag){
            UserOperateLogUtil.costUsdt(usdtAmount*-1, userId, UserCostMsg.nftMachineBuy.replace("aa", nftCode));
        }else{

        }
        return flag;
    }

    /**

     */
    public static void addNftGold(int userId, String nftCode, long coinNum) {
        int commodityType = CommodityUtil.gold();
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, coinNum);
        if(flag){
            UserOperateLogUtil.costBalance(coinNum, userId, 0, commodityType,
                    UserCostMsg.nftMachineBuy.replace("aa", nftCode));
        }else{

        }
    }

    /**

     */
    public static void addNftGoldEarn(int userId, String nftCode, double usdtAmount) {
        boolean flag = UserUsdtUtil.addUsdtBalance(userId, usdtAmount);
        if(flag){
            UserOperateLogUtil.costUsdt(usdtAmount, userId, UserCostMsg.nftMachineBuy.replace("aa", nftCode));
        }else{

        }
    }

    /**

     */
    public static boolean costNftAttribute(int userId, int attributeType, String nftCode, long costAxc) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAxc);
        if(flag && costAxc>0){
            String logMsg = UserCostMsg.upgradeNftAttribute+"【"+
                    nftCode + "-" + NftAttributeTypeEnum.getNameByCode(attributeType) +"】";
            
            UserOperateLogUtil.costBalance(costAxc*-1, userId, 0, commodityType, logMsg);
        }
        return flag;
    }

    /**

     */
    public static boolean costSellGoldMachineAddCoin(int userId, double usdtAmount, String nftCode) {
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, usdtAmount);
        if(flag){
            UserOperateLogUtil.costUsdt(usdtAmount*-1, userId,
                    UserCostMsg.sellCoinMachineAddCoin.replace("aa", nftCode));
        }else{

        }
        return flag;
    }

    /**

     */
    public static boolean costNftRepairDurability(int userId, String nftCode, long costNum) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costNum);
        if(flag){
            UserOperateLogUtil.costBalance(costNum*-1, userId, 0, commodityType,
                    UserCostMsg.sellCoinMachineRepairDurability.replace("aa", nftCode));
        }else{

        }
        return flag;
    }

    /**

     */
    public static boolean costBuyNft(int userId, SellGoldMachineMsgEntity entity) {
        int commodityType = entity.getSaleCommodityType();
        long costAmount = entity.getSaleNum();
        boolean flag;
        if(commodityType==CommodityUtil.usdt()){
            flag = UserUsdtUtil.costUsdtBalance(userId, costAmount);
            if(flag){
                UserOperateLogUtil.costUsdt(costAmount*-1, userId,
                        UserCostMsg.buyNft.replace("aa", entity.getNftCode()));
            }else{

            }
        }else{
            flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAmount);
            if(flag){
                UserOperateLogUtil.costBalance(costAmount*-1, userId, 0, commodityType,
                        UserCostMsg.buyNft.replace("aa", entity.getNftCode()));
            }else{

                        CommodityTypeEnum.getNameByCode(commodityType), costAmount);
            }
        }
        return flag;
    }

    /**

     */
    public static void addSaleNftEarn(int userId, int commodityType, double saleNum, String nftCode) {
        boolean flag;
        if(commodityType==CommodityUtil.usdt()){
            flag = UserUsdtUtil.addUsdtBalance(userId, saleNum);
            if(flag){
                UserOperateLogUtil.costUsdt(saleNum, userId,
                        UserCostMsg.saleNft.replace("aa", nftCode));
            }else{

            }
        }else{
            long resultNum = (long) saleNum;
            flag = UserBalanceUtil.addUserBalance(userId, commodityType, resultNum);
            if(flag){
                UserOperateLogUtil.costBalance(resultNum, userId, 0, commodityType,
                        UserCostMsg.saleNft.replace("aa", nftCode));
            }else{

                        CommodityTypeEnum.getNameByCode(commodityType), resultNum);
            }
        }
    }

    /**

     */
    public static void backTransferFail(int userId, int tokenType, int amount) {
        String costMsg = UserCostMsg.withdrawChainFailRet;
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            int commodityType = CommodityUtil.axc();//AXC
            int totalAmount = amount+WalletConfigMsg.axcFee;
            boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, totalAmount);
            if(flag){
                UserOperateLogUtil.costBalance(totalAmount, userId, 0, commodityType, costMsg);
            }else{

                        CommodityTypeEnum.getNameByCode(commodityType), totalAmount);
            }
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //USDT
            double totalNum = amount+WalletConfigMsg.usdtFee;
            boolean flag = UserUsdtUtil.addUsdtBalance(userId, totalNum);
            if(flag){
                UserOperateLogUtil.costUsdt(totalNum, userId, costMsg);
            }else{

            }
        }
    }
}
