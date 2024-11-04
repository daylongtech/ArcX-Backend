package avatar.util.nft;

import avatar.data.nft.*;
import avatar.entity.nft.*;
import avatar.global.enumMsg.basic.nft.NftAttributeTypeEnum;
import avatar.global.enumMsg.basic.nft.NftStatusEnum;
import avatar.global.enumMsg.basic.nft.NftTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.nft.info.*;
import avatar.module.nft.record.NftHoldHistoryDao;
import avatar.module.nft.record.SellGoldMachineGoldHistoryDao;
import avatar.module.nft.record.SellGoldMachineOperateHistoryDao;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.ImgUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.recharge.RechargeGoldUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUsdtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 */
public class SellGoldMachineUtil {
    /**

     */
    public static String showImg(){
        
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?"": MediaUtil.getMediaUrl(entity.getStoreShowImg());
    }

    /**

     */
    public static String loadOperateMachine() {
        
        List<String> list = OperateSellGoldMachineListDao.getInstance().loadMsg();
        return list.size()==0?"":list.get(0);
    }

    /**

     */
    public static void dealSellGoldMachineMsg(int userId, String nftCode, Map<String, Object> dataMap) {
        
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        dataMap.put("nftCd", nftCode);
        long usdtExchange = (long)(entity.getOperatePrice()*1000000);
        dataMap.put("usdtExc", usdtExchange);
        double usdtBalance = UserUsdtUtil.usdtBalance(userId);
        dataMap.put("usdtBl", (long)usdtBalance);
        long storedNum = entity.getGoldNum();
        dataMap.put("maxUsdt", (long)(storedNum/usdtBalance));
        dataMap.put("maxVl", storedNum);
    }

    /**

     */
    public static int checkExchangeNftGold(Map<String, Object> map, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        int userId = ParamsUtil.userId(map);
        String nftCode = ParamsUtil.stringParmasNotNull(map, "nftCd");
        long usdtAmount = ParamsUtil.longParmasNotNull(map, "usdtAmt");
        long usdtExc = ParamsUtil.longParmasNotNull(map, "usdtExc");
        if(entity!=null && entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode() && entity.getUserId()!=0){
            long exchangeNum = (long)(entity.getOperatePrice()*1000000);
            if(exchangeNum!=usdtExc){
                status = ClientCode.NFT_PRICE_CHANGE.getCode();
            }else {
                if (exchangeNum*usdtAmount>entity.getGoldNum()){
                    status = ClientCode.NFT_STORED_NO_ENOUGH.getCode();
                }else{
                    
                    boolean flag = UserCostLogUtil.costNftGoldUsdt(userId, nftCode, usdtAmount);
                    if(!flag){
                        status = ClientCode.BALANCE_NO_ENOUGH.getCode();
                    }
                }
            }
        }else{
            status = ClientCode.NFT_OFF_OPERATE.getCode();
        }
        return status;
    }

    /**

     */
    public static void exchangeNftGold(Map<String, Object> map, SellGoldMachineMsgEntity entity) {
        int userId = ParamsUtil.userId(map);
        String nftCode = ParamsUtil.stringParmasNotNull(map, "nftCd");
        long usdtAmount = ParamsUtil.longParmasNotNull(map, "usdtAmt");
        long exchangeNum = (long)(entity.getOperatePrice()*1000000);
        long totalGold = exchangeNum*usdtAmount;
        
        UserCostLogUtil.addNftGold(userId, nftCode, totalGold);
        
        costMachineStoredGold(entity, exchangeNum, totalGold);
        
        double earn = addSellGoldHistory(entity, userId, usdtAmount, totalGold);
        
        UserCostLogUtil.addNftGoldEarn(entity.getUserId(), nftCode, earn);
    }

    /**

     */
    private static void costMachineStoredGold(SellGoldMachineMsgEntity entity, long exchangeNum, long totalGold) {
        boolean updateStatusFlag = false;
        String startOperateTime = entity.getStartOperateTime();
        entity.setGoldNum(entity.getGoldNum()-totalGold);
        if(entity.getGoldNum()<exchangeNum){
            
            entity.setStatus(NftStatusEnum.UNUSED.getCode());
            entity.setSellTime(0);
            entity.setStartOperateTime("");
            long operateMinute = loadOperateMinute(startOperateTime);
            entity.setDurability(Math.max(0, entity.getDurability()-operateMinute));
            entity.setExpNum(entity.getExpNum()+operateMinute);
            updateStatusFlag = true;
        }else{
            entity.setSellTime(entity.getSellTime()+1);
        }
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
        if(updateStatusFlag){
            
            SellGoldMachineOperateHistoryDao.getInstance().insert(initSellGoldMachineOperateHistoryEntity(
                    entity.getUserId(), entity.getNftCode(), startOperateTime));
        }
    }

