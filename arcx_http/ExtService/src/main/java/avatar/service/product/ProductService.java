package avatar.service.product;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.info.ProductMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.basicConfig.basic.ProductConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.info.ProductSecondTypeFoldingListDao;
import avatar.module.product.info.ProductTypeFordingListDao;
import avatar.module.product.repair.ProductRepairDao;
import avatar.net.session.Session;
import avatar.task.product.RepairProductDealTask;
import avatar.util.basic.ImgUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ProductCheckParamsUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ListUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class ProductService {
    /**

     */
    public static void productMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = ProductCheckParamsUtil.checkProductStatus(map);
        if (ParamsUtil.isSuccess(status)) {
            int productId = ParamsUtil.productId(map);
            int userId = ParamsUtil.userId(map);
            String versionCode = ParamsUtil.versionCode(map);
            
            ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
            dataMap.put("devId", productId);
            dataMap.put("devNm", entity.getProductName());
            
            int productType = entity.getProductType();
            dataMap.put("devTp", productType);
            dataMap.put("sndTp", entity.getSecondType());
            dataMap.put("devIdx", ProductUtil.productIndex(productId, entity.getLiveUrl()));
            dataMap.put("devPct", ImgUtil.loadProductImg(entity.getImgProductId()));
            dataMap.put("lvTp", entity.getLiveType());
            dataMap.put("lvAds", entity.getLiveUrl());
            dataMap.put("wbLvAds", StrUtil.checkEmpty(entity.getWebLiveUrl())?entity.getLiveUrl():entity.getWebLiveUrl());
            dataMap.put("cmdTp", entity.getCostCommodityType());
            dataMap.put("csAmt", entity.getCost());
            dataMap.put("devSts", ProductUtil.loadGamingUser(productId).getUserId()>0?
                    YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
            dataMap.put("devDtPct", ImgUtil.loadProductDetailImg(entity.getImgProductDetailId()));
            ProductGamingUserMsg gamingUserMsg = ProductUtil.loadGamingUser(productId);
            
            if(gamingUserMsg.getUserId()>0) {
                dataMap.put("gmPly", CrossServerMsgUtil.initConciseServerUserMsg(gamingUserMsg));
            }
            
            ProductUtil.coinMultiMsg(userId, productId, versionCode, dataMap);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void productList(Map<String, Object> map, Session session) {
        List<ProductMsg> retList = new ArrayList<>();
        int status = ProductCheckParamsUtil.productList(map);
        if(ParamsUtil.isSuccess(status)) {
            int productType = ParamsUtil.intParmasNotNull(map, "devTp");
            
            List<Integer> productList = ProductTypeFordingListDao.getInstance().loadList(productType);
            if(productList.size()>0) {
                
                List<Integer> loadList = ListUtil.getPageList(ParamsUtil.pageNum(map),
                        ParamsUtil.pageSize(map), productList);
                loadList.forEach(pId -> retList.add(ProductUtil.ProductMsg(pId)));
            }
        }
        
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
    }

    /**

     */
    public static void fastJoinProduct(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = ProductCheckParamsUtil.fastJoinProduct(map);
        if (ParamsUtil.isSuccess(status)) {
            int productType = ParamsUtil.intParmasNotNull(map, "devTp");
            ProductMsg productMsg = null;
            int retProductId = ProductUtil.loadFastJoinProduct(productType, dataMap);
            
            if (retProductId > 0) {
                productMsg = ProductUtil.ProductMsg(retProductId);
            }
            dataMap.put("devIfo", productMsg);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void gamingProduct(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = CheckParamsUtil.checkAccessToken(map);
        if (ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            dataMap.put("devId", UserOnlineUtil.loadGamingProduct(userId));
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void repairProduct(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = ProductCheckParamsUtil.repairProduct(map);
        int productId = 0;
        int userId = 0;
        if(ParamsUtil.isSuccess(status)){
            userId = ParamsUtil.userId(map);
            productId = ParamsUtil.productId(map);
            
            ProductRepairEntity entity = ProductRepairDao.getInstance().loadByProductId(productId);
            if(entity!=null && (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getCreateTime()))
                    < ProductConfigMsg.productRepairIntervalTime*1000){
                status = ClientCode.REPAIR_MSG_EXIST.getCode();
            }else{
                
                boolean flag = ProductRepairDao.getInstance().insert(ProductUtil.
                        initProductRepairEntity(userId, productId, 0));
                if(!flag){
                    status = ClientCode.FAIL.getCode();
                }
            }
            
            if(ParamsUtil.isSuccess(status)){
                UserOperateLogUtil.repairProduct(userId, productId);
            }
        }
        
        if(ParamsUtil.isSuccess(status)){
            
            SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS_REPAIR.getCode(), dataMap);
            
            SchedulerSample.delayed(10, new RepairProductDealTask(productId, userId));
        }else {
            SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
        }
    }
}
