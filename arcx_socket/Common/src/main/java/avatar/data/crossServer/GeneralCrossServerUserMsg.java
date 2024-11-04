package avatar.data.crossServer;

import java.io.Serializable;

/**

 */
public class GeneralCrossServerUserMsg implements Serializable {
    
    private int serverSideType;

    
    private int userId;

    
    private String nickName;

    
    private String imgUrl;

    
    private String nationCode;

    
    private String nationEn;

    
    private int userLevel;

    
    private int vipLevel;

    
    private CrossServerSearchProductPrizeMsg productPrizeMsg;

    
    private long createTime;

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getNationEn() {
        return nationEn;
    }

    public void setNationEn(String nationEn) {
        this.nationEn = nationEn;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public CrossServerSearchProductPrizeMsg getProductPrizeMsg() {
        return productPrizeMsg;
    }

    public void setProductPrizeMsg(CrossServerSearchProductPrizeMsg productPrizeMsg) {
        this.productPrizeMsg = productPrizeMsg;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