    /**

     */
    private static long loadOperateMinute(String durabilityUpdateTime) {
        long totalTime = TimeUtil.getNowTime()-TimeUtil.strToLong(durabilityUpdateTime);
        long minuteTime = 60000;
        long totalDurability = totalTime/minuteTime;
        if(totalTime%minuteTime>0){
            totalDurability += 1;
        }
        return totalDurability;
    }

    /**

     */
    private static SellGoldMachineOperateHistoryEntity initSellGoldMachineOperateHistoryEntity(int userId, String nftCode,
            String startOperateTime) {
        SellGoldMachineOperateHistoryEntity entity = new SellGoldMachineOperateHistoryEntity();
        entity.setNftCode(nftCode);
        entity.setUserId(userId);
        entity.setCreateTime(startOperateTime);
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    private static double addSellGoldHistory(SellGoldMachineMsgEntity entity, int userId, long usdtAmount, long totalGold) {
        
        double fee = sellCoinMachineTax()*1.0/100;
        double costFee = StrUtil.truncateNmDecimal(usdtAmount*fee, 4);
        double realEarn = StrUtil.truncateNmDecimal(usdtAmount-costFee, 4);
        
        SellGoldMachineGoldHistoryDao.getInstance().insert(initSellGoldMachineGoldHistoryEntity(
                entity, userId, totalGold, usdtAmount, costFee, realEarn));
        return realEarn;
    }

    /**

     */
    private static int sellCoinMachineTax() {
        
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getOperateTax();
    }

    /**

     */
    private static SellGoldMachineGoldHistoryEntity initSellGoldMachineGoldHistoryEntity(SellGoldMachineMsgEntity msgEntity,
            int buyUserId, long totalGold, long usdtAmount, double costFee, double realEarn) {
        SellGoldMachineGoldHistoryEntity entity = new SellGoldMachineGoldHistoryEntity();
        entity.setNftCode(msgEntity.getNftCode());
        entity.setOperatePrice(msgEntity.getOperatePrice());
        entity.setUserId(msgEntity.getUserId());
        entity.setBuyUserId(buyUserId);
        entity.setGoldNum(totalGold);
        entity.setBalanceNum(msgEntity.getGoldNum());
        entity.setUsdtNum(usdtAmount);
        entity.setTax(sellCoinMachineTax());
        entity.setTaxNum(costFee);
        entity.setRealEarn(realEarn);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void fillKnapsackMsg(String nftCode, NftKnapsackMsg msg) {
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        msg.setNftCd(entity.getNftCode());
        msg.setNm(entity.getNftName());
        msg.setPct(ImgUtil.nftImg(entity.getImgId()));
        msg.setNftTp(NftTypeEnum.SELL_COIN_MACHINE.getCode());
        msg.setStat(entity.getStatus());
        msg.setAtbTbln(conciseAttributeList(entity));
    }

    /**

     */
    private static List<ConciseNftAttributeMsg> conciseAttributeList(SellGoldMachineMsgEntity entity) {
        List<ConciseNftAttributeMsg> retList = new ArrayList<>();
        List<NftAttributeTypeEnum> typeList = NftAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            typeList.forEach(enumMsg->{
                int attributeType = enumMsg.getCode();
                switch (attributeType){
                    case 1:
                        
                        retList.add(initConciseExpAttribute(attributeType, entity));
                        break;
                    case 2:
                        
                        retList.add(initConciseSpaceAttribute(attributeType, entity));
                        break;
                    case 3:
                        
                        retList.add(initConciseIncomeAttribute(attributeType, entity));
                        break;
                }
            });
        }
        return retList;
    }

    /**

     */
    private static ConciseNftAttributeMsg initConciseIncomeAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        ConciseNftAttributeMsg msg = new ConciseNftAttributeMsg();
        msg.setAtbTp(attributeType);
        int lv = entity.getSpaceLv();
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(lv);
        msg.setAtbIfo(configEntity==null?0:configEntity.getIncomeDiscount());
        return msg;
    }

