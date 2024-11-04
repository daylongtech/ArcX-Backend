package avatar.util.basic;

import avatar.data.basic.award.GeneralAwardMsg;

/**

 */
public class AwardUtil {
    /**

     */
    public static GeneralAwardMsg initGeneralAwardMsg(int commodityType, int awardImgId, int awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(commodityType);
        msg.setAwdPct(MediaUtil.getMediaUrl(ImgUtil.loadAwardImg(awardImgId)));
        msg.setAwdAmt(awardNum);
        return msg;
    }

    /**

     */
    public static GeneralAwardMsg initGeneralAwardMsg(int commodityType, String imgUrl, long awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(commodityType);
        msg.setAwdPct(imgUrl);
        msg.setAwdAmt(awardNum);
        return msg;
    }
}
