package avatar.data.recharge;

import java.io.Serializable;
import java.util.List;

/**

 */
public class UserRechargePropertyMsg implements Serializable {
    
    private int userId;

    
    private long refreshTime;

    
    private List<Integer> propertyList;

    
    private List<Integer> buyList;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public List<Integer> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Integer> propertyList) {
        this.propertyList = propertyList;
    }

    public List<Integer> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<Integer> buyList) {
        this.buyList = buyList;
    }
}
