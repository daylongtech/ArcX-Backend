package avatar.util.basic.general;

import avatar.data.basic.award.GeneralAwardMsg;

/**

 */
public class AwardUtil {
    /**

     */
    public static GeneralAwardMsg initGeneralAwardMsg(int awardType, int awardId, int awardImgId, int awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(awardType);
        msg.setCmdId(awardId);
        msg.setAwdPct(MediaUtil.getMediaUrl(ImgUtil.loadAwardImg(awardImgId)));
        msg.setAwdAmt(awardNum);
        return msg;
    }

    /**

     */
    public static GeneralAwardMsg initGeneralAwardMsg(int awardType, int awardId, String awardImgUrl, int awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(awardType);
        msg.setCmdId(awardId);
        msg.setAwdPct(awardImgUrl);
        msg.setAwdAmt(awardNum);
        return msg;
    }
}
