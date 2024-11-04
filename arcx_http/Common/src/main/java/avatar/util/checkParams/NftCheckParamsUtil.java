package avatar.util.checkParams;

import avatar.global.enumMsg.basic.nft.NftAttributeTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserUsdtUtil;

import java.util.Map;

/**

 */
public class NftCheckParamsUtil {
    /**

     */
    public static int exchangeNftGold(Map<String, Object> map){
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.stringParmasNotNull(map, "nftCd");
                long usdtAmt = ParamsUtil.longParmasNotNull(map, "usdtAmt");
                long usdtExc = ParamsUtil.longParmasNotNull(map, "usdtExc");
                if(StrUtil.checkEmpty(nftCd) || usdtAmt<=0 || usdtExc<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(UserUsdtUtil.usdtBalance(ParamsUtil.userId(map))<usdtAmt){
                    status = ClientCode.BALANCE_NO_ENOUGH.getCode();
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
    public static int nftMsg(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                if(StrUtil.checkEmpty(nftCd)){
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
    public static int upgradeNft(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                int atbTp = ParamsUtil.intParmasNotNull(map, "atbTp");
                if(StrUtil.checkEmpty(nftCd) || StrUtil.checkEmpty(NftAttributeTypeEnum.getNameByCode(atbTp))){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int sellGoldMachineAddCoin(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                long gdAmt = ParamsUtil.longParmasNotNull(map, "gdAmt");
                if(StrUtil.checkEmpty(nftCd) || gdAmt<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int sellGoldMachineRepairDurability(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int sellGoldMachineOperate(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                double salePrice = ParamsUtil.doubleParmasNotNull(map, "slPrc");
                if(StrUtil.checkEmpty(nftCd) || salePrice<1){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int sellGoldMachineStopOperate(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int sellGoldMachineListMarket(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                long salePrice = ParamsUtil.longParmasNotNull(map, "slPrc");
                if(StrUtil.checkEmpty(nftCd) || salePrice<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int sellGoldMachineCancelMarket(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int nftReport(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessTokenPage(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();
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
    public static int buyNft(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessTokenPage(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }
}
