package avatar.util.checkParams;

import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

import java.util.Map;

/**

 */
public class CheckParamsUtil {
    /**

     */
    public static int checkAccessToken(Map<String, Object> map){
        int status = ClientCode.SUCCESS.getCode();
        
        String accessToken = ParamsUtil.accessToken(map);
        if(!StrUtil.checkEmpty(accessToken)){
            
            int userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
            if(userId==0){
                status = ClientCode.ACCESS_TOKEN_ERROR.getCode();
            }else if(userId>0 && UserUtil.loadUserStatus(userId)!= UserStatusEnum.NORMAL.getCode()){
                status = ClientCode.ACCOUNT_DISABLED.getCode();
            }else{
                
                UserTokenMsgEntity tokenMsgEntity = UserTokenMsgDao.getInstance().loadByUserId(userId);
                if(tokenMsgEntity==null || tokenMsgEntity.getAccessOutTime()<= TimeUtil.getNowTime()){
                    status = ClientCode.ACCESS_TOKEN_OUT_TIME.getCode();
                }
            }
        }else{
            status = ClientCode.LOGIN_PLEASE.getCode();
        }
        return status;
    }

    /**

     */
    public static int checkPage(Map<String, Object> map){
        int status = ClientCode.SUCCESS.getCode();
        try {
            int pageNum = ParamsUtil.pageNum(map);
            int pageSize = ParamsUtil.pageSize(map);
            if(pageNum<=0 || pageSize<=0){
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    public static int checkAccessTokenPage(Map<String, Object> map){
        int status = checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            try {
                int pageNum = ParamsUtil.pageNum(map);
                int pageSize = ParamsUtil.pageSize(map);
                if (pageNum <= 0 || pageSize <= 0) {
                    status = ClientCode.PARAMS_ERROR.getCode();
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }
}
