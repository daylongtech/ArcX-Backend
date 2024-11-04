package avatar.entity.product.innoMsg;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class InnoPushCoinWindowMsgEntity extends BaseEntity {
    public InnoPushCoinWindowMsgEntity() {
        super(InnoPushCoinWindowMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int secondType;


    private String imgUrl;


    private int awardLockOutTime;


    private int multiCoolingTime;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getAwardLockOutTime() {
        return awardLockOutTime;
    }

    public void setAwardLockOutTime(int awardLockOutTime) {
        this.awardLockOutTime = awardLockOutTime;
    }

    public int getMultiCoolingTime() {
        return multiCoolingTime;
    }

    public void setMultiCoolingTime(int multiCoolingTime) {
        this.multiCoolingTime = multiCoolingTime;
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
