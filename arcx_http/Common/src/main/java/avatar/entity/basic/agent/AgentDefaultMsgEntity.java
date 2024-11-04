package avatar.entity.basic.agent;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class AgentDefaultMsgEntity extends BaseEntity {
    public AgentDefaultMsgEntity() {
        super(AgentDefaultMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private String defaultNation;


    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefaultNation() {
        return defaultNation;
    }

    public void setDefaultNation(String defaultNation) {
        this.defaultNation = defaultNation;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
