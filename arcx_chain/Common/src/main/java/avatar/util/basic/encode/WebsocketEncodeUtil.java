package avatar.util.basic.encode;

import avatar.global.enumMsg.system.ClientCode;
import avatar.module.user.info.UserIdMacDao;
import avatar.util.LogUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.MD5Util;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

/**

 */
public class WebsocketEncodeUtil {
    /**

     * MD5(timestamp+code+key+code)
     */
    public static int checkEncode(String accessToken, boolean checkTokenFlag, JSONObject jsonObject) {
        int status = ClientCode.SUCCESS.getCode();
        int userId = 0;
        try{
            if(checkTokenFlag || !UserUtil.isTourist(accessToken)) {
                status = UserUtil.checkAccessToken(accessToken);
            }
            if(ParamsUtil.isConfirm(status)) {
                String outsource1Pas = jsonObject.getString("outsource1Pas");
                String verifyStr = jsonObject.getLongValue("timestamp") + "+";
                String mac = jsonObject.containsKey("deviceId") ? jsonObject.getString("deviceId") : "";
                int randomNum = jsonObject.getIntValue("randomNum");
                if (!StrUtil.checkEmpty(accessToken) && !UserUtil.isTourist(accessToken)) {
                    userId = UserUtil.loadUserIdByToken(accessToken);
                    if(userId>0) {
                        String userMac = UserIdMacDao.getInstance().loadByUserId(userId);
                        if(!StrUtil.checkEmpty(userMac)){
                            mac = userMac;
                        }
                    }
                }
                if (!StrUtil.checkEmpty(mac)) {
                    verifyStr += firstCode(mac, randomNum);
                    verifyStr += ("+" + accessToken);
                    verifyStr += "+";
                    verifyStr += secondCode(mac);
                    if (!MD5Util.MD5(verifyStr).equals(outsource1Pas)) {
                        status = ClientCode.VERIFY_SIGN_ERROR.getCode();
                    }
                }else{
                    status = ClientCode.VERIFY_SIGN_ERROR.getCode();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            status = ClientCode.VERIFY_SIGN_ERROR.getCode();
        }
        if(ParamsUtil.isVerifyError(status)){

        }
        return status;
    }

    /**



     */
    private static String firstCode(String mac, int randomNum) {
        int totalNum = 0;
        for(int i=0;i<10;i++){
            String code = String.valueOf(mac.charAt(i));
            if(StrUtil.isNumber(code)){
                totalNum += Integer.parseInt(code);
            }
        }
        if(totalNum==0){
            totalNum = 1;
        }
        return totalNum*randomNum+"";
    }

    /**



     */
    private static String secondCode(String mac) {
        return mac.substring(1, 4);
    }
}
