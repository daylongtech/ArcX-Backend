package avatar.data.product.info;

import avatar.data.crossServer.ConciseServerUserMsg;

import java.util.List;

/**

 */
public class ProductMsg {
    
    private int devId;

    
    private String devNm;

    
    private String devPct;

    
    private int csAmt;

    
    private List<ConciseServerUserMsg> plyTbln;

    
    private List<Integer> devTbln;

    public int getDevId() {
        return devId;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }

    public String getDevNm() {
        return devNm;
    }

    public void setDevNm(String devNm) {
        this.devNm = devNm;
    }

    public String getDevPct() {
        return devPct;
    }

    public void setDevPct(String devPct) {
        this.devPct = devPct;
    }

    public int getCsAmt() {
        return csAmt;
    }

    public void setCsAmt(int csAmt) {
        this.csAmt = csAmt;
    }

    public List<ConciseServerUserMsg> getPlyTbln() {
        return plyTbln;
    }

    public void setPlyTbln(List<ConciseServerUserMsg> plyTbln) {
        this.plyTbln = plyTbln;
    }

    public List<Integer> getDevTbln() {
        return devTbln;
    }

    public void setDevTbln(List<Integer> devTbln) {
        this.devTbln = devTbln;
    }
}
