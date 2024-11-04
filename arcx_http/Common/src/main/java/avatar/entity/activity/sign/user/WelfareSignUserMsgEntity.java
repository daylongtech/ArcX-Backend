package avatar.entity.activity.sign.user;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class WelfareSignUserMsgEntity extends BaseEntity {
    public WelfareSignUserMsgEntity() {
        super(WelfareSignUserMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int userId;


    private String signTime;


    private int continueSignDay;


    private int sumSignDay;

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

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public int getContinueSignDay() {
        return continueSignDay;
    }

    public void setContinueSignDay(int continueSignDay) {
        this.continueSignDay = continueSignDay;
    }

    public int getSumSignDay() {
        return sumSignDay;
    }

    public void setSumSignDay(int sumSignDay) {
        this.sumSignDay = sumSignDay;
    }
}
