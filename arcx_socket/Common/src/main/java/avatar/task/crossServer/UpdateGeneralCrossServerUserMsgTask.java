package avatar.task.crossServer;

import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.module.crossServer.CrossServerUserMsgDao;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class UpdateGeneralCrossServerUserMsgTask extends ScheduledTask {

    
    private GeneralCrossServerUserMsg userMsg;

    public UpdateGeneralCrossServerUserMsgTask(GeneralCrossServerUserMsg userMsg) {

        this.userMsg = userMsg;
    }

    @Override
    public void run() {
        try {
            int serverSideType = userMsg.getServerSideType();
            int userId = userMsg.getUserId();
            
            GeneralCrossServerUserMsg msg = CrossServerMsgUtil.loadGeneralCrossServerUserMsg(
                    serverSideType, userId);
            if(msg!=null){
                CrossServerUserMsgDao.getInstance().setCache(serverSideType, userId, msg);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }

}
