package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class DollGamingMsg implements Serializable {
    
    private int productId;

    
    private int time;

    
    private boolean isInitalization;

    
    private boolean isCatch;

    
    private int gamingState;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isInitalization() {
        return isInitalization;
    }

    public void setInitalization(boolean initalization) {
        isInitalization = initalization;
    }

    public boolean isCatch() {
        return isCatch;
    }

    public void setCatch(boolean aCatch) {
        isCatch = aCatch;
    }

    public int getGamingState() {
        return gamingState;
    }

    public void setGamingState(int gamingState) {
        this.gamingState = gamingState;
    }
}
