package avatar.entity.activity.dragonTrain.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class DragonTrainWheelIconMsgEntity extends BaseEntity {
    public DragonTrainWheelIconMsgEntity() {
        super(DragonTrainWheelIconMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int awardImgId;


    private int commodityType;


    private int giftId;


    private int awardMinNum;


    private int awardMaxNum;


    private int awardProbability;


    private int totalProbability;


    private String createTime;


    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAwardImgId() {
        return awardImgId;
    }

    public void setAwardImgId(int awardImgId) {
        this.awardImgId = awardImgId;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getAwardMinNum() {
        return awardMinNum;
    }

    public void setAwardMinNum(int awardMinNum) {
        this.awardMinNum = awardMinNum;
    }

    public int getAwardMaxNum() {
        return awardMaxNum;
    }

    public void setAwardMaxNum(int awardMaxNum) {
        this.awardMaxNum = awardMaxNum;
    }

    public int getAwardProbability() {
        return awardProbability;
    }

    public void setAwardProbability(int awardProbability) {
        this.awardProbability = awardProbability;
    }

    public int getTotalProbability() {
        return totalProbability;
    }

    public void setTotalProbability(int totalProbability) {
        this.totalProbability = totalProbability;
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
