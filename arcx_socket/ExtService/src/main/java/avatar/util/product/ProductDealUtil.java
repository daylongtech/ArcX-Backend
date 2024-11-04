package avatar.util.product;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.product.gamingMsg.DollAwardCommodityMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoReceiveProductOperateMsg;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.CatchDollResultEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.InnoReturnCoinTask;
import avatar.task.innoMsg.SyncInnoAutoPushCoinTask;
import avatar.util.LogUtil;
import avatar.util.basic.general.AwardUtil;
import avatar.util.basic.general.ImgUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static avatar.util.product.ProductUtil.isInnoProduct;

/**

 */
public class ProductDealUtil {
    /**

     */
    public static void updateProductTime(int productId, int userId, ProductRoomMsg msg) {

                ProductUtil.loadProductName(productId), userId);
        long nowTime = TimeUtil.getNowTime();
        msg.setPushCoinOnTime(TimeUtil.getNowTime());
        
        ProductRoomDao.getInstance().setCache(productId, msg);
        
        productRefreshTime(userId, productId, nowTime);
    }

    /**

     */
    public static void productRefreshTime(int userId, int productId, long time) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("devId", productId);
        dataJson.put("rfTm", time);
        dataJson.put("lfTm", ProductUtil.loadProductLeftTime(time, productId));
        
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_REFRESH_TIME,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }

    /**

     */
    public static void pushCoinFailDeal(int userId, int productId, InnoReceiveProductOperateMsg productOperateMsg) {
        
        SchedulerSample.delayed(5, new InnoReturnCoinTask(productId, userId,
                productOperateMsg.getOnProductTime()));
    }

    /**

     */
    public static void socketOffDeal(int userId){
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if (list!=null && list.size()>0) {
            list.forEach(entity->{
                int productId = entity.getProductId();
                
                UserOnlineUtil.onlineMsgNoOnline(userId, productId);
                
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                        2000);
                try {
                    if (lock.lock()) {
                        
                        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                        if (productRoomMsg != null && productRoomMsg.getGamingUserId() == userId) {
                            
                            if(isInnoProduct(productId)){
                                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.NO.getCode()));
                            }
                        }
                    }
                } catch (Exception e) {
                    ErrorDealUtil.printError(e);
                } finally {
                    lock.unlock();
                }
            });
        }
    }

    /**

     */
    public static List<GeneralAwardMsg> dealMachineAward(int userId, int productId, int result, int getType,
            DollAwardCommodityMsg awardMsg) {
        List<GeneralAwardMsg> awardList = new ArrayList<>();
        if(result== CatchDollResultEnum.WIN.getCode()){
            
            UserAttributeUtil.productAwardEnergyDeal(userId, productId, getType, awardList);
            
            if(awardMsg!=null){
                awardList.add(AwardUtil.initGeneralAwardMsg(awardMsg.getCommodityType(),
                        awardMsg.getAwardId(), ImgUtil.productAwardImg(awardMsg.getAwardImgId()), awardMsg.getAwardNum()));
            }
        }
        return awardList;
    }
}
