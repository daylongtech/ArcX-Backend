package avatar.util.product;

import avatar.data.product.gamingMsg.InnoProductOffLineMsg;
import avatar.data.product.gamingMsg.ProductAwardLockMsg;
import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.global.basicConfig.basic.ProductConfigMsg;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.info.ProductAwardLockDao;
import avatar.module.product.info.ProductSettlementMsgDao;
import avatar.module.product.innoMsg.InnoPushCoinMultiDao;
import avatar.module.product.innoMsg.InnoPushCoinWindowDao;
import avatar.util.system.TimeUtil;

import java.util.Collections;
import java.util.List;

/**

 */
public class ProductGamingUtil {
    /**

     */
    public static int loadInnoLastMultiLevel(int productId){
        int multiLevel = 0;
        long coolingTime = loadMultiCollingTime(productId);
        if(coolingTime>0) {
            
            InnoProductOffLineMsg msg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
            if(msg!=null && (TimeUtil.getNowTime()-msg.getOffLineTime())<coolingTime){
                multiLevel = msg.getMulti();
            }
        }
        return multiLevel;
    }

    /**

     */
    private static long loadMultiCollingTime(int productId) {
        long collTime = 0;
        int secondType = ProductUtil.loadSecondType(productId);
        if(secondType>0){
            
            InnoPushCoinWindowMsgEntity entity = InnoPushCoinWindowDao.getInstance().loadBySecondType(secondType);
            collTime = entity==null?0:entity.getMultiCoolingTime();
        }
        return collTime*1000;
    }

    /**

     */
    public static InnoProductOffLineMsg initInnoProductOffLineMsg(int productId) {
        InnoProductOffLineMsg msg = new InnoProductOffLineMsg();
        msg.setProductId(productId);
        msg.setOffLineTime(0);
        msg.setMulti(loadMultiLevel(productId));
        return msg;
    }

    /**

     */
    public static int loadMultiLevel(int productId) {
        int multiLevel = 1;
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        int coinMul = msg.getCoinMulti();
        if(coinMul>0){
            
            List<Integer> multiList = InnoPushCoinMultiDao.getInstance().loadBySecondType(
                    ProductUtil.loadSecondType(productId));
            if(multiList.size()>0){
                Collections.sort(multiList);
                for(int i=0;i<multiList.size();i++){
                    if(coinMul==multiList.get(i)){
                        multiLevel += i;
                        break;
                    }
                }
            }
        }
        
        multiLevel = Math.min(multiLevel, ProductConfigMsg.maxCoinMultiLevel);
        return multiLevel;
    }

    /**

     */
    public static ProductAwardLockMsg initProductAwardLockMsg(int productId) {
        ProductAwardLockMsg msg = new ProductAwardLockMsg();
        msg.setProductId(productId);
        msg.setCoinMulti(0);
        msg.setIsAwardLock(YesOrNoEnum.NO.getCode());
        msg.setLockTime(0);
        return msg;
    }

}
