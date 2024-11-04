package avatar.entity.product.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class ProductInfoEntity extends BaseEntity {
    public ProductInfoEntity() {
        super(ProductInfoEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private String productName;


    private int imgProductId;


    private int liveType;


    private String liveUrl;


    private String webLiveUrl;


    private int sequence;


    private int secondSequence;


    private String alias;


    private int productType;


    private int secondType;


    private int costCommodityType;


    private int cost;


    private String descr;


    private int imgProductDetailId;


    private int imgProductPresentId;


    private String ip;


    private String port;


    private int defaultMusic;


    private int commodityType;


    private int awardId;


    private int awardMinNum;


    private int awardMaxNum;


    private int status;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getImgProductId() {
        return imgProductId;
    }

    public void setImgProductId(int imgProductId) {
        this.imgProductId = imgProductId;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getWebLiveUrl() {
        return webLiveUrl;
    }

    public void setWebLiveUrl(String webLiveUrl) {
        this.webLiveUrl = webLiveUrl;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSecondSequence() {
        return secondSequence;
    }

    public void setSecondSequence(int secondSequence) {
        this.secondSequence = secondSequence;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public int getCostCommodityType() {
        return costCommodityType;
    }

    public void setCostCommodityType(int costCommodityType) {
        this.costCommodityType = costCommodityType;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getImgProductDetailId() {
        return imgProductDetailId;
    }

    public void setImgProductDetailId(int imgProductDetailId) {
        this.imgProductDetailId = imgProductDetailId;
    }

    public int getImgProductPresentId() {
        return imgProductPresentId;
    }

    public void setImgProductPresentId(int imgProductPresentId) {
        this.imgProductPresentId = imgProductPresentId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getDefaultMusic() {
        return defaultMusic;
    }

    public void setDefaultMusic(int defaultMusic) {
        this.defaultMusic = defaultMusic;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getAwardMinNum() {
        return awardMinNum;
    }

    public void setAwardMinNum(int awardMinNum) {
        this.awardMinNum = awardMinNum;
    }

    public int getAwardMaxNum() {
        return awardMaxNum;
    }

    public void setAwardMaxNum(int awardMaxNum) {
        this.awardMaxNum = awardMaxNum;
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
