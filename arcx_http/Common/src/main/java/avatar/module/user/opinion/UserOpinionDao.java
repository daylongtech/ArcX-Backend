package avatar.module.user.opinion;

import avatar.entity.user.opinion.UserOpinionEntity;
import avatar.global.enumMsg.basic.system.DealStatusEnum;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**

 */
public class UserOpinionDao {
    private static final UserOpinionDao instance = new UserOpinionDao();
    public static final UserOpinionDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public boolean insert(int userId, String opinion, String imgUrl){
        UserOpinionEntity entity = new UserOpinionEntity();
        entity.setUserId(userId);
        entity.setOpinion(opinion);
        entity.setImgUrl(imgUrl);
        entity.setDealOpinion("");
        entity.setDealBackUserId(0);
        entity.setStatus(DealStatusEnum.NO_DEAL.getCode());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        entity.setUpdateTime(TimeUtil.getNowTimeStr());
        return GameData.getDB().insert(entity);
    }

    /**

     */
    public int loadDbByUserId(int userId) {
        String sql = "select IFNULL(count(id),0) from user_opinion where user_id=? and create_time>=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{userId, TimeUtil.getTodayTime()});
        return StrUtil.listNum(list);
    }
}
