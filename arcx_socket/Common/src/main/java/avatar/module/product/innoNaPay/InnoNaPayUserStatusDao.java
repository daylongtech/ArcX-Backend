package avatar.module.product.innoNaPay;

import avatar.data.product.innoNaPay.InnoNaPayUserStatusMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.InnoNaPayUtil;

/**

 */
public class InnoNaPayUserStatusDao {
    private static final InnoNaPayUserStatusDao instance = new InnoNaPayUserStatusDao();
    public static final InnoNaPayUserStatusDao getInstance() {
        return instance;
    }

    /**

     */
    public InnoNaPayUserStatusMsg loadMsg(int userId) {
        
        InnoNaPayUserStatusMsg msg = loadCache(userId);
        if (msg == null) {
            msg = InnoNaPayUtil.initInnoNaPayUserStatusMsg(userId);
            
            setCache(userId, msg);
        }
        
        return InnoNaPayUtil.dealInnoNaPayUserStatusMsg(msg);
    }

    //=========================cache===========================

    /**

     */
    private InnoNaPayUserStatusMsg loadCache(int userId) {
        return (InnoNaPayUserStatusMsg) GameData.getCache().get(ProductPrefixMsg.INNO_NA_PAY_USER_STATUS + "_" + userId);
    }

    /**

     */
    public void setCache(int userId, InnoNaPayUserStatusMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_USER_STATUS + "_" + userId, msg);
    }

    /**

     */
    public void removeCache(int userId) {
        GameData.getCache().removeCache(ProductPrefixMsg.INNO_NA_PAY_USER_STATUS + "_" + userId);
    }

}
