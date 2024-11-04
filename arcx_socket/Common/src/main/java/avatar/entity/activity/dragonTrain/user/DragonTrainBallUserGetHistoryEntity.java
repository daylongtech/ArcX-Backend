package avatar.entity.activity.dragonTrain.user;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class DragonTrainBallUserGetHistoryEntity extends BaseEntity {
    public DragonTrainBallUserGetHistoryEntity() {
        super(DragonTrainBallUserGetHistoryEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int userId;


    private int productId;


    private int currentNum;


    private int awardCoin;


    private int isTrigger;


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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public int getAwardCoin() {
        return awardCoin;
    }

    public void setAwardCoin(int awardCoin) {
        this.awardCoin = awardCoin;
    }

    public int getIsTrigger() {
        return isTrigger;
    }

    public void setIsTrigger(int isTrigger) {
        this.isTrigger = isTrigger;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
