package avatar.task.product.innoMsg;

import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnoProductWinPrizeDealTask extends ScheduledTask {

    
    private int awardType;

    
    private int productId;

    
    private int userId;

    
    private int awardNum;

    
    private int isStart;

    public InnoProductWinPrizeDealTask(int awardType, int productId, int userId, int awardNum, int isStart) {

        this.awardType = awardType;
        this.productId = productId;
        this.userId = userId;
        this.awardNum = awardNum;
        this.isStart = isStart;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId)){
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);
            dataJson.put("devAwdTp", awardType);
            dataJson.put("awdAmt", awardNum);
            dataJson.put("stFlg", isStart);
            
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_INNO_WIN_PRIZE, ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }
}
