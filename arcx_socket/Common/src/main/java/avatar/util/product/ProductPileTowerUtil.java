package avatar.util.product;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.entity.product.pileTower.ProductPileTowerConfigEntity;
import avatar.entity.product.pileTower.ProductPileTowerUserMsgEntity;
import avatar.module.product.gaming.PileTowerMsgDao;
import avatar.module.product.pileTower.ProductPileTowerConfigDao;
import avatar.module.product.pileTower.ProductPileTowerUserMsgDao;
import avatar.util.LogUtil;
import avatar.util.log.CostUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserNoticePushUtil;

/**

 */
public class ProductPileTowerUtil {
    /**

     */
    public static void dealPileTowerAward(int userId, int productId) {
        
        ProductPileTowerConfigEntity configEntity = ProductPileTowerConfigDao.getInstance().loadMsg();
        if(configEntity!=null) {
            
            ProductPileTowerUserMsgEntity userMsgEntity = ProductPileTowerUserMsgDao.getInstance().loadByUserId(userId);
            if(userMsgEntity==null || (TimeUtil.getNowTime()- TimeUtil.strToLong(userMsgEntity.getCreateTime()))
                    >configEntity.getIntervalTime()*1000){
                
                int minNum = configEntity.getMinNum();
                int maxNum = configEntity.getMaxNum();
                if(minNum>0 && maxNum>0){
                    int awardNum = StrUtil.loadInterValNum(minNum, maxNum);
                    
                    CostUtil.addProductPileTowerAward(userId, productId, awardNum);
                    
                    UserNoticePushUtil.pushPileTowerAward(userId, productId, awardNum, configEntity.getAwardImgId());
                }else{

                }
            }
        }else{

        }
    }

    /**

     */
    public static ProductPileTowerUserMsgEntity initProductPileTowerUserMsgEntity(int userId, int productId, int num){
        ProductPileTowerUserMsgEntity entity = new ProductPileTowerUserMsgEntity();
        entity.setUserId(userId);
        entity.setProductId(productId);
        entity.setNum(num);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void initMsg(PileTowerMsg msg){
        msg.setPileTime(0);
        msg.setTillTime(0);
        PileTowerMsgDao.getInstance().setCache(msg.getProductId(), msg);
    }

}
