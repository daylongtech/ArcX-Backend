package avatar.entity.user.attribute;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserAttributeConfigEntity extends BaseEntity {
    public UserAttributeConfigEntity() {
        super(UserAttributeConfigEntity.class);
    }

    @Pk

    private int id;


    private int lv;


    private long lvExp;


    private long energyMax;


    private long energyAxc;


    private long chargingSecond;


    private long chargingAxc;


    private long airdropCoin;


    private long airdropAxc;


    private double luckyProbability;


    private long luckyAxc;


    private double charmAddition;


    private long charmAxc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getLvExp() {
        return lvExp;
    }

    public void setLvExp(long lvExp) {
        this.lvExp = lvExp;
    }

    public long getEnergyMax() {
        return energyMax;
    }

    public void setEnergyMax(long energyMax) {
        this.energyMax = energyMax;
    }

    public long getEnergyAxc() {
        return energyAxc;
    }

    public void setEnergyAxc(long energyAxc) {
        this.energyAxc = energyAxc;
    }

    public long getChargingSecond() {
        return chargingSecond;
    }

    public void setChargingSecond(long chargingSecond) {
        this.chargingSecond = chargingSecond;
    }

    public long getChargingAxc() {
        return chargingAxc;
    }

    public void setChargingAxc(long chargingAxc) {
        this.chargingAxc = chargingAxc;
    }

    public long getAirdropCoin() {
        return airdropCoin;
    }

    public void setAirdropCoin(long airdropCoin) {
        this.airdropCoin = airdropCoin;
    }

    public long getAirdropAxc() {
        return airdropAxc;
    }

    public void setAirdropAxc(long airdropAxc) {
        this.airdropAxc = airdropAxc;
    }

    public double getLuckyProbability() {
        return luckyProbability;
    }

    public void setLuckyProbability(double luckyProbability) {
        this.luckyProbability = luckyProbability;
    }

    public long getLuckyAxc() {
        return luckyAxc;
    }

    public void setLuckyAxc(long luckyAxc) {
        this.luckyAxc = luckyAxc;
    }

    public double getCharmAddition() {
        return charmAddition;
    }

    public void setCharmAddition(double charmAddition) {
        this.charmAddition = charmAddition;
    }

    public long getCharmAxc() {
        return charmAxc;
    }

    public void setCharmAxc(long charmAxc) {
        this.charmAxc = charmAxc;
    }
}
