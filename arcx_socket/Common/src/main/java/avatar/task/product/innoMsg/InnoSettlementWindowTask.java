package avatar.task.product.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoSettlementWindowMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class InnoSettlementWindowTask extends ScheduledTask {

    
    private InnoSettlementWindowMsg innoSettlementWindowMsg;

    
    private int productId;

    public InnoSettlementWindowTask(InnoSettlementWindowMsg innoSettlementWindowMsg, int productId) {

        this.innoSettlementWindowMsg = innoSettlementWindowMsg;
        this.productId = productId;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoSettlementWindowMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoSettlementWindowMsg.getServerSideType())){

                    
                    pushNotice(innoSettlementWindowMsg);
                }else{

                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**

     */
    private void pushNotice(InnoSettlementWindowMsg innoSettlementWindowMsg) {
        int userId = innoSettlementWindowMsg.getUserId();
        if(UserOnlineUtil.isOnline(userId)){
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);
            
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_SETTLEMENT_WINDOW_NOTICE,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }
}
