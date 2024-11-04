package avatar.entity.user.token;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserTokenMsgEntity extends BaseEntity {
    public UserTokenMsgEntity() {
        super(UserTokenMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int userId;


    private String accessToken;


    private long accessOutTime;


    private String refreshToken;


    private long refreshOutTime;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getAccessOutTime() {
        return accessOutTime;
    }

    public void setAccessOutTime(long accessOutTime) {
        this.accessOutTime = accessOutTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getRefreshOutTime() {
        return refreshOutTime;
    }

    public void setRefreshOutTime(long refreshOutTime) {
        this.refreshOutTime = refreshOutTime;
    }
}