    /**

     */
    private static ConciseNftAttributeMsg initConciseSpaceAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        ConciseNftAttributeMsg msg = new ConciseNftAttributeMsg();
        msg.setAtbTp(attributeType);
        int lv = entity.getSpaceLv();
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(lv);
        msg.setAtbIfo(configEntity==null?0:configEntity.getStoredMax());
        return msg;
    }

    /**

     */
    private static ConciseNftAttributeMsg initConciseExpAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        ConciseNftAttributeMsg msg = new ConciseNftAttributeMsg();
        msg.setAtbTp(attributeType);
        msg.setAtbIfo(entity.getLv());
        return msg;
    }

    /**

     */
    private static int loadNextLevel(int lv, List<Integer> levelList) {
        int retLv = 0;
        if(levelList.size()>0) {
            for (int nxLv : levelList) {
                if (nxLv > lv) {
                    retLv = nxLv;
                    break;
                }
            }
        }
        return retLv;
    }

    /**

     */
    public static void initNftMsg(String nftCode, Map<String, Object> dataMap) {
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        dataMap.put("nftCd", entity.getNftCode());
        dataMap.put("nm", entity.getNftName());
        dataMap.put("pct", ImgUtil.nftImg(entity.getImgId()));
        dataMap.put("nftTp", NftTypeEnum.SELL_COIN_MACHINE.getCode());
        dataMap.put("adAmt", entity.getAdv());
        dataMap.put("slCmdTp", entity.getSaleCommodityType());
        dataMap.put("slTax", NftUtil.saleTax());
        dataMap.put("opTm", loadOperateTime(entity));
        dataMap.put("stat", entity.getStatus());
        dataMap.put("gdIfo", goldMsg(entity));
        dataMap.put("durbtyIfo", durabilityMsg(entity));
        dataMap.put("atbTbln", attributeList(entity));
    }

    /**

     */
    private static long loadOperateTime(SellGoldMachineMsgEntity entity) {
        long operateTime = 0;
        if(entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode()){
            operateTime = (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getStartOperateTime()))/1000;
            operateTime = Math.max(0, operateTime);
        }
        return operateTime;
    }

    /**

     */
    private static List<NftAttributeMsg> attributeList(SellGoldMachineMsgEntity entity) {
        List<NftAttributeMsg> retList = new ArrayList<>();
        List<NftAttributeTypeEnum> typeList = NftAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            
            List<Integer> lvList = SellGoldMachineUpLvListDao.getInstance().loadMsg();
            typeList.forEach(enumMsg->{
                int attributeType = enumMsg.getCode();
                switch (attributeType){
                    case 1:
                        
                        retList.add(initExpAttribute(attributeType, lvList, entity));
                        break;
                    case 2:
                        
                        retList.add(initSpaceAttribute(attributeType, lvList, entity));
                        break;
                    case 3:
                        
                        retList.add(initIncomeAttribute(attributeType, lvList, entity));
                        break;
                }
            });
        }
        return retList;
    }

    /**

     */
    private static NftAttributeMsg initIncomeAttribute(int attributeType, List<Integer> lvList,
            SellGoldMachineMsgEntity entity) {
        NftAttributeMsg msg = new NftAttributeMsg();
        msg.setAtbTp(attributeType);
        int incomeLv = entity.getIncomeLv();
        int userLv = entity.getLv();
        msg.setLv(incomeLv);
        int nxLv = loadNextLevel(incomeLv, lvList);
        msg.setNxLv(nxLv);
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(incomeLv);
        msg.setUpFlg((nxLv>0 && nxLv<userLv)
                ? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        msg.setCsAmt(configEntity.getDiscountAxc());
        msg.setLvAmt(configEntity.getIncomeDiscount());
        
        if(nxLv>0){
            configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(nxLv);
            msg.setNxLvAmt(configEntity==null?0:configEntity.getIncomeDiscount());
        }else{
            msg.setNxLvAmt(0);
        }
        msg.setSdNma(incomeLv);
        msg.setSdDma(userLv);
        return msg;
    }

    /**

     */
    private static NftAttributeMsg initSpaceAttribute(int attributeType, List<Integer> lvList,
            SellGoldMachineMsgEntity entity) {
        NftAttributeMsg msg = new NftAttributeMsg();
        msg.setAtbTp(attributeType);
        int spaceLv = entity.getSpaceLv();
        int userLv = entity.getLv();
        msg.setLv(spaceLv);
        int nxLv = loadNextLevel(spaceLv, lvList);
        msg.setNxLv(nxLv);
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(spaceLv);
        msg.setUpFlg((nxLv>0 && nxLv<userLv)
                ? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        msg.setCsAmt(configEntity.getStoredAxc());
        msg.setLvAmt(configEntity.getStoredMax());
        
        if(nxLv>0){
            configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(nxLv);
            msg.setNxLvAmt(configEntity==null?0:configEntity.getStoredMax());
        }else{
            msg.setNxLvAmt(0);
        }
        msg.setSdNma(spaceLv);
        msg.setSdDma(userLv);
        return msg;
    }

    /**

     */
    private static NftAttributeMsg initExpAttribute(int attributeType, List<Integer> lvList,
            SellGoldMachineMsgEntity entity) {
        NftAttributeMsg msg = new NftAttributeMsg();
        msg.setAtbTp(attributeType);
        
        int lv = entity.getLv();
        msg.setLv(lv);
        int nextLv = loadNextLevel(lv, lvList);
        msg.setNxLv(nextLv);
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(lv);
        msg.setUpFlg((configEntity.getUpExp()>0 && loadMachineExp(entity)>=configEntity.getUpExp())
                ? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
        msg.setCmdTp(CommodityUtil.axc());
        msg.setCsAmt(configEntity.getLvAxc());
        msg.setLvAmt(lv);
        msg.setNxLvAmt(nextLv);
        msg.setSdNma(entity.getExpNum());
        msg.setSdDma(configEntity.getUpExp());
        return msg;
    }

    /**

     */
    private static long loadMachineExp(SellGoldMachineMsgEntity entity) {
        long expNum = entity.getExpNum();
        if(entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode() && !StrUtil.checkEmpty(entity.getStartOperateTime())){
            expNum += (Math.max(0, (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getStartOperateTime()))/60000));
        }
        return expNum;
    }

    /**

     */
    private static NftDurabilityMsg durabilityMsg(SellGoldMachineMsgEntity entity) {
        NftDurabilityMsg msg = new NftDurabilityMsg();
        msg.setDurbtyAmt(loadMachineDurability(entity));
        long maxDurability = NftUtil.durability();
        msg.setCmdTp(CommodityUtil.axc());
        msg.setCsAmt(maxDurability-entity.getDurability());
        return msg;
    }

    /**

     */
    private static long loadMachineDurability(SellGoldMachineMsgEntity entity) {
        long durability = entity.getDurability();
        if(entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode() && !StrUtil.checkEmpty(entity.getStartOperateTime())){
            durability -= (Math.max(0, (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getStartOperateTime()))/60000));
        }
        return Math.max(0, durability);
    }

    /**

     */
    private static NftGoldMsg goldMsg(SellGoldMachineMsgEntity entity) {
        NftGoldMsg msg = new NftGoldMsg();
        msg.setCnGdAmt(entity.getGoldNum());
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
        msg.setUpGdAmt(Math.max(0,(configEntity.getStoredMax()-entity.getGoldNum())));
        
        if(entity.getSpaceLv()!=entity.getIncomeLv()){
            configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getIncomeLv());
        }
        msg.setDscot(configEntity.getIncomeDiscount());
        msg.setOriAmt(RechargeGoldUtil.basicGoldNum());
        msg.setTax(sellCoinMachineTax());
        return msg;
    }

    /**

     */
    public static int checkNftUpgrade(int userId, int attributeType, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()==NftStatusEnum.LISTING.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();
        }else {
            List<Integer> lvList = SellGoldMachineUpLvListDao.getInstance().loadMsg();
            int nextLv = 0;
            long costAxc = 0;
            if (attributeType == NftAttributeTypeEnum.LV.getCode()) {
                
                
                SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getLv());
                nextLv = loadNextLevel(entity.getLv(), lvList);
                costAxc = configEntity.getLvAxc();
                if (entity.getExpNum() < configEntity.getUpExp()) {
                    status = ClientCode.UPGRADE_CONDITION_NOT_FIT.getCode();
                }
            } else if (attributeType == NftAttributeTypeEnum.SPACE.getCode()) {
                
                
                SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
                nextLv = loadNextLevel(entity.getSpaceLv(), lvList);
                costAxc = configEntity.getStoredAxc();
            } else if (attributeType == NftAttributeTypeEnum.INCOME.getCode()) {
                
                
                SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getIncomeLv());
                nextLv = loadNextLevel(entity.getIncomeLv(), lvList);
                costAxc = configEntity.getDiscountAxc();
            }
            if(nextLv<=0){
                status = ClientCode.UPGRADE_CONDITION_NOT_FIT.getCode();
            }else if(UserCostLogUtil.costNftAttribute(entity.getUserId(), attributeType, entity.getNftCode(), costAxc)){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static void dealNftUpgrade(int attributeType, SellGoldMachineMsgEntity entity) {
        if(attributeType==NftAttributeTypeEnum.LV.getCode()){
            
            int oriLv = entity.getLv();
            entity.setLv(oriLv+1);
            
            SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(oriLv);
            entity.setExpNum(Math.max(0, entity.getExpNum()-configEntity.getUpExp()));
        }else if(attributeType==NftAttributeTypeEnum.SPACE.getCode()){
            
            entity.setSpaceLv(entity.getSpaceLv()+1);
        }else if(attributeType==NftAttributeTypeEnum.INCOME.getCode()){
            
            entity.setIncomeLv(entity.getIncomeLv()+1);
        }
        
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**

     */
    public static int checkSellGoldMachineAddCoin(int userId, long goldAmount, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();
        }else {
            
            SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
            SellGoldMachineUpConfigEntity incomeConfigEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getIncomeLv());
            long basicGoldNum = RechargeGoldUtil.basicGoldNum();
            long maxGold = configEntity.getStoredMax();
            double usdtAmount = 0;
            if(goldAmount>(maxGold-entity.getGoldNum()) || goldAmount<basicGoldNum){
                status = ClientCode.NFT_STORED_NUM_ERROR.getCode();
            }else{
                
                usdtAmount = StrUtil.truncateFourDecimal((goldAmount*1.0/basicGoldNum)*
                        (100-incomeConfigEntity.getIncomeDiscount())*1.0/100);
            }
            if(usdtAmount>0 && UserCostLogUtil.costSellGoldMachineAddCoin(userId, usdtAmount, entity.getNftCode())){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static void dealSellGoldMachineAddCoin(long goldAmount, SellGoldMachineMsgEntity entity) {
        entity.setGoldNum(entity.getGoldNum()+goldAmount);
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**

     */
    public static int checkSellGoldMachineRepairDurability(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();
        }else {
            long durability = NftUtil.durability()-entity.getDurability();
            if(durability>0 && UserCostLogUtil.costNftRepairDurability(userId, entity.getNftCode(), durability)){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static void dealSellGoldMachineRepairDurability(SellGoldMachineMsgEntity entity) {
        entity.setDurability(NftUtil.durability());
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**

     */
    public static int checkSellGoldMachineOperate(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();
        }else if(entity.getDurability()<=0){
            status = ClientCode.NFT_DURABILITY.getCode();
        }
        return status;
    }

    /**

     */
    public static void dealSellGoldMachineOperate(double salePrice, SellGoldMachineMsgEntity entity) {
        entity.setOperatePrice(salePrice);
        entity.setStartOperateTime(TimeUtil.getNowTimeStr());
        entity.setStatus(NftStatusEnum.IN_OPERATION.getCode());
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**

     */
    public static int checkSellGoldMachineStopOperate(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()!=NftStatusEnum.IN_OPERATION.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    public static void dealSellGoldMachineStopOperate(SellGoldMachineMsgEntity entity) {
        entity.setStatus(NftStatusEnum.UNUSED.getCode());
        entity.setSellTime(0);
        entity.setStartOperateTime("");
        long operateMinute = loadOperateMinute(entity.getStartOperateTime());
        entity.setDurability(Math.max(0, entity.getDurability()-operateMinute));
        entity.setExpNum(entity.getExpNum()+operateMinute);
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**

     */
    public static int checkSellGoldMachineListMarket(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    public static void dealSellGoldMachineListMarket(long salePrice, SellGoldMachineMsgEntity entity) {
        entity.setSaleNum(salePrice);
        entity.setStatus(NftStatusEnum.LISTING.getCode());
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**

     */
    public static int checkSellGoldMachineCancelMarket(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()!=NftStatusEnum.LISTING.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    public static void dealSellGoldMachineCancelMarket(SellGoldMachineMsgEntity entity) {
        entity.setStatus(NftStatusEnum.UNUSED.getCode());
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**

     */
    public static NftReportMsg initNftReportMsg(SellGoldMachineGoldHistoryEntity entity) {
        NftReportMsg msg = new NftReportMsg();
        msg.setOpTm(TimeUtil.strToLong(entity.getCreateTime()));
        msg.setOpPrc((long)(1.1*1000000));
        msg.setInc(entity.getRealEarn());
        msg.setBlAmt(entity.getGoldNum());
        msg.setTax(entity.getTax());
        return msg;
    }

    /**

     */
    public static void fillMarketNftMsg(SellGoldMachineMsgEntity entity, MarketNftMsg msg) {
        msg.setNftCd(entity.getNftCode());
        msg.setNm(entity.getNftName());
        msg.setPct(ImgUtil.nftImg(entity.getImgId()));
        msg.setNftTp(NftTypeEnum.SELL_COIN_MACHINE.getCode());
        msg.setSlCmdTp(entity.getSaleCommodityType());
        msg.setSlAmt(entity.getSaleNum());
        msg.setAtbTbln(conciseAttributeList(entity));
    }

    /**

     */
    public static void initMarketNftMsg(String nftCode, Map<String, Object> dataMap) {
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        dataMap.put("nftCd", entity.getNftCode());
        dataMap.put("nm", entity.getNftName());
        dataMap.put("pct", ImgUtil.nftImg(entity.getImgId()));
        dataMap.put("nftTp", NftTypeEnum.SELL_COIN_MACHINE.getCode());
        dataMap.put("atbTbln", marketAttributeList(entity));
    }

    /**

     */
    private static List<MarketNftAttributeMsg> marketAttributeList(SellGoldMachineMsgEntity entity) {
        List<MarketNftAttributeMsg> retList = new ArrayList<>();
        List<NftAttributeTypeEnum> typeList = NftAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            typeList.forEach(enumMsg->{
                int attributeType = enumMsg.getCode();
                switch (attributeType){
                    case 1:
                        
                        retList.add(initMarketExpAttribute(attributeType, entity));
                        break;
                    case 2:
                        
                        retList.add(initMarketSpaceAttribute(attributeType, entity));
                        break;
                    case 3:
                        
                        retList.add(initMarketIncomeAttribute(attributeType, entity));
                        break;
                }
            });
        }
        return retList;
    }

    /**

     */
    private static MarketNftAttributeMsg initMarketIncomeAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        MarketNftAttributeMsg msg = new MarketNftAttributeMsg();
        msg.setAtbTp(attributeType);
        msg.setLv(entity.getSpaceLv());
        msg.setAtbMsg(entity.getGoldNum()+"");
        return msg;
    }

    /**

     */
    private static MarketNftAttributeMsg initMarketSpaceAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        MarketNftAttributeMsg msg = new MarketNftAttributeMsg();
        msg.setAtbTp(attributeType);
        msg.setLv(entity.getSpaceLv());
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
        msg.setAtbMsg(configEntity.getIncomeDiscount()+"%");
        return msg;
    }

    /**

     */
    private static MarketNftAttributeMsg initMarketExpAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        MarketNftAttributeMsg msg = new MarketNftAttributeMsg();
        msg.setAtbTp(attributeType);
        msg.setLv(entity.getLv());
        
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getLv());
        msg.setAtbMsg(configEntity==null?"0":configEntity.getUpExp()+"");
        return msg;
    }

    /**

     */
    public static int buyNft(int userId, String nftCode) {
        
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        
        int status = checkBuyNft(userId, entity);
        if(ParamsUtil.isSuccess(status)){
            
            dealBuyNft(userId, entity);
        }
        return status;
    }

    /**

     */
    private static int checkBuyNft(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();
        if(entity.getStatus()!=NftStatusEnum.LISTING.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();
        }else if(!UserCostLogUtil.costBuyNft(userId, entity)){
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();
        }
        return status;
    }

    /**

     */
    private static void dealBuyNft(int userId, SellGoldMachineMsgEntity entity) {
        int oriUserId = entity.getUserId();
        entity.setUserId(userId);
        entity.setStatus(NftStatusEnum.UNUSED.getCode());
        boolean flag = SellGoldMachineMsgDao.getInstance().update(oriUserId, entity);
        if(flag){
            
            UserCostLogUtil.addSaleNftEarn(oriUserId, entity.getSaleCommodityType(),
                    NftUtil.loadSaleNftEarn(entity.getSaleNum()), entity.getNftCode());
            
            NftHoldHistoryDao.getInstance().insert(NftUtil.initNftHoldHistoryEntity(
                    NftTypeEnum.SELL_COIN_MACHINE.getCode(), entity.getNftCode(), userId));
        }else{

        }
    }
}
