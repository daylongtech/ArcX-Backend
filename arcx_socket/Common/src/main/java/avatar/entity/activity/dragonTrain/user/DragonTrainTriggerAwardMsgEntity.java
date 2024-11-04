package avatar.entity.activity.dragonTrain.user;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class DragonTrainTriggerAwardMsgEntity extends BaseEntity {
    public DragonTrainTriggerAwardMsgEntity() {
        super(DragonTrainTriggerAwardMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int triggerId;


    private int commodityType;


    private int giftId;


    private int resultAwardNum;


    private int awardMinNum;


    private int awardMaxNum;


    private int awardProbability;


    private int totalProbability;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(int triggerId) {
        this.triggerId = triggerId;
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

    public int getResultAwardNum() {
        return resultAwardNum;
    }

    public void setResultAwardNum(int resultAwardNum) {
        this.resultAwardNum = resultAwardNum;
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
}
