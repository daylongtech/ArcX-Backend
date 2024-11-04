package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class UserStartGameMultiMsg implements Serializable {
    
    private int userId;

    
    private int coinMulti;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoinMulti() {
        return coinMulti;
    }

    public void setCoinMulti(int coinMulti) {
        this.coinMulti = coinMulti;
    }
}
