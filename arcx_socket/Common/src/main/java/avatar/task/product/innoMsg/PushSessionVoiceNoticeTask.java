package avatar.task.product.innoMsg;

import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class PushSessionVoiceNoticeTask extends ScheduledTask {

    
    private JSONObject jsonMap;

    
    private int productId;

    
    private Session session;

    public PushSessionVoiceNoticeTask(JSONObject jsonMap, int productId, Session session) {

        this.jsonMap = jsonMap;
        this.productId = productId;
        this.session = session;
    }

    @Override
    public void run() {
        jsonMap.put("devId", productId);
        
        SendWebsocketMsgUtil.sendBySession(WebSocketCmd.S2C_PRODUCT_VOICE_NOTICE,
                ClientCode.SUCCESS.getCode(), session, jsonMap);
    }
}
