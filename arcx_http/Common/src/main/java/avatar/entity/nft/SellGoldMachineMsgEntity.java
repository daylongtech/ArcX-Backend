package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class SellGoldMachineMsgEntity extends BaseEntity {
    public SellGoldMachineMsgEntity() {
        super(SellGoldMachineMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int userId;


    private String nftCode;


    private String nftName;


    private int imgId;


    private int lv;


    private long expNum;


    private int spaceLv;


    private int incomeLv;


    private long goldNum;


    private long durability;


    private long adv;


    private int saleCommodityType;


    private long saleNum;


    private double operatePrice;


    private String startOperateTime;


    private int sellTime;


    private int status;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNftCode() {
        return nftCode;
    }

    public void setNftCode(String nftCode) {
        this.nftCode = nftCode;
    }

    public String getNftName() {
        return nftName;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getExpNum() {
        return expNum;
    }

    public void setExpNum(long expNum) {
        this.expNum = expNum;
    }

    public int getSpaceLv() {
        return spaceLv;
    }

    public void setSpaceLv(int spaceLv) {
        this.spaceLv = spaceLv;
    }

    public int getIncomeLv() {
        return incomeLv;
    }

    public void setIncomeLv(int incomeLv) {
        this.incomeLv = incomeLv;
    }

    public long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(long goldNum) {
        this.goldNum = goldNum;
    }

    public long getDurability() {
        return durability;
    }

    public void setDurability(long durability) {
        this.durability = durability;
    }

    public long getAdv() {
        return adv;
    }

    public void setAdv(long adv) {
        this.adv = adv;
    }

    public int getSaleCommodityType() {
        return saleCommodityType;
    }

    public void setSaleCommodityType(int saleCommodityType) {
        this.saleCommodityType = saleCommodityType;
    }

    public long getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(long saleNum) {
        this.saleNum = saleNum;
    }

    public double getOperatePrice() {
        return operatePrice;
    }

    public void setOperatePrice(double operatePrice) {
        this.operatePrice = operatePrice;
    }

    public String getStartOperateTime() {
        return startOperateTime;
    }

    public void setStartOperateTime(String startOperateTime) {
        this.startOperateTime = startOperateTime;
    }

    public int getSellTime() {
        return sellTime;
    }

    public void setSellTime(int sellTime) {
        this.sellTime = sellTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
