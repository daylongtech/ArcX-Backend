package avatar.util.user;

import avatar.entity.user.opinion.CommunicateEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.opinion.CommunicateMsgDao;
import avatar.util.system.TimeUtil;

/**

 */
public class UserOpinionUtil {
    /**

     */
    public static void dealCommunicateMsg(String email) {
        
        int emailSize = CommunicateMsgDao.getInstance().loadDbNum(email);
        if(emailSize==0){
            CommunicateMsgDao.getInstance().insert(initCommunicateEntity(email));
        }
    }

    /**

     */
    private static CommunicateEntity initCommunicateEntity(String email){
        CommunicateEntity entity = new CommunicateEntity();
        entity.setEmail(email);
        entity.setDealBackUserId(0);
        entity.setDealFlag(YesOrNoEnum.NO.getCode());
        entity.setCommentMsg("");
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime("");
        return entity;
    }

}
