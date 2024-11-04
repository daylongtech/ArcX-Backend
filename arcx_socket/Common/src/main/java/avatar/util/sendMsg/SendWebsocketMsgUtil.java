package avatar.util.sendMsg;

import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.net.session.Session;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class SendWebsocketMsgUtil {
    /**

     */
    public static void sendByUserId(int cmd, int status, int userId, JSONObject dataJson) {
        String accessToken = UserUtil.loadAccessToken(userId);
        if(!StrUtil.checkEmpty(accessToken)) {
            sendByAccessToken(cmd, status, accessToken, dataJson);
            if (!StrUtil.strToList(ConfigMsg.noListenSocket).contains(cmd)) {

                        cmd, userId, JsonUtil.mapToJson(dataJson));
            }
        }
    }

    /**

     */
    public static void sendByAccessToken(int cmd, int status, String accessToken, JSONObject dataJson) {
        if(!StrUtil.checkEmpty(accessToken)) {
            int userId = UserUtil.loadUserIdByToken(accessToken);
            Session session = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
            if (session != null) {
                Map<String, Object> map = new HashMap<>();
                
                map.put("errorCode", status);
                map.put("errorDesc", ClientCode.getErrorMsgByStatus(status));
                map.put("serverTime", TimeUtil.getNowTimeStr());
                map.put("serverDate", TimeUtil.getNowTime());
                map.put("serverMsg", dataJson);
                
                session.sendClientByAccessTokenRes(cmd, accessToken, map);
                if (!StrUtil.strToList(ConfigMsg.noListenSocket).contains(cmd)) {

                            cmd, userId, JsonUtil.mapToJson(map));
                }
            } else {

                
                UserOnlineUtil.delUserOnlineMsg(userId);
            }
        }
    }

    /**

     */
    public static void sendBySession(int cmd, int status, Session session, JSONObject dataJson) {
        if(session!=null){
            Map<String, Object> map = new HashMap<>();
            
            map.put("errorCode" , status);
            map.put("errorDesc", ClientCode.getErrorMsgByStatus(status));
            map.put("serverTime", TimeUtil.getNowTimeStr());
            map.put("serverDate", TimeUtil.getNowTime());
            map.put("serverMsg", dataJson);
            
            session.sendClientByAccessTokenRes(cmd, session.getAccessToken(), map);
            if (!StrUtil.strToList(ConfigMsg.noListenSocket).contains(cmd)) {

                        cmd, UserUtil.loadUserIdByToken(session.getAccessToken()), JsonUtil.mapToJson(map));
            }
        }
    }

    /**

     */
    public static void closeSocket(int userId) {
        String accessToken = UserUtil.loadAccessToken(userId);
        if(!StrUtil.checkEmpty(accessToken)) {
            while (true) {
                Session session = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
                if (session != null) {

                    session.close();
                } else {
                    break;
                }
            }
        }
    }
}
