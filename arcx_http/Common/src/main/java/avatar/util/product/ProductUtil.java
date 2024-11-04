package avatar.util.product;

import avatar.data.crossServer.ConciseServerUserMsg;
import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.info.ProductMsg;
import avatar.data.product.innoMsg.ProductCoinMultiMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.entity.product.productType.ProductSecondLevelTypeEntity;
import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.enumMsg.product.ProductTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.gaming.ProductGamingUserMsgDao;
import avatar.module.product.info.ProductGroupListDao;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.info.ProductTypeFordingListDao;
import avatar.module.product.innoMsg.InnoPushCoinMultiDao;
import avatar.module.product.innoMsg.InnoPushCoinWindowDao;
import avatar.module.product.productType.ProductSecondLevelTypeDao;
import avatar.util.basic.ImgUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**

 */
public class ProductUtil {
    /**

     */
    public static int productIndex(int productId, String liveUrl) {
        int index = 1;
        List<Integer> productIdList = ProductGroupListDao.getInstance().loadByLiveUrl(liveUrl);
        if(productIdList.size()>0){
            for(int i=0;i<productIdList.size();i++){
                if(productIdList.get(i)==productId){
                    index = i+1;
                    break;
                }
            }
        }
        return index;
    }

    /**

     */
    public static ProductGamingUserMsg loadGamingUser(int productId){
        
        return ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
    }

    /**

     */
    public static ProductGamingUserMsg initProductGamingUserMsg(int productId) {
        ProductGamingUserMsg msg = new ProductGamingUserMsg();
        msg.setServerSideType(0);
        msg.setProductId(productId);
        msg.setUserId(0);
        msg.setUserName("");
        msg.setImgUrl("");
        return msg;
    }

    /**

     */
    public static void coinMultiMsg(int userId, int productId, String versionCode, Map<String, Object> dataMap) {
        if (isInnoProduct(productId)) {
            int secondType = loadSecondType(productId);
            
            InnoPushCoinWindowMsgEntity windowMsgEntity = InnoPushCoinWindowDao.getInstance().
                    loadBySecondType(secondType);
            if (windowMsgEntity != null) {
                ProductCoinMultiMsg msg = new ProductCoinMultiMsg();
                msg.setWdwPct(MediaUtil.getMediaUrl(windowMsgEntity.getImgUrl()));
                List<Integer> multiList = InnoPushCoinMultiDao.getInstance().loadBySecondType(secondType);
                boolean unlockFlag = InnoProductUtil.isUnlockVersion(versionCode);
                msg.setMulTbln(InnoProductUtil.productMultiLimitList(userId, productId, secondType, multiList, unlockFlag));
                dataMap.put("ptyMul", msg);
            }
        }
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
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?0:entity.getSecondType();
    }

    /**

     */
    public static ProductMsg ProductMsg(int productId) {
        ProductMsg msg = new ProductMsg();
        msg.setDevId(productId);
        msg.setDevNm(loadProductName(productId));
        
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        msg.setDevPct(ImgUtil.loadProductImg(entity.getImgProductId()));
        msg.setCsAmt(entity.getCost());
        
        List<ConciseServerUserMsg> userImgUrlList = new ArrayList<>();
        
        
        ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(gamingUserMsg!=null && gamingUserMsg.getUserId()>0) {
            userImgUrlList.add(CrossServerMsgUtil.initConciseServerUserMsg(gamingUserMsg));
        }
        if(userImgUrlList.size()>0){
            msg.setPlyTbln(userImgUrlList);
        }
        
        msg.setDevTbln(ProductGroupListDao.getInstance().loadByLiveUrl(entity.getLiveUrl()));
        return msg;
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
    public static int loadFastJoinProduct(int productType, Map<String, Object> dataMap) {
        int retProductId = 0;
        List<Integer> productIdList = ProductTypeFordingListDao.getInstance().loadList(productType);
        if(productIdList.size()>0){
            retProductId = loadNobodyProduct(productIdList, dataMap);
        }
        return retProductId;
    }

    /**

     */
    private static int loadNobodyProduct(List<Integer> productIdList,  Map<String, Object> dataMap){
        int retProductId = 0;
        Collections.shuffle(productIdList);
        int oriRetPId = 0;
        
        for(int productId : productIdList){
            ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
            if(gamingUserMsg.getUserId()==0){
                retProductId = productId;
                oriRetPId = productId;
                break;
            }
        }
        if(retProductId==0){
            retProductId = productIdList.get(0);
        }
        if(dataMap!=null){
            dataMap.put("fulFlg", oriRetPId==0?YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
        }
        return retProductId;
    }

    /**

     */
    public static String productLog(int productId){
        return productId+"-"+getProductTypeName(productId)+"-"+
                loadProductName(productId);
    }

    /**

     */
    private static String getProductTypeName(int productId) {
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
    public static ProductRepairEntity initProductRepairEntity(int userId, int productId, int breakType) {
        ProductRepairEntity entity = new ProductRepairEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setBreakType(breakType);
        entity.setStatus(YesOrNoEnum.NO.getCode());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }

}
