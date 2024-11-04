package avatar.task.product.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoAwardScoreMultiMsg;
import avatar.global.enumMsg.product.innoMsg.InnoAwardScoreMultiEnum;
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
public class InnoAwardScoreMultiDealTask extends ScheduledTask {

    
    private InnoAwardScoreMultiMsg innoAwardScoreMultiMsg;

    
    private int productId;

    public InnoAwardScoreMultiDealTask(InnoAwardScoreMultiMsg innoAwardScoreMultiMsg, int productId) {

        this.innoAwardScoreMultiMsg = innoAwardScoreMultiMsg;
        this.productId = productId;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int userId = innoAwardScoreMultiMsg.getUserId();
                int awardMultiScore = innoAwardScoreMultiMsg.getAwardMulti();
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardScoreMultiMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardScoreMultiMsg.getServerSideType())){

                            InnoAwardScoreMultiEnum.getNameByCode(awardMultiScore));
                    
                    pushNotice(innoAwardScoreMultiMsg);
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
    private void pushNotice(InnoAwardScoreMultiMsg innoAwardScoreMultiMsg) {
        int userId = innoAwardScoreMultiMsg.getUserId();
        if(UserOnlineUtil.isOnline(userId)){
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);
            dataJson.put("awdMt", innoAwardScoreMultiMsg.getAwardMulti());
            
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_AWARD_SCORE_MULTI_NOTICE,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }
}
