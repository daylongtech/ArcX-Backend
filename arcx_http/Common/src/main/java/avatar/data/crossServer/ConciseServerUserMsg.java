package avatar.data.crossServer;

/**

 */
public class ConciseServerUserMsg {
    
    private int plyId;

    
    private String plyNm;

    
    private String plyPct;

    
    private int svTp;

    public int getPlyId() {
        return plyId;
    }

    public void setPlyId(int plyId) {
        this.plyId = plyId;
    }

    public String getPlyNm() {
        return plyNm;
    }

    public void setPlyNm(String plyNm) {
        this.plyNm = plyNm;
    }

    public String getPlyPct() {
        return plyPct;
    }

    public void setPlyPct(String plyPct) {
        this.plyPct = plyPct;
    }

    public int getSvTp() {
        return svTp;
    }

    public void setSvTp(int svTp) {
        this.svTp = svTp;
    }
}
