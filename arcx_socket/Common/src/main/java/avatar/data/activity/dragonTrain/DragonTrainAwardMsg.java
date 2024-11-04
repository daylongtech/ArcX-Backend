package avatar.data.activity.dragonTrain;


import avatar.entity.activity.dragonTrain.info.DragonTrainWheelIconMsgEntity;

/**

 */
public class DragonTrainAwardMsg {
    
    private int resultAwardNum;

    
    private DragonTrainWheelIconMsgEntity wheelIconMsg;

    public int getResultAwardNum() {
        return resultAwardNum;
    }

    public void setResultAwardNum(int resultAwardNum) {
        this.resultAwardNum = resultAwardNum;
    }

    public DragonTrainWheelIconMsgEntity getWheelIconMsg() {
        return wheelIconMsg;
    }

    public void setWheelIconMsg(DragonTrainWheelIconMsgEntity wheelIconMsg) {
        this.wheelIconMsg = wheelIconMsg;
    }
}
