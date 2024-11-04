package avatar.util.user;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.product.gamingMsg.LotteryMsg;
import avatar.data.user.attribute.UserEnergyMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.ImgUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class UserNoticePushUtil {
    /**

     */
    public static void userBalancePush(int userId) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));
        dataJson.put("axcAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.AXC.getCode()));//axc
        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_USER_BALANCE,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }

    /**

     */
    public static void pushLotteryNotice(int productId, int secondType, int userId, int addLotteryNum, int addCoinNum) {
        JSONObject dataJson = new JSONObject();
        LotteryMsg msg = ProductUtil.initUserLotteryMsg(userId, secondType, addCoinNum, addLotteryNum);
        if(msg!=null) {
            dataJson.put("devId", productId);
            dataJson.put("awdLotAmt", addLotteryNum);
            dataJson.put("lotAmy", msg.getNum());
            dataJson.put("amtUpp", msg.getMaxNum());
            dataJson.put("awdGdAmt", addCoinNum);
            
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PRODUCT_LOTTERY_PROGRESS,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }

    /**

     */
    public static void pushPileTowerAward(int userId, int productId, int awardNum, int awardImgId) {
        if(UserOnlineUtil.isOnline(userId)) {
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);
            dataJson.put("awdTbln", initPileAwardList(awardNum, awardImgId));
            
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PILE_TOWER_AWARD,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }

    /**

     */
    private static List<GeneralAwardMsg> initPileAwardList(int awardNum, int awardImgId) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(CommodityTypeEnum.GOLD_COIN.getCode());
        msg.setAwdPct(MediaUtil.getMediaUrl(ImgUtil.loadAwardImg(awardImgId)));
        msg.setAwdAmt(awardNum);
        retList.add(msg);
        return retList;
    }

    /**

     */
    public static void pileTower(int userId, int productId, int isStop) {
        if(UserOnlineUtil.isOnline(userId)) {
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);
            dataJson.put("stpFlg", isStop);
            
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PILE_TOWER_STATUS_NOTICE,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }

    /**

     */
    public static void kickOutProductPush(int userId, int productId) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("devId", productId);
        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_KICK_OUT_PRODUCT, ClientCode.SUCCESS.getCode(),
                userId, dataJson);
    }

    /**

     */
    public static void systemNoticePush(int userId, String content) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("ntcCt", content);
        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_SYSTEM_NOTICE, ClientCode.SUCCESS.getCode(),
                userId, dataJson);
    }

    /**

     */
    public static void userEnergyMsg(int userId, long changeNum) {
        UserEnergyMsg msg = UserAttributeUtil.userEnergyMsg(UserAttributeMsgDao.getInstance().loadMsg(userId));
        JSONObject dataJson = new JSONObject();
        dataJson.put("cnAmt", msg.getCnAmt());
        dataJson.put("ttAmt", msg.getTtAmt());
        dataJson.put("cgAmt", changeNum);
        dataJson.put("lfTm", msg.getLfTm());
        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_ENERGY_MSG, ClientCode.SUCCESS.getCode(),
                userId, dataJson);
    }

    /**

     */
    public static void pushDragonAwardMsg(int userId, JSONObject dataJson) {

        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_INNO_DRAGON_AWARD_MSG,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }
}
