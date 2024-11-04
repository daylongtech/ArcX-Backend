package avatar.module.product.normalProduct;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**

 */
public class InnerNormalProductReconnectDao {
    private static final InnerNormalProductReconnectDao instance = new InnerNormalProductReconnectDao();
    public static final InnerNormalProductReconnectDao getInstance(){
        return instance;
    }

    /**

     */
    public int loadMsg(String fromKey, String toKey) {
        
        return loadCache(fromKey, toKey);
    }

    //=========================cache===========================

    /**

     */
    private int loadCache(String fromKey, String tokey){
        int status = YesOrNoEnum.NO.getCode();
        Object object = GameData.getCache().get(ProductPrefixMsg.INNER_NORMAL_PRODUCT_RECONNECT+fromKey+tokey);
        if(object==null){
            setCache(fromKey, tokey, status);
        }else{
            status = (int) object;
        }
        return status;
    }

    /**

     */
    public void setCache(String fromKey, String tokey, int status){
        GameData.getCache().set(ProductPrefixMsg.INNER_NORMAL_PRODUCT_RECONNECT+fromKey+tokey, status);
    }
}
