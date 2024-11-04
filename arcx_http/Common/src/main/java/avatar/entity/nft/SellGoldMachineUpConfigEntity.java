package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class SellGoldMachineUpConfigEntity extends BaseEntity {
    public SellGoldMachineUpConfigEntity() {
        super(SellGoldMachineUpConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int lv;


    private long upExp;


    private long storedMax;


    private double incomeDiscount;


    private long lvAxc;


    private long lvArcx;


    private long discountAxc;


    private long storedAxc;


    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getUpExp() {
        return upExp;
    }

    public void setUpExp(long upExp) {
        this.upExp = upExp;
    }

    public long getStoredMax() {
        return storedMax;
    }

    public void setStoredMax(long storedMax) {
        this.storedMax = storedMax;
    }

    public double getIncomeDiscount() {
        return incomeDiscount;
    }

    public void setIncomeDiscount(double incomeDiscount) {
        this.incomeDiscount = incomeDiscount;
    }

    public long getLvAxc() {
        return lvAxc;
    }

    public void setLvAxc(long lvAxc) {
        this.lvAxc = lvAxc;
    }

    public long getLvArcx() {
        return lvArcx;
    }

    public void setLvArcx(long lvArcx) {
        this.lvArcx = lvArcx;
    }

    public long getDiscountAxc() {
        return discountAxc;
    }

    public void setDiscountAxc(long discountAxc) {
        this.discountAxc = discountAxc;
    }

    public long getStoredAxc() {
        return storedAxc;
    }

    public void setStoredAxc(long storedAxc) {
        this.storedAxc = storedAxc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
