package avatar.util.nft;

import avatar.data.nft.MarketNftMsg;
import avatar.data.nft.NftKnapsackMsg;
import avatar.entity.nft.NftConfigEntity;
import avatar.entity.nft.NftHoldHistoryEntity;
import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.enumMsg.basic.nft.NftTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.nft.info.NftConfigDao;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.util.basic.CommodityUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.Map;

/**

 */
public class NftUtil {
    /**

     */
    public static int saleTax() {
        
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getSaleTax();
    }

    /**

     */
    public static long durability() {
        
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getDurability();
    }

    /**

     */
    public static NftKnapsackMsg initNftKnapsackMsg(String nftCode) {
        NftKnapsackMsg msg = new NftKnapsackMsg();
        int nftType = loadNftType(nftCode);
        if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
            
            SellGoldMachineUtil.fillKnapsackMsg(nftCode, msg);
        }else{
            msg = null;
        }
        return msg;
    }

    /**

     */
    public static int initNftMsg(String nftCode, Map<String, Object>dataMap) {
        int status = ClientCode.SUCCESS.getCode();
        int nftType = loadNftType(nftCode);
        if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
            SellGoldMachineUtil.initNftMsg(nftCode, dataMap);
        }else{
            status = ClientCode.NFT_NO_EXIST.getCode();
        }
        return status;
    }

    /**

     */
    public static int initMarketNftMsg(String nftCode, Map<String, Object> dataMap) {
        int status = ClientCode.SUCCESS.getCode();
        int nftType = loadNftType(nftCode);
        if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
            SellGoldMachineUtil.initMarketNftMsg(nftCode, dataMap);
        }else{
            status = ClientCode.NFT_NO_EXIST.getCode();
        }
        return status;
    }

    /**

     */
    public static MarketNftMsg initMarketNftMsg(String nftCode) {
        MarketNftMsg msg = new MarketNftMsg();
        
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        if(entity!=null){
            SellGoldMachineUtil.fillMarketNftMsg(entity, msg);
        }else{
            msg = null;
        }
        return msg;
    }

    /**

     */
    public static int loadNftType(String nftCode) {
        int nftType = 0;
        
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        if(entity!=null){
            nftType = NftTypeEnum.SELL_COIN_MACHINE.getCode();
        }
        return nftType;
    }

    /**

     */
    public static NftHoldHistoryEntity initNftHoldHistoryEntity(int nftType, String nftCode, int userId) {
        NftHoldHistoryEntity entity = new NftHoldHistoryEntity();
        entity.setNftType(nftType);
        entity.setNftCode(nftCode);
        entity.setUserId(userId);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static double loadSaleNftEarn(long saleNum) {
        return StrUtil.truncateFourDecimal((100-saleTax())*1.0/100*saleNum);
    }
}
