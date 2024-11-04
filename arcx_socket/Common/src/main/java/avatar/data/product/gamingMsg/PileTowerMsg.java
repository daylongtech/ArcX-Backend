package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class PileTowerMsg implements Serializable {
    
    private int productId;

    
    private int tillTime;

    
    private long pileTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTillTime() {
        return tillTime;
    }

    public void setTillTime(int tillTime) {
        this.tillTime = tillTime;
    }

    public long getPileTime() {
        return pileTime;
    }

    public void setPileTime(long pileTime) {
        this.pileTime = pileTime;
    }
}
