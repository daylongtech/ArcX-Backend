package avatar.data.product.innoMsg;

/**

 */
public class InnoVoiceNoticeMsg {
    private String alias;

    private int userId;

    private int serverSideType;

    private int voiceType;

    private int isStart;

    private int isEndSwitch;

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

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
    }

    public int getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(int voiceType) {
        this.voiceType = voiceType;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }

    public int getIsEndSwitch() {
        return isEndSwitch;
    }

    public void setIsEndSwitch(int isEndSwitch) {
        this.isEndSwitch = isEndSwitch;
    }
}
