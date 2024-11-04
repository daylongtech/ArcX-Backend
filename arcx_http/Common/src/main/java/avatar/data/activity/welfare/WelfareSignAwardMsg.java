package avatar.data.activity.welfare;

import avatar.data.basic.award.GeneralAwardMsg;

import java.util.List;

/**

 */
public class WelfareSignAwardMsg {
    
    private List<GeneralAwardMsg> earnArr;

    
    private int snFlag;

    public List<GeneralAwardMsg> getEarnArr() {
        return earnArr;
    }

    public void setEarnArr(List<GeneralAwardMsg> earnArr) {
        this.earnArr = earnArr;
    }

    public int getSnFlag() {
        return snFlag;
    }

    public void setSnFlag(int snFlag) {
        this.snFlag = snFlag;
    }
}
