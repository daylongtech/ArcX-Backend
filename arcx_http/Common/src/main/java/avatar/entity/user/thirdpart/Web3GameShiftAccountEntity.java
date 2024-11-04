package avatar.entity.user.thirdpart;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class Web3GameShiftAccountEntity extends BaseEntity {
    public Web3GameShiftAccountEntity() {
        super(Web3GameShiftAccountEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int userId;


    private String wallet;


    private String axcAccount;


    private String usdtAccount;


    private String createTime;

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

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getAxcAccount() {
        return axcAccount;
    }

    public void setAxcAccount(String axcAccount) {
        this.axcAccount = axcAccount;
    }

    public String getUsdtAccount() {
        return usdtAccount;
    }

    public void setUsdtAccount(String usdtAccount) {
        this.usdtAccount = usdtAccount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
