package avatar.util.basic.general;

import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.util.system.StrUtil;

/**

 */
public class CommodityUtil {

    /**

     */
    public static boolean normalFlag(int commodityType){
        return StrUtil.strToList(ConfigMsg.normalCommodity).contains(commodityType);
    }

    /**

     */
    public static int gold(){
        return CommodityTypeEnum.GOLD_COIN.getCode();
    }

    /**
     * axc
     */
    public static int axc(){
        return CommodityTypeEnum.AXC.getCode();
    }

    /**

     */
    public static int exp() {
        return CommodityTypeEnum.EXP.getCode();
    }

    /**

     */
    public static String loadAwardName(int awardType, int awardId) {
        if(awardType>0) {
            String awardName;
            if(awardType==CommodityTypeEnum.PROPERTY.getCode()){
                awardName = PropertyTypeEnum.getNameByCode(awardId);
            }else{
                awardName = CommodityTypeEnum.getNameByCode(awardType);
            }
            return awardName;
        }else{
            return "";
        }
    }

}
