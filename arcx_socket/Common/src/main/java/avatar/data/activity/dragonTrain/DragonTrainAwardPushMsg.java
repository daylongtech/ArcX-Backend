package avatar.data.activity.dragonTrain;

import avatar.data.basic.award.GeneralAwardMsg;

import java.util.List;

/**

 */
public class DragonTrainAwardPushMsg {
    
    private List<String> icTbln;

    
    private List<DragonTrainAwardIndexMsg> icAwdTbln;

    
    private List<GeneralAwardMsg> awdTbln;

    public List<String> getIcTbln() {
        return icTbln;
    }

    public void setIcTbln(List<String> icTbln) {
        this.icTbln = icTbln;
    }

    public List<DragonTrainAwardIndexMsg> getIcAwdTbln() {
        return icAwdTbln;
    }

    public void setIcAwdTbln(List<DragonTrainAwardIndexMsg> icAwdTbln) {
        this.icAwdTbln = icAwdTbln;
    }

    public List<GeneralAwardMsg> getAwdTbln() {
        return awdTbln;
    }

    public void setAwdTbln(List<GeneralAwardMsg> awdTbln) {
        this.awdTbln = awdTbln;
    }
}
