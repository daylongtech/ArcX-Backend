package avatar.entity.product.repair;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class ProductRepairConfigEntity extends BaseEntity {
    public ProductRepairConfigEntity() {
        super(ProductRepairConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private String officalUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfficalUrl() {
        return officalUrl;
    }

    public void setOfficalUrl(String officalUrl) {
        this.officalUrl = officalUrl;
    }
}
