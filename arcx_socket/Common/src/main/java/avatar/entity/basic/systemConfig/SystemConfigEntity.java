package avatar.entity.basic.systemConfig;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class SystemConfigEntity extends BaseEntity {
    public SystemConfigEntity() {
        super(SystemConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int serverType;


    private String mediaPrefix;


    private int registerCoin;


    private int systemMaintain;


    private String serverImg;

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

    public int getRegisterCoin() {
        return registerCoin;
    }

    public void setRegisterCoin(int registerCoin) {
        this.registerCoin = registerCoin;
    }

    public int getSystemMaintain() {
        return systemMaintain;
    }

    public void setSystemMaintain(int systemMaintain) {
        this.systemMaintain = systemMaintain;
    }

    public String getServerImg() {
        return serverImg;
    }

    public void setServerImg(String serverImg) {
        this.serverImg = serverImg;
    }
}
