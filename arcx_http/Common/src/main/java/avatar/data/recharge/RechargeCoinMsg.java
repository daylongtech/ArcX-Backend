package avatar.data.recharge;

/**

 */
public class RechargeCoinMsg {
    
    private int cmdId;

    
    private long cnAmt;

    
    private int usdtAmt;

    
    private String pct;

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public long getCnAmt() {
        return cnAmt;
    }

    public void setCnAmt(long cnAmt) {
        this.cnAmt = cnAmt;
    }

    public int getUsdtAmt() {
        return usdtAmt;
    }

    public void setUsdtAmt(int usdtAmt) {
        this.usdtAmt = usdtAmt;
    }

    public String getPct() {
        return pct;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }
}
