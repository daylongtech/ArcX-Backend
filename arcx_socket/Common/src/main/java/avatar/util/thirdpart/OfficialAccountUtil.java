package avatar.util.thirdpart;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.repair.ProductRepairConfigEntity;
import avatar.entity.product.repair.ProductRepairUserOfficalEntity;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.code.thirdpart.OfficialAccountMsg;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.repair.ProductRepairConfigDao;
import avatar.module.product.repair.ProductRepairUserOfficalDao;
import avatar.module.product.repair.RepairOfficalAccountTokenDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class OfficialAccountUtil {
    /**

     */
    public static void sendOfficalAccount(int productId){
        
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        if(productInfoEntity!=null) {
            String productName = "(" + ProductUtil.loadProductName(productId) + ")";
            
            List<ProductRepairUserOfficalEntity> userList = ProductRepairUserOfficalDao.getInstance().loadAll();
            if (userList != null && userList.size() > 0) {
                for (ProductRepairUserOfficalEntity entity : userList) {
                    String openId = entity.getOpenid();
                    String url = loadSendAccountUrl();
                    if(!StrUtil.checkEmpty(url)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("touser", openId);
                        map.put("template_id", "IH5NRdH4ayD30gmVfXYZU-3B7qDikETuYa9RaEQhnx4");
                        map.put("url", url);
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> firstData = new HashMap<>();

                        firstData.put("color", "#FF4635");
                        data.put("first", firstData);
                        Map<String, Object> deviceData = new HashMap<>();
                        deviceData.put("value", "【"+ ConfigMsg.appName+"】"+productName);
                        deviceData.put("color", "#FF4635");
                        data.put("device", deviceData);
                        Map<String, Object> timeData = new HashMap<>();
                        timeData.put("value", TimeUtil.getNowTimeStr());
                        timeData.put("color", "#FF4635");
                        data.put("time", timeData);
                        Map<String, Object> remarkData = new HashMap<>();

                        remarkData.put("color", "#FF4635");
                        data.put("remark", remarkData);
                        
                        map.put("data", data);
                        String json = JsonUtil.mapToJson(map);
                        
                        String accessToken = getAccessToken();
                        if (!StrUtil.checkEmpty(accessToken)) {

                            
                            String str = HttpClientUtil.sendHttpPost(OfficialAccountMsg.officalAccountUrl + accessToken, json);

                            if (!StrUtil.checkEmpty(str)) {
                                Map<String, Object> retMap = JsonUtil.strToMap(str);
                                if (retMap != null && retMap.size() > 0 && retMap.containsKey("errcode")) {
                                    int errcode = Integer.parseInt(retMap.get("errcode").toString());
                                    if (errcode == 40001) {

                                        accessToken = loadNewAcceddToken();
                                        if (!StrUtil.checkEmpty(accessToken)) {
                                            RepairOfficalAccountTokenDao.getInstance().setCache(accessToken);
                                            
                                            sendOfficalAccount(productId);
                                        }
                                    }
                                }
                            }
                        } else {

                        }
                    }else{

                    }
                }
            }
        }else{

        }
    }

    /**

     */
    private static String loadSendAccountUrl() {
        String url = "";
        
        ProductRepairConfigEntity configEntity = ProductRepairConfigDao.getInstance().loadMsg();
        if(configEntity!=null && !StrUtil.checkEmpty(configEntity.getOfficalUrl())) {
            
            url = configEntity.getOfficalUrl();
        }
        return url;
    }

    /**

     */
    private static String getAccessToken(){
        
        String token = RepairOfficalAccountTokenDao.getInstance().loadToken();
        if(StrUtil.checkEmpty(token)) {
            String url = OfficialAccountMsg.accessTokenUrl + "&appid=" +
                    OfficialAccountMsg.wx_appid + "&secret=" + OfficialAccountMsg.wx_appsecret;
            String retMsg = HttpClientUtil.sendHttpGet(url);

            if (!StrUtil.checkEmpty(retMsg)) {
                Map<String, Object> map = JsonUtil.strToMap(retMsg);
                if(map==null){

                    token = "";
                }else {
                    token = map.get("access_token").toString();
                    
                    RepairOfficalAccountTokenDao.getInstance().setCache(token);
                }
            } else {
                token = "";
            }
        }
        return token;
    }

    /**

     */
    private static String loadNewAcceddToken(){
        String token;
        
        String url = OfficialAccountMsg.accessTokenUrl + "&appid=" + OfficialAccountMsg.wx_appid + "&secret=" + OfficialAccountMsg.wx_appsecret;
        String retMsg = HttpClientUtil.sendHttpGet(url);

        if (!StrUtil.checkEmpty(retMsg)) {
            Map<String, Object> map = JsonUtil.strToMap(retMsg);
            if(map==null){

                token = "";
            }else {
                token = map.get("access_token").toString();
                
                RepairOfficalAccountTokenDao.getInstance().setCache(token);
            }
        } else {
            token = "";
        }
        return token;
    }
}
