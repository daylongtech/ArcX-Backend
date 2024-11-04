package avatar.util.normalProduct;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.data.product.normalProduct.RequestGeneralMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.info.UserInfoDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class InnerNormalProductWebsocketUtil {
    /**

     */
    public static RequestGeneralMsg initRequestGeneralMsg(int userId, int productId) {
        RequestGeneralMsg msg = new RequestGeneralMsg();
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());
        msg.setAlias(ProductUtil.loadProductAlias(productId));
        if(userId>0) {
            
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            msg.setUserId(userId);
            msg.setUserName(userInfoEntity == null ? "" : userInfoEntity.getNickName());
            msg.setImgUrl(userInfoEntity == null ? "" : MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));
        }else{
            msg.setUserId(0);
            msg.setUserName("");
            msg.setImgUrl("");
        }
        return msg;
    }

    /**

     */
    private static RequestGeneralMsg initRequestGeneralMsg(ProductGamingUserMsg msg) {
        RequestGeneralMsg requestGeneralMsg = new RequestGeneralMsg();
        requestGeneralMsg.setServerSideType(msg.getServerSideType());
        requestGeneralMsg.setUserId(msg.getUserId());
        requestGeneralMsg.setUserName(msg.getUserName());
        requestGeneralMsg.setImgUrl(msg.getImgUrl());
        requestGeneralMsg.setAlias(ProductUtil.loadProductAlias(msg.getProductId()));
        return requestGeneralMsg;
    }

    /**

     */
    public static int sendOperateMsg(int cmd, String host, int port, Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();
        
        int hostId = InnerNormalProductConnectUtil.loadUid(host, port);
        String linkMsg = host+port+hostId;
        
        InnerNormalProductClient client = InnerNormalProductOperateUtil.loadClient(host, port, linkMsg);
        if(client.isOpen()) {
            if (hostId > 0) {
                SendNormalProductInnerMsgUtil.sendClientMsg(client, cmd, hostId, map);
            }else{

                status = ClientCode.PRODUCT_EXCEPTION.getCode();
            }
        }else{

            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        }
        return status;
    }

    /**

     */
    public static void sendMsg(int cmd, String host, int port, RequestGeneralMsg requestGeneralMsg) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("requestGeneralMsg", requestGeneralMsg);
        sendOperateMsg(cmd, host, port, dataMap);
    }

    /**

     */
    public static Map<String, Object> initOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));
        map.put("operateStruct", initOperateStruct(productId, operateList));
        return map;
    }

    /**

     */
    private static Map<String, Object> initOperateStruct(int productId, List<String> operateList) {
        
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        int productType = productInfoEntity.getProductType();
        int secondType = productInfoEntity.getSecondType();
        Map<String, Object> retMap = new HashMap<>();
        if(operateList!=null && operateList.size()>0){
            operateList.forEach(operateStr->{
                String operate = operateStr;
                if(operateStr.contains("_")){
                    
                    operate = operateStr.split("_")[0];
                }
                if(!retMap.containsKey(operate)){
                    
                    String instruct = ProductUtil.loadInstruct(productType, secondType, operateStr);
                    if(!StrUtil.checkEmpty(instruct)){
                        retMap.put(operate, instruct);
                    }
                }
            });
        }
        return retMap;
    }

    /**

     */
    public static Map<String, Object> initProductOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));
        map.put("operateStruct", initOperateStruct(productId, operateList));
        return map;
    }

    /**

     */
    public static Map<String, Object> initGetCoinOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));
        map.put("operateStruct", initOperateStruct(productId, operateList));
        map.put("productGeneralParams", initProductGeneralParams(productId, 0));
        return map;
    }

    /**

     */
    private static ProductGeneralParamsMsg initProductGeneralParams(int productId, int operateState) {
        ProductGeneralParamsMsg retMsg = new ProductGeneralParamsMsg();
        
        ProductRoomMsg msg = ProductRoomDao.getInstance().loadByProductId(productId);
        retMsg.setGameTime(ProductUtil.startGameTime(productId));
        retMsg.setOnProductTime(msg.getOnProductTime());
        retMsg.setPushCoinOnTime(msg.getPushCoinOnTime());
        retMsg.setOperateState(operateState);
        return retMsg;
    }

    /**

     */
    public static Map<String, Object> initSettlementOperateMap(int productId, int userId, int operateState,
            List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));
        map.put("operateStruct", initOperateStruct(productId, operateList));
        map.put("productGeneralParams", initProductGeneralParams(productId, operateState));
        return map;
    }

    /**

     */
    public static Map<String, Object> initDownCatchOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));
        map.put("operateStruct", initOperateStruct(productId, operateList));
        map.put("productGeneralParams", initProductGeneralParams(productId, 0));
        return map;
    }

    /**

     */
    public static Map<String, Object> initProductMsgOperateMap(ProductGamingUserMsg msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(msg));
        return map;
    }

}
