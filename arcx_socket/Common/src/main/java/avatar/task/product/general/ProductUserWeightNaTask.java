package avatar.task.product.general;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserWeightUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**

 */
public class ProductUserWeightNaTask extends ScheduledTask {
    
    private int productId;

    
    private int userId;

    
    private long naNum;

    public ProductUserWeightNaTask(int productId, int userId, long naNum) {

        this.productId = productId;
        this.userId = userId;
        this.naNum = naNum;
    }

    @Override
    public void run() {
        
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        int productType = productInfoEntity.getProductType();
        int secondType = productInfoEntity.getSecondType();

        if(ProductUtil.isInnoProduct(productId)) {
            UserWeightUtil.addUserInnoNaNum(userId, secondType, naNum);
        }
    }
}
