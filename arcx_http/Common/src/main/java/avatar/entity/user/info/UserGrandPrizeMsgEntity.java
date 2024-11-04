package avatar.entity.user.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service

public class UserGrandPrizeMsgEntity extends BaseEntity {
    public UserGrandPrizeMsgEntity() {
        super(UserGrandPrizeMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;


    private int userId;


    private int pileTower;


    private int dragonBall;


    private int roomRankFirst;


    private int roomRankSecond;


    private int roomRankThird;


    private int freeGame;


    private int gem;


    private int prizeWheelGrand;


    private int prizeWheelMajor;


    private int prizeWheelMinor;


    private int agyptBox;


    private int gossip;


    private int heroBattle;


    private int thunder;


    private int jackpotKingkong;


    private int jackpotHero;


    private int whistle;


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

    public int getPileTower() {
        return pileTower;
    }

    public void setPileTower(int pileTower) {
        this.pileTower = pileTower;
    }

    public int getDragonBall() {
        return dragonBall;
    }

    public void setDragonBall(int dragonBall) {
        this.dragonBall = dragonBall;
    }

    public int getRoomRankFirst() {
        return roomRankFirst;
    }

    public void setRoomRankFirst(int roomRankFirst) {
        this.roomRankFirst = roomRankFirst;
    }

    public int getRoomRankSecond() {
        return roomRankSecond;
    }

    public void setRoomRankSecond(int roomRankSecond) {
        this.roomRankSecond = roomRankSecond;
    }

    public int getRoomRankThird() {
        return roomRankThird;
    }

    public void setRoomRankThird(int roomRankThird) {
        this.roomRankThird = roomRankThird;
    }

    public int getFreeGame() {
        return freeGame;
    }

    public void setFreeGame(int freeGame) {
        this.freeGame = freeGame;
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getPrizeWheelGrand() {
        return prizeWheelGrand;
    }

    public void setPrizeWheelGrand(int prizeWheelGrand) {
        this.prizeWheelGrand = prizeWheelGrand;
    }

    public int getPrizeWheelMajor() {
        return prizeWheelMajor;
    }

    public void setPrizeWheelMajor(int prizeWheelMajor) {
        this.prizeWheelMajor = prizeWheelMajor;
    }

    public int getPrizeWheelMinor() {
        return prizeWheelMinor;
    }

    public void setPrizeWheelMinor(int prizeWheelMinor) {
        this.prizeWheelMinor = prizeWheelMinor;
    }

    public int getAgyptBox() {
        return agyptBox;
    }

    public void setAgyptBox(int agyptBox) {
        this.agyptBox = agyptBox;
    }

    public int getGossip() {
        return gossip;
    }

    public void setGossip(int gossip) {
        this.gossip = gossip;
    }

    public int getHeroBattle() {
        return heroBattle;
    }

    public void setHeroBattle(int heroBattle) {
        this.heroBattle = heroBattle;
    }

    public int getThunder() {
        return thunder;
    }

    public void setThunder(int thunder) {
        this.thunder = thunder;
    }

    public int getJackpotKingkong() {
        return jackpotKingkong;
    }

    public void setJackpotKingkong(int jackpotKingkong) {
        this.jackpotKingkong = jackpotKingkong;
    }

    public int getJackpotHero() {
        return jackpotHero;
    }

    public void setJackpotHero(int jackpotHero) {
        this.jackpotHero = jackpotHero;
    }

    public int getWhistle() {
        return whistle;
    }

    public void setWhistle(int whistle) {
        this.whistle = whistle;
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
