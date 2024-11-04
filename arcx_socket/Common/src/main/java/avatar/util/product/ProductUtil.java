package avatar.util.product;

import avatar.data.product.gamingMsg.*;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.user.info.ConciseUserMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.innoMsg.InnoProductWinPrizeMsgEntity;
import avatar.entity.product.productType.ProductSecondLevelTypeEntity;
import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.module.product.gaming.*;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.innoMsg.InnoProductWinPrizeMsgDao;
import avatar.module.product.instruct.InstructCatchDollDao;
import avatar.module.product.instruct.InstructPresentDao;
import avatar.module.product.instruct.InstructPushCoinDao;
import avatar.module.product.productType.LotteryCoinPercentDao;
import avatar.module.product.productType.ProductSecondLevelTypeDao;
import avatar.module.product.repair.ProductRepairDao;
import avatar.module.user.online.UserProductOnlineListDao;
import avatar.module.user.product.UserLotteryMsgDao;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.TimeUtil;
import avatar.util.thirdpart.OfficialAccountUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class ProductUtil {
    /**

     */
    public static boolean isDollMachine(int productId) {
        int productType = loadProductType(productId);
        return productType==ProductTypeEnum.DOLL_MACHINE.getCode() ||
                productType==ProductTypeEnum.PRESENT_MACHINE.getCode();
    }

    /**

     */
    public static int loadProductType(int productId) {
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?0:entity.getProductType();
    }

    /**

     */
    public static boolean isInnoProduct(int productId) {
        boolean flag = false;
        
        int secondType = loadSecondType(productId);
        if(secondType>0){
            
            ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
            flag = entity!=null && entity.getIsInnoProduct()== YesOrNoEnum.YES.getCode();
        }
        return flag;
    }

    /**

     */
    public static int loadSecondType(int productId) {
        
        ProductInfoEntity infoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        return infoEntity==null?0:infoEntity.getSecondType();
    }

    /**

     */
    public static String productLog(int productId){
        return productId+"-"+getProductTypeName(productId)+"-"+loadProductName(productId);
    }

    /**

     */
    public static String getProductTypeName(int productId) {
        String retName = "";
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        int productType = entity.getProductType();
        if(productType>0){
            retName = ProductTypeEnum.loadNameByCode(productType);
        }
        return retName;
    }

    /**

     */
    public static String loadProductName(int productId) {
        String name = "";
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        if(entity!=null){
            name = entity.getProductName();
        }
        return name;
    }

    /**

     */
    public static int productCost(int productId) {
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        int cost = entity==null?0:entity.getCost();
        if(isInnoProduct(productId)) {
            
            ProductAwardLockMsg lockMsg = ProductAwardLockDao.getInstance().loadByProductId(productId);
            if (lockMsg.getCoinMulti() > 0) {
                cost = lockMsg.getCoinMulti();
            }
        }
        return cost;
    }

    /**

     */
    public static void dealSelfProductAwardMsg(List<Long> list, List<Long> awardList, long onProductTime) {
        if(list.size()>0){
            for(long awardId : list){
                
                InnoProductWinPrizeMsgEntity entity = InnoProductWinPrizeMsgDao.getInstance().loadDbById(awardId);
                long createTime = TimeUtil.strToLong(entity.getCreateTime());
                if(entity.getIsStart()==YesOrNoEnum.YES.getCode() && createTime>=onProductTime
                        && (TimeUtil.getNowTime()-createTime)<=ProductConfigMsg.innoAwardTillTime){
                    
                    awardList.add(awardId);
                }
            }
        }
    }

    /**

     */
    public static long loadProductLeftTime(long refreshTime, int productId) {
        long leftTime = 0;
        long leftLongTime = TimeUtil.getNowTime()-refreshTime;
        int productOffTime = productOffLineTime(productId)-10;
        if(leftLongTime>=0){
            leftTime = Math.max(0,(productOffTime-leftLongTime/1000));
        }
        return leftTime;
    }

    /**

     */
    public static int productOffLineTime(int productId){
        int offLineTime = 0;
        int secondType = loadSecondType(productId);
        if(secondType>0){
            
            ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
            offLineTime = entity==null?0:entity.getServerOffLineTime();
        }
        return offLineTime;
    }

    /**

     */
    public static void refreshRoomMsg(int productId) {
        
        List<Session> sessionList = ProductSocketUtil.dealOnlineSession(productId);
        if(sessionList.size()>0){
            
            JSONObject jsonMap = initProductRoomMsg(productId);
            sessionList.forEach(session-> SendWebsocketMsgUtil.sendBySession(WebSocketCmd.S2C_ROOM_MSG, ClientCode.SUCCESS.getCode(),
                    session, jsonMap));
        }
    }

    /**

     */
    public static void refreshRoomMsg(int productId, int userId) {
        
        JSONObject jsonMap = initProductRoomMsg(productId);
        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_ROOM_MSG, ClientCode.SUCCESS.getCode(),
                userId, jsonMap);
    }

    /**

     */
    private static JSONObject initProductRoomMsg(int productId) {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("devId", productId);
        
        ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(gamingUserMsg.getUserId()>0){
            jsonMap.put("gmPly", CrossServerMsgUtil.initServerTypeUserMsg(
                    gamingUserMsg.getUserId(), gamingUserMsg.getServerSideType()));
        }
        jsonMap.put("vsUsrTbln", loadProductVisitUserList(productId));
        jsonMap.put("ppAmt", ProductSocketUtil.sessionMap.get(productId).size());
        return jsonMap;
    }

    /**

     */
    private static List<ConciseUserMsg> loadProductVisitUserList(int productId) {
        int gamingUserId = ProductGamingUtil.loadGamingUserId(productId);
        List<ConciseUserMsg> retList = new ArrayList<>();
        
        List<Integer> onlineList = UserProductOnlineListDao.getInstance().loadByProductId(productId);
        if(onlineList.size()>0){
            onlineList.forEach(userId-> {
                if(gamingUserId!=userId){
                    retList.add(UserUtil.initConciseUserMsg(userId));
                }
            });
        }
        return retList;
    }

    /**

     */
    public static boolean isEnoughCost(int userId, int productId, int coinMulti) {
        int cost = coinMulti>0?coinMulti:ProductUtil.productCost(productId);
        long balance = UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode());
        return balance>=cost;
    }

    /**

     */
    public static boolean isNormalProduct(int productId){
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity!=null && entity.getStatus()== ProductStatusEnum.NORMAL.getCode();
    }

    /**

     */
    public static boolean isOffLine(int operate) {
        return operate == ProductOperationEnum.OFF_LINE.getCode();
    }

    /**

     */
    public static String productIp(int productId) {
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?"":entity.getIp();
    }

    /**

     */
    public static int productSocketPort(int productId) {
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?0:Integer.parseInt(entity.getPort());
    }

    /**

     */
    public static String loadProductAlias(int productId) {
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?"":entity.getAlias();
    }

    /**

     */
    public static String loadSecondTypeName(int secondType) {
        if(secondType>0) {
            
            ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
            return entity == null ? "" : entity.getName();
        }else{
            return "";
        }
    }

    /**

     */
    public static boolean isOutStartGameTime(int productId) {
        boolean flag = false;
        
        long startGameGetCoinTime = ProductConfigMsg.startGameGetCoinTime*1000;
        if(startGameGetCoinTime==0){
            flag = true;
        }else {
            
            InnoProductOffLineMsg offLineMsg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
            long offTime = offLineMsg.getOffLineTime();
            if (offTime==0 || (offTime > 0 && (TimeUtil.getNowTime() - offTime) >= startGameGetCoinTime)) {
                flag = true;
            }
        }
        return flag;
    }

    /**

     */
    public static InnoProductWinPrizeMsgEntity initInnoProductWinPrizeMsgEntity(int userId, int productId,
            int awardType, int awardNum, int isStart) {
        InnoProductWinPrizeMsgEntity entity = new InnoProductWinPrizeMsgEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setAwardType(awardType);
        entity.setAwardNum(awardNum);
        entity.setIsStart(isStart);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void addRepairMsg(int productId, int breakType) {
        
        ProductGamingUserMsg msg = loadGamingUser(productId);
        int userId = msg.getUserId();
        if(userId>0 && CrossServerMsgUtil.isArcxServer(msg.getServerSideType())){
            
            ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
            
            ProductRepairEntity repairProductEntity = ProductRepairDao.getInstance().loadByProductId(productId);
            if(productInfoEntity!=null && repairProductEntity==null){
                
                ProductRepairDao.getInstance().insert(initProductRepairEntity(userId, productId, breakType));
            }else if(repairProductEntity!=null){

            }else{

            }
            
            OfficialAccountUtil.sendOfficalAccount(productId);
        }else{

        }
    }

    /**

     */
    private static ProductGamingUserMsg loadGamingUser(int productId){
        
        return ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
    }

    /**

     */
    private static ProductRepairEntity initProductRepairEntity(int userId, int productId, int breakType) {
        ProductRepairEntity entity = new ProductRepairEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setBreakType(breakType);
        entity.setStatus(YesOrNoEnum.NO.getCode());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }

    /**

     */
    public static boolean checkPushCoinProductGetCoin(int productId, InnerProductJsonMapMsg jsonMapMsg) {
        boolean flag = true;
        int result = jsonMapMsg.getDataMap().get("retNum") == null ? 0 :
                (int) jsonMapMsg.getDataMap().get("retNum");
        if (result > 0) {
            flag = isOutStartGameTime(productId);
            if (!flag) {

                        ProductConfigMsg.startGameGetCoinTime);
            }
        }
        return flag;
    }

    /**

     */
    public static boolean isLotteryProduct(int secondType) {
        
        ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
        return entity!=null && entity.getIsLotteryProduct()==YesOrNoEnum.YES.getCode();
    }

    /**

     */
    public static String loadInstruct(int productType, int secondType, String operateStr) {
        String instruct = "";
        if (productType == ProductTypeEnum.DOLL_MACHINE.getCode()) {
            
            instruct = getDollPushInstruct(operateStr, secondType);
        } else if (productType == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
            
            instruct = getCoinPusherInstruct(operateStr, secondType);
        } else if (productType == ProductTypeEnum.PRESENT_MACHINE.getCode()) {
            
            instruct = getPresentPushInstruct(operateStr, secondType);
        }
        return instruct;
    }

    /**

     */
    private static String getPresentPushInstruct(String operate, int levelType) {
        String sendStr = "";
        
        ProductSecondLevelTypeEntity levelEntity = ProductSecondLevelTypeDao.getInstance()
                .loadBySecondType(levelType);
        if (levelEntity != null) {
            String name = levelEntity.getInstructType() + "_" + operate;
            
            sendStr = InstructPresentDao.getInstance().loadByName(name);
        }
        return sendStr;
    }

    /**

     */
    private static String getDollPushInstruct(String operate, int levelType) {
        String sendStr = "";
        
        ProductSecondLevelTypeEntity levelEntity = ProductSecondLevelTypeDao.getInstance()
                .loadBySecondType(levelType);
        if (levelEntity != null) {
            
            if(operate.equals(ProductOperationEnum.CATCH.getCode()+"")){
                operate = ProductOperationEnum.STRONG_CATCH.getCode()+"";
            }
            String name = levelEntity.getInstructType() + "_" + operate;
            
            sendStr = InstructCatchDollDao.getInstance().loadByName(name);
        }
        return sendStr;
    }

    /**

     */
    private static String getCoinPusherInstruct(String operate, int levelType) {
        String sendStr = "";
        
        ProductSecondLevelTypeEntity levelEntity = ProductSecondLevelTypeDao.getInstance()
                .loadBySecondType(levelType);
        if (levelEntity != null) {
            String name = levelEntity.getInstructType() + "_" + operate;
            
            sendStr = InstructPushCoinDao.getInstance().loadByName(name);
        }
        return sendStr;
    }

    /**

     */
    public static int startGameTime(int productId) {
        
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        return gamingMsg==null?0:gamingMsg.getTime();
    }

    /**

     */
    public static LotteryMsg initUserLotteryMsg(int userId, int secondLevelType, int num, int addLotteryNum) {
        if (isLotteryProduct(secondLevelType)) {
            LotteryMsg msg = new LotteryMsg();
            
            UserLotteryMsg entity = UserLotteryMsgDao.getInstance().loadByMsg(userId, secondLevelType);
            if (entity != null) {
                msg.setAddLotteryNum(addLotteryNum);
                msg.setNum(entity.getLotteryNum());
                
                int maxNum = LotteryCoinPercentDao.getInstance().loadBySecondLevelType(secondLevelType);
                maxNum = maxNum > 0 ? maxNum : ProductConfigMsg.lotteryCoinExchange;
                msg.setMaxNum(maxNum);
                msg.setAddCoin(num);
            } else {

            }
            return msg;
        } else {

            return null;
        }
    }

    /**

     */
    public static void joinProductNotice(int productId, int userId) {
        List<Session> sessionList = ProductSocketUtil.dealOnlineSession(productId);
        if(sessionList.size()>0) {
            
            JSONObject jsonMap = initJoinProductNoticeMsg(productId, userId);
            
            sessionList.forEach(session-> SendWebsocketMsgUtil.sendBySession(WebSocketCmd.S2C_JOIN_PRODUCT,
                    ClientCode.SUCCESS.getCode(), session, jsonMap));
        }
    }

    /**

     */
    private static JSONObject initJoinProductNoticeMsg(int productId, int userId) {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("devId", productId);
        jsonMap.put("plyId", userId);
        
        ConciseUserMsg conciseUserMsg = UserUtil.initConciseUserMsg(userId);
        jsonMap.put("plyNm", conciseUserMsg.getPlyNm());
        jsonMap.put("plyPct", conciseUserMsg.getPlyPct());
        return jsonMap;
    }

    /**

     */
    public static boolean isCoinMultiExist(int productId, int coinMulti) {
        boolean flag = false;
        if(isInnoProduct(productId)) {
            int secondType = loadSecondType(productId);
            if (secondType > 0) {
                List<Integer> list = InnoPushCoinMultiDao.getInstance().loadBySecondType(secondType);
                if (list.size() > 0) {
                    flag = list.contains(coinMulti);
                }
            }
        }
        return flag;
    }

    /**

     */
    public static boolean isSpecifyMachine(int productId, int productType) {
        return loadProductType(productId)==productType;
    }

}
