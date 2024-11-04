package avatar.data.user.attribute;

import java.io.Serializable;

/**

 */
public class UserOnlineExpMsg implements Serializable {
    
    private int userId;

    
    private long coinNum;

    
    private long expNum;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(long coinNum) {
        this.coinNum = coinNum;
    }

    public long getExpNum() {
        return expNum;
    }

    public void setExpNum(long expNum) {
        this.expNum = expNum;
    }
}
