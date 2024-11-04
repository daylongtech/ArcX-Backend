package avatar.service.nft;

import avatar.data.nft.MarketNftMsg;
import avatar.global.enumMsg.basic.nft.NftTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.module.nft.info.NftMarketListDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.NftCheckParamsUtil;
import avatar.util.nft.NftUtil;
import avatar.util.nft.SellGoldMachineUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ListUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class NftMarketService {
    /**

     */
    public static void marketNftList(Map<String, Object> map, Session session) {
        List<MarketNftMsg> retList = new ArrayList<>();
        
        int status = CheckParamsUtil.checkPage(map);
        if(ParamsUtil.isSuccess(status)) {
            
            List<String> list = ListUtil.getPageList(ParamsUtil.pageNum(map),
                    ParamsUtil.pageSize(map), NftMarketListDao.getInstance().loadMsg());
            if(list.size()>0){
                list.forEach(nftCode-> {
                    MarketNftMsg msg = NftUtil.initMarketNftMsg(nftCode);
                    if(msg!=null){
                        retList.add(msg);
                    }
                });
            }
        }
        
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
    }

    /**

     */
    public static void marketNftMsg(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.nftMsg(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            
            status = NftUtil.initMarketNftMsg(nftCode, dataMap);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void buyNft(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.buyNft(map);
        Map<String, Object> dataMap = new HashMap<>();
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);
                    int nftType = NftUtil.loadNftType(nftCode);
                    if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
                        
                        status = SellGoldMachineUtil.buyNft(userId, nftCode);
                    }else{
                        status = ClientCode.NFT_NO_EXIST.getCode();
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }
}
