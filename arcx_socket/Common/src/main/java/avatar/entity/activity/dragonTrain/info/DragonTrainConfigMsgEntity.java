package avatar.entity.activity.dragonTrain.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class DragonTrainConfigMsgEntity extends BaseEntity {
    public DragonTrainConfigMsgEntity() {
        super(DragonTrainConfigMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int awardNum;


    private int againTime;


    private int isRepeatAward;


    private int awardImgId;


    private int dragonMinNum;


    private int dragonMaxNum;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public int getAgainTime() {
        return againTime;
    }

    public void setAgainTime(int againTime) {
        this.againTime = againTime;
    }

    public int getIsRepeatAward() {
        return isRepeatAward;
    }

    public void setIsRepeatAward(int isRepeatAward) {
        this.isRepeatAward = isRepeatAward;
    }

    public int getAwardImgId() {
        return awardImgId;
    }

    public void setAwardImgId(int awardImgId) {
        this.awardImgId = awardImgId;
    }

    public int getDragonMinNum() {
        return dragonMinNum;
    }

    public void setDragonMinNum(int dragonMinNum) {
        this.dragonMinNum = dragonMinNum;
    }

    public int getDragonMaxNum() {
        return dragonMaxNum;
    }

    public void setDragonMaxNum(int dragonMaxNum) {
        this.dragonMaxNum = dragonMaxNum;
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
