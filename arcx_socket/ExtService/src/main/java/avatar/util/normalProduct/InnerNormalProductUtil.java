package avatar.util.normalProduct;

import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.basic.errrorCode.NormalProductClientCode;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.info.ProductAliasDao;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
public class InnerNormalProductUtil {
    /**

     */
    public static void dealMsg(String jsonStr) {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            
            if(checkEncode(jsonStr)) {
                InnerProductJsonMapMsg jsonMapMsg = dealJsonMsg(jsonStr);
                if (jsonMapMsg != null) {
                    
                    InnerNormalProductCmdUtil.dealCmdMsg(jsonMapMsg);
                } else {

                }
            }
        });
        cachedPool.shutdown();
    }

    /**

     */
    private static InnerProductJsonMapMsg dealJsonMsg(String jsonStr) {
        InnerProductJsonMapMsg msg = new InnerProductJsonMapMsg();
        try{
            if(!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if(jsonMap!=null && jsonMap.size()>0) {
                    
                    int cmd = (int) jsonMap.get("cmd");
                    msg.setCmd(cmd);
                    
                    int hostId = (int) jsonMap.get("userId");
                    msg.setHostId(hostId);
                    Map<String, Object> paramMap = (Map<String, Object>) jsonMap.get("param");
                    
                    Map<String, Object> dataMap = (Map<String, Object>) paramMap.get("data");
                    msg.setDataMap(dataMap);
                    
                    int status = (int) paramMap.get("status");
                    msg.setStatus(status);
                    
                    String time = paramMap.get("time").toString();
                    msg.setTime(time);
                    
                    ResponseGeneralMsg responseGeneralMsg = initResponseGeneralMsg(dataMap);
                    String alias = responseGeneralMsg.getAlias();
                    int productId = ProductAliasDao.getInstance().loadByAlias(alias);
                    msg.setProductId(productId);
                    msg.setUserId(responseGeneralMsg.getUserId());
                    msg.setResponseGeneralMsg(responseGeneralMsg);
                    
                    if(dataMap.containsKey("productGeneralParams")){
                        msg.setProductGeneralParamsMsg(initProductGeneralParamsMsg(dataMap));
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            return null;
        }
        return msg;
    }

    /**

     */
    public static ResponseGeneralMsg initResponseGeneralMsg(Map<String, Object> dataMap) {
        ResponseGeneralMsg msg = new ResponseGeneralMsg();
        if(dataMap.containsKey("responseGeneralMsg")) {
            Map<String, Object> responseGeneralMap = (Map<String, Object>) dataMap.get("responseGeneralMsg");
            msg.setServerSideType((int) responseGeneralMap.get("serverSideType"));
            msg.setAlias(responseGeneralMap.get("alias").toString());
            msg.setUserId((int) responseGeneralMap.get("userId"));
            msg.setUserName(responseGeneralMap.get("userName").toString());
            msg.setImgUrl(responseGeneralMap.get("imgUrl").toString());
        }
        return msg;
    }

    /**

     */
    public static ProductGeneralParamsMsg initProductGeneralParamsMsg(Map<String, Object> dataMap) {
        ProductGeneralParamsMsg msg = new ProductGeneralParamsMsg();
        Map<String, Object> productGeneralMap = (Map<String, Object>) dataMap.get("productGeneralParams");
        msg.setGameTime((int)productGeneralMap.get("gameTime"));
        msg.setOnProductTime(Long.parseLong(productGeneralMap.get("onProductTime").toString()));
        msg.setPushCoinOnTime(Long.parseLong(productGeneralMap.get("pushCoinOnTime").toString()));
        msg.setOperateState(productGeneralMap.containsKey("operateState")?
                (int)productGeneralMap.get("operateState"):0);
        return msg;
    }

    /**

     */
    private static boolean checkEncode(String jsonStr) {
        boolean flag = false;
        try{
            if(!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if (jsonMap != null && jsonMap.size() > 0) {
                    Map<String, Object> paramMap = (Map<String, Object>) jsonMap.get("param");
                    
                    Map<String, Object> dataMap = (Map<String, Object>) paramMap.get("data");
                    flag = InnerEnCodeUtil.checkEncode(dataMap);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**

     */
    public static void heart(InnerNormalProductClient client) {
        int hostId = InnerNormalProductConnectUtil.loadUid(client.getURI().getHost(), client.getURI().getPort());
        if(hostId>0) {
            SendNormalProductInnerMsgUtil.sendClientMsg(client, WebsocketInnerCmd.C2S_HEART, hostId, new HashMap<>());
        }
    }

    /**

     */
    public static int loadClientCode(int status) {
        if(status==NormalProductClientCode.PRODUCT_OCCUPY.getCode()){
            
            status = ClientCode.PRODUCT_OCCUPY.getCode();
        }else if(status== NormalProductClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode()){
            
            status = ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode();
        }else if(status==NormalProductClientCode.PRODUCT_EXCEPTION.getCode()){
            
            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        }else if(status==NormalProductClientCode.TWICE_OPERATE.getCode()){
            
            status = ClientCode.TWICE_OPERATE.getCode();
        }
        return status;
    }
}
