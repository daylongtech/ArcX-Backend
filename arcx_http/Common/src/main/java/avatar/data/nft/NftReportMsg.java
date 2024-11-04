package avatar.data.nft;

/**

 */
public class NftReportMsg {
    
    private long opTm;

    
    private long opPrc;

    
    private double inc;

    
    private long blAmt;

    
    private int tax;

    public long getOpTm() {
        return opTm;
    }

    public void setOpTm(long opTm) {
        this.opTm = opTm;
    }

    public long getOpPrc() {
        return opPrc;
    }

    public void setOpPrc(long opPrc) {
        this.opPrc = opPrc;
    }

    public double getInc() {
        return inc;
    }

    public void setInc(double inc) {
        this.inc = inc;
    }

    public long getBlAmt() {
        return blAmt;
    }

    public void setBlAmt(long blAmt) {
        this.blAmt = blAmt;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
}
