package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class SellGoldMachineGoldHistoryEntity extends BaseEntity {
    public SellGoldMachineGoldHistoryEntity() {
        super(SellGoldMachineGoldHistoryEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private String nftCode;


    private double operatePrice;


    private int userId;


    private int buyUserId;


    private long goldNum;


    private long balanceNum;


    private double usdtNum;


    private int tax;


    private double taxNum;


    private double realEarn;


    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNftCode() {
        return nftCode;
    }

    public void setNftCode(String nftCode) {
        this.nftCode = nftCode;
    }

    public double getOperatePrice() {
        return operatePrice;
    }

    public void setOperatePrice(double operatePrice) {
        this.operatePrice = operatePrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(int buyUserId) {
        this.buyUserId = buyUserId;
    }

    public long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(long goldNum) {
        this.goldNum = goldNum;
    }

    public long getBalanceNum() {
        return balanceNum;
    }

    public void setBalanceNum(long balanceNum) {
        this.balanceNum = balanceNum;
    }

    public double getUsdtNum() {
        return usdtNum;
    }

    public void setUsdtNum(double usdtNum) {
        this.usdtNum = usdtNum;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public double getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(double taxNum) {
        this.taxNum = taxNum;
    }

    public double getRealEarn() {
        return realEarn;
    }

    public void setRealEarn(double realEarn) {
        this.realEarn = realEarn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
