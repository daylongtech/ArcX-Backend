package avatar.entity.crossServer;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class CrossServerDomainEntity extends BaseEntity {
    public CrossServerDomainEntity() {
        super(CrossServerDomainEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int serverType;


    private String domainMsg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getDomainMsg() {
        return domainMsg;
    }

    public void setDomainMsg(String domainMsg) {
        this.domainMsg = domainMsg;
    }
}
