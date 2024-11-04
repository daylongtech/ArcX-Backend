package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class UserLotteryMsg implements Serializable {
    
    private int userId;

    
    private int secondLevelType;

    
    private int lotteryNum;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSecondLevelType() {
        return secondLevelType;
    }

    public void setSecondLevelType(int secondLevelType) {
        this.secondLevelType = secondLevelType;
    }

    public int getLotteryNum() {
        return lotteryNum;
    }

    public void setLotteryNum(int lotteryNum) {
        this.lotteryNum = lotteryNum;
    }
}
