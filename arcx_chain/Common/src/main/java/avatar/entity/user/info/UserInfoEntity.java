package avatar.entity.user.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserInfoEntity extends BaseEntity {
    public UserInfoEntity() {
        super(UserInfoEntity.class);
    }

    @Pk

    private int id;


    private String nickName;


    private int sex;


    private String imgUrl;


    private String signature;

    @Column(name = "ip" , comment = "ip")
    private String ip;


    private int loginWay;


    private int mobilePlatformType;


    private int status;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getLoginWay() {
        return loginWay;
    }

    public void setLoginWay(int loginWay) {
        this.loginWay = loginWay;
    }

    public int getMobilePlatformType() {
        return mobilePlatformType;
    }

    public void setMobilePlatformType(int mobilePlatformType) {
        this.mobilePlatformType = mobilePlatformType;
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
