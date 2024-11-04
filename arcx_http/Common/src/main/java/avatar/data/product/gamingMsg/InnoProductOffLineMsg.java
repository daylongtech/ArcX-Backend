package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class InnoProductOffLineMsg implements Serializable {
    
    private int productId;

    
    private int multi;

    
    private long offLineTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getMulti() {
        return multi;
    }

    public void setMulti(int multi) {
        this.multi = multi;
    }

    public long getOffLineTime() {
        return offLineTime;
    }

    public void setOffLineTime(long offLineTime) {
        this.offLineTime = offLineTime;
    }
}
