package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class NftConfigEntity extends BaseEntity {
    public NftConfigEntity() {
        super(NftConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private String storeShowImg;


    private int saleTax;


    private int operateTax;


    private long durability;


    private long adWeight;


    private long saleWeight;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreShowImg() {
        return storeShowImg;
    }

    public void setStoreShowImg(String storeShowImg) {
        this.storeShowImg = storeShowImg;
    }

    public int getSaleTax() {
        return saleTax;
    }

    public void setSaleTax(int saleTax) {
        this.saleTax = saleTax;
    }

    public int getOperateTax() {
        return operateTax;
    }

    public void setOperateTax(int operateTax) {
        this.operateTax = operateTax;
    }

    public long getDurability() {
        return durability;
    }

    public void setDurability(long durability) {
        this.durability = durability;
    }

    public long getAdWeight() {
        return adWeight;
    }

    public void setAdWeight(long adWeight) {
        this.adWeight = adWeight;
    }

    public long getSaleWeight() {
        return saleWeight;
    }

    public void setSaleWeight(long saleWeight) {
        this.saleWeight = saleWeight;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
