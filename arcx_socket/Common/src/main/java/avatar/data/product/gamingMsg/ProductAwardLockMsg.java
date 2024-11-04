package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class ProductAwardLockMsg implements Serializable {
    
    private int productId;

    
    private int coinMulti;

    
    private int isAwardLock;

    
    private long lockTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCoinMulti() {
        return coinMulti;
    }

    public void setCoinMulti(int coinMulti) {
        this.coinMulti = coinMulti;
    }

    public int getIsAwardLock() {
        return isAwardLock;
    }

    public void setIsAwardLock(int isAwardLock) {
        this.isAwardLock = isAwardLock;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }
}
