package avatar.data.product.innoMsg;

/**

 */
public class InnoReceiveProductOperateMsg {
    private int status;

    private String alias;

    private int userId;

    private String userName;

    private String imgUrl;

    private int serverSideType;

    private int innoProductOperateType;

    private long onProductTime;

    private int awardType;

    private int breakType;

    private int coinNum;

    private int awardNum;

    private int isStart;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
    }

    public int getInnoProductOperateType() {
        return innoProductOperateType;
    }

    public void setInnoProductOperateType(int innoProductOperateType) {
        this.innoProductOperateType = innoProductOperateType;
    }

    public long getOnProductTime() {
        return onProductTime;
    }

    public void setOnProductTime(long onProductTime) {
        this.onProductTime = onProductTime;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public int getBreakType() {
        return breakType;
    }

    public void setBreakType(int breakType) {
        this.breakType = breakType;
    }

    public int getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }
}
