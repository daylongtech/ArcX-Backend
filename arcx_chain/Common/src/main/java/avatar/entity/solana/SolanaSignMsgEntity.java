package avatar.entity.solana;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class SolanaSignMsgEntity extends BaseEntity {
    public SolanaSignMsgEntity() {
        super(SolanaSignMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private String signature;


    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
