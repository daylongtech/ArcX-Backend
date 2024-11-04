package avatar.entity.user.online;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserOnlineMsgEntity extends BaseEntity {
    public UserOnlineMsgEntity() {
        super(UserOnlineMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int userId;


    private int productId;


    private int isOnline;


    private int isGaming;


    private int isCharter;


    private String ip;


    private String port;


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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getIsGaming() {
        return isGaming;
    }

    public void setIsGaming(int isGaming) {
        this.isGaming = isGaming;
    }

    public int getIsCharter() {
        return isCharter;
    }

    public void setIsCharter(int isCharter) {
        this.isCharter = isCharter;
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
