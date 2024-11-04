package avatar.data.recharge;

import java.util.List;

/**

 */
public class RechargePropertyMsg {
    
    private long rfTm;

    
    private int rfAxcAmt;

    
    List<RechargePropertyDetailMsg> ppyTbln;

    public long getRfTm() {
        return rfTm;
    }

    public void setRfTm(long rfTm) {
        this.rfTm = rfTm;
    }

    public int getRfAxcAmt() {
        return rfAxcAmt;
    }

    public void setRfAxcAmt(int rfAxcAmt) {
        this.rfAxcAmt = rfAxcAmt;
    }

    public List<RechargePropertyDetailMsg> getPpyTbln() {
        return ppyTbln;
    }

    public void setPpyTbln(List<RechargePropertyDetailMsg> ppyTbln) {
        this.ppyTbln = ppyTbln;
    }
}
