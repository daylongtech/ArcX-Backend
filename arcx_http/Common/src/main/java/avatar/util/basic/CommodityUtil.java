package avatar.util.basic;

import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
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
     * usdt
     */
    public static int usdt(){
        return CommodityTypeEnum.USDT.getCode();
    }

    /**

     */
    public static int property(){
        return CommodityTypeEnum.PROPERTY.getCode();
    }

    /**

     */
    public static int exp() {
        return CommodityTypeEnum.EXP.getCode();
    }
}
