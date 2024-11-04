package avatar.util.checkParams;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.enumMsg.product.ProductStatusEnum;
import avatar.global.enumMsg.product.ProductTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**

 */
public class ProductCheckParamsUtil {
    /**

     */
    public static int checkProductStatus(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();
        if(ParamsUtil.isSuccess(status)){
            try {
                int productId = ParamsUtil.productId(map);
                
                ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
                if(productId==0){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }else if(entity==null){
                    status = ClientCode.PRODUCT_NO_EXIST.getCode();
                }else if(entity.getStatus()!=ProductStatusEnum.NORMAL.getCode()){
                    status = ClientCode.PRODUCT_EXCEPTION.getCode();
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static int productList(Map<String, Object> map) {
        int status = CheckParamsUtil.checkPage(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int productType = ParamsUtil.intParmasNotNull(map, "devTp");
                if(StrUtil.checkEmpty(ProductTypeEnum.loadNameByCode(productType))){
                    status = ClientCode.PARAMS_ERROR.getCode();
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }

    /**

     */
    public static int fastJoinProduct(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();
        try {
            int productType = ParamsUtil.intParmasNotNull(map, "devTp");
            if(StrUtil.checkEmpty(ProductTypeEnum.loadNameByCode(productType))){
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();
        }
        return status;
    }

    /**

     */
    public static int repairProduct(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                status = checkProductStatus(map);
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();
            }
        }
        return status;
    }

}
