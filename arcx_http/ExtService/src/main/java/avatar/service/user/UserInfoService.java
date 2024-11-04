package avatar.service.user;

import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.user.opinion.UserOpinionDao;
import avatar.net.session.Session;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.UserCheckParamsUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserOpinionUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class UserInfoService {

    /**

     */
    public static void updateUserInfo(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        int status = UserCheckParamsUtil.updateUserInfo(map);
        if(ParamsUtil.isSuccess(status)) {
            
            int userId = ParamsUtil.userId(map);
            String nickName = ParamsUtil.stringParmasNotNull(map, "plyNm");
            int sex = ParamsUtil.intParmasNotNull(map, "sex");
            
            UserUtil.updateUserInfo(userId, nickName, sex);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void updateUserPassword(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        int status = UserCheckParamsUtil.updateUserPassword(map);
        if(ParamsUtil.isSuccess(status)) {
            
            int userId = ParamsUtil.userId(map);
            String email = ParamsUtil.stringParmasNotNull(map, "email");
            String password = ParamsUtil.stringParmasNotNull(map, "pwd");
            
            UserUtil.updateUserPassword(userId, email, password);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void userInfo(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            
            int userId = ParamsUtil.userId(map);
            
            UserUtil.loadUserInfo(userId, dataMap);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void userOpinion(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.userOpinion(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            String content = ParamsUtil.stringParmasNotNull(map, "fbCnt");
            String imgUrl = ParamsUtil.stringParmas(map, "fbPct");
            
            int num = UserOpinionDao.getInstance().loadDbByUserId(userId);
            if(num>= ConfigMsg.userMaxOpinion){
                status = ClientCode.OPINION_DAILY_MAX.getCode();
            }else{
                boolean flag = UserOpinionDao.getInstance().insert(userId, content, imgUrl);
                if(!flag){
                    status = ClientCode.FAIL.getCode();
                }
            }
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**

     */
    public static void communicateMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        
        int status = UserCheckParamsUtil.communicateMsg(map);
        if(ParamsUtil.isSuccess(status)) {
            String email = ParamsUtil.stringParmasNotNull(map, "email");
            UserOpinionUtil.dealCommunicateMsg(email);
        }
        
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

}
