package avatar.entity.product.productType;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class ProductSecondLevelTypeEntity extends BaseEntity {
    public ProductSecondLevelTypeEntity() {
        super(ProductSecondLevelTypeEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int productType;


    private String name;


    private String enName;


    private int isInnoProduct;


    private int isLotteryProduct;


    private String instructType;


    private int serverOffLineTime;


    private int closeSocketOffTime;


    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public int getIsInnoProduct() {
        return isInnoProduct;
    }

    public void setIsInnoProduct(int isInnoProduct) {
        this.isInnoProduct = isInnoProduct;
    }

    public int getIsLotteryProduct() {
        return isLotteryProduct;
    }

    public void setIsLotteryProduct(int isLotteryProduct) {
        this.isLotteryProduct = isLotteryProduct;
    }

    public String getInstructType() {
        return instructType;
    }

    public void setInstructType(String instructType) {
        this.instructType = instructType;
    }

    public int getServerOffLineTime() {
        return serverOffLineTime;
    }

    public void setServerOffLineTime(int serverOffLineTime) {
        this.serverOffLineTime = serverOffLineTime;
    }

    public int getCloseSocketOffTime() {
        return closeSocketOffTime;
    }

    public void setCloseSocketOffTime(int closeSocketOffTime) {
        this.closeSocketOffTime = closeSocketOffTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
