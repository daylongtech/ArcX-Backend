package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class ProductRoomMsg implements Serializable {
    
    private int productId;

    
    private int gamingUserId;

    
    private long onProductTime;

    
    private long pushCoinOnTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getGamingUserId() {
        return gamingUserId;
    }

    public void setGamingUserId(int gamingUserId) {
        this.gamingUserId = gamingUserId;
    }

    public long getOnProductTime() {
        return onProductTime;
    }

    public void setOnProductTime(long onProductTime) {
        this.onProductTime = onProductTime;
    }

    public long getPushCoinOnTime() {
        return pushCoinOnTime;
    }

    public void setPushCoinOnTime(long pushCoinOnTime) {
        this.pushCoinOnTime = pushCoinOnTime;
    }

}
