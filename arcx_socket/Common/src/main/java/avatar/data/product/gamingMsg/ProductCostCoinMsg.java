package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**

 */
public class ProductCostCoinMsg implements Serializable {
    
    private int productId;

    
    private long sumAddCoin;

    
    private long sumCostCoin;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getSumAddCoin() {
        return sumAddCoin;
    }

    public void setSumAddCoin(long sumAddCoin) {
        this.sumAddCoin = sumAddCoin;
    }

    public long getSumCostCoin() {
        return sumCostCoin;
    }

    public void setSumCostCoin(long sumCostCoin) {
        this.sumCostCoin = sumCostCoin;
    }
}
