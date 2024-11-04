package avatar.entity.crossServer;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class CrossServerMediaPrefixEntity extends BaseEntity {
    public CrossServerMediaPrefixEntity() {
        super(CrossServerMediaPrefixEntity.class);
    }

    @Pk

    private int id;


    private int serverType;


    private String mediaPrefix;

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

    public String getMediaPrefix() {
        return mediaPrefix;
    }

    public void setMediaPrefix(String mediaPrefix) {
        this.mediaPrefix = mediaPrefix;
    }
}
