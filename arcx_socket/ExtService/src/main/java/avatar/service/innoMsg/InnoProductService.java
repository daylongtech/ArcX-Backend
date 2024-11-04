package avatar.service.innoMsg;

import avatar.data.product.innoMsg.InnoDragonMsg;
import avatar.data.product.innoMsg.InnoGetCoinMsg;
import avatar.data.product.innoMsg.InnoProductAwardMsg;
import avatar.data.product.innoMsg.InnoProductMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.info.ProductAliasDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.task.product.innoMsg.InnoVoiceNoticeTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import com.alibaba.fastjson.JSONObject;

/**

 */
public class InnoProductService {
    /**

     */
    public static void describeProductMsg(InnoProductMsg innoProductMsg) {
        String alias = innoProductMsg.getAlias();
        int productId = ProductAliasDao.getInstance().loadByAlias(alias);
        if(productId>0){
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                    2000);
            try {
                if (lock.lock()) {
                    
                    ProductGamingUtil.updateGamingUserMsg(productId, innoProductMsg);
                    
                    SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
    }

    /**

     */
    public static void describeGetCoinMsg(InnoGetCoinMsg innoGetCoinMsg) {
        int productId = ProductAliasDao.getInstance().loadByAlias(innoGetCoinMsg.getAlias());
        if(productId>0) {
            int cost = ProductUtil.productCost(productId);
            if(cost>0) {
                
                getCoin(innoGetCoinMsg, productId, cost);
            }
        }
    }

    /**

     */
    private static void getCoin(InnoGetCoinMsg innoGetCoinMsg, int productId, int cost) {
        int userId = innoGetCoinMsg.getUserId();
        int serverSideType = innoGetCoinMsg.getServerSideType();
        int coinNum = innoGetCoinMsg.getRetNum();
    }

    /**

     */
    public static void describProductAwardMsg(InnoProductAwardMsg innoProductAwardMsg) {
        int productId = ProductAliasDao.getInstance().loadByAlias(innoProductAwardMsg.getAlias());
    }

    /**

     */
    public static void voiceNotice(JSONObject jsonMap) {
        String alias = InnoParamsUtil.loadStringParams(jsonMap, "alias");
        if(!StrUtil.checkEmpty(alias)) {
            int productId = ProductAliasDao.getInstance().loadByAlias(alias);
            if(productId>0) {
                SchedulerSample.delayed(1,
                        new InnoVoiceNoticeTask(jsonMap, productId));
            }
        }else{

        }
    }

    /**

     */
    public static void describeDragonMsg(InnoDragonMsg innoDragonMsg) {
        int productId = ProductAliasDao.getInstance().loadByAlias(innoDragonMsg.getAlias());
        if(productId>0) {
            
        }
    }
}
