package avatar.entity.recharge.property;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class RechargePropertyConfigEntity extends BaseEntity {
    public RechargePropertyConfigEntity() {
        super(RechargePropertyConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int showNum;


    private int refreshPrice;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public int getRefreshPrice() {
        return refreshPrice;
    }

    public void setRefreshPrice(int refreshPrice) {
        this.refreshPrice = refreshPrice;
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
