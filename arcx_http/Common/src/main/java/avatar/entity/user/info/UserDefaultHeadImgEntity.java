package avatar.entity.user.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserDefaultHeadImgEntity extends BaseEntity {
    public UserDefaultHeadImgEntity() {
        super(UserDefaultHeadImgEntity.class);
    }

    @Pk

    private int id;


    private String imgUrl;


    private int isCrossPlatform;


    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsCrossPlatform() {
        return isCrossPlatform;
    }

    public void setIsCrossPlatform(int isCrossPlatform) {
        this.isCrossPlatform = isCrossPlatform;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
