package avatar.data.product.innoNaPay;

import java.io.Serializable;

/**

 */
public class InnoNaPayUserStatusMsg implements Serializable {
    
    private int userId;

    
    private boolean payFlag;

    
    private long refreshTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isPayFlag() {
        return payFlag;
    }

    public void setPayFlag(boolean payFlag) {
        this.payFlag = payFlag;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }
}
