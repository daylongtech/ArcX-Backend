package avatar.data.product.innoMsg;

import java.util.List;

/**

 */
public class ProductCoinMultiMsg {
    
    private String wdwPct;

    
    private List<ProductCoinMultiLimitMsg> mulTbln;

    public String getWdwPct() {
        return wdwPct;
    }

    public void setWdwPct(String wdwPct) {
        this.wdwPct = wdwPct;
    }

    public List<ProductCoinMultiLimitMsg> getMulTbln() {
        return mulTbln;
    }

    public void setMulTbln(List<ProductCoinMultiLimitMsg> mulTbln) {
        this.mulTbln = mulTbln;
    }
}
