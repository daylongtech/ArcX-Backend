package avatar.entity.user.attribute;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserAttributeMsgEntity extends BaseEntity {
    public UserAttributeMsgEntity() {
        super(UserAttributeMsgEntity.class);
    }

    @Pk

    private int id;


    private int userId;


    private int userLevel;


    private long userLevelExp;


    private int energyLevel;


    private long energyNum;


    private int chargingLevel;


    private String chargingTime;


    private int airdropLevel;


    private int luckyLevel;


    private int charmLevel;


    private String createTime;


    private String updateTime;

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

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public long getUserLevelExp() {
        return userLevelExp;
    }

    public void setUserLevelExp(long userLevelExp) {
        this.userLevelExp = userLevelExp;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public long getEnergyNum() {
        return energyNum;
    }

    public void setEnergyNum(long energyNum) {
        this.energyNum = energyNum;
    }

    public int getChargingLevel() {
        return chargingLevel;
    }

    public void setChargingLevel(int chargingLevel) {
        this.chargingLevel = chargingLevel;
    }

    public String getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(String chargingTime) {
        this.chargingTime = chargingTime;
    }

    public int getAirdropLevel() {
        return airdropLevel;
    }

    public void setAirdropLevel(int airdropLevel) {
        this.airdropLevel = airdropLevel;
    }

    public int getLuckyLevel() {
        return luckyLevel;
    }

    public void setLuckyLevel(int luckyLevel) {
        this.luckyLevel = luckyLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
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
