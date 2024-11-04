package avatar.entity.user.attribute;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserGameExpConfigEntity extends BaseEntity {
    public UserGameExpConfigEntity() {
        super(UserGameExpConfigEntity.class);
    }

    @Pk

    private int id;


    private long coinNum;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(long coinNum) {
        this.coinNum = coinNum;
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
