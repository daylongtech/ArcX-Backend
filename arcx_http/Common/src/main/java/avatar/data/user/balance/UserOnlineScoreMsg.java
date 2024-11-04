package avatar.data.user.balance;

import java.io.Serializable;

/**

 */
public class UserOnlineScoreMsg implements Serializable {
    
    private int userId;

    
    private int commodityType;

    
    private long addNum;

    
    private long costNum;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public long getAddNum() {
        return addNum;
    }

    public void setAddNum(long addNum) {
        this.addNum = addNum;
    }

    public long getCostNum() {
        return costNum;
    }

    public void setCostNum(long costNum) {
        this.costNum = costNum;
    }
}
