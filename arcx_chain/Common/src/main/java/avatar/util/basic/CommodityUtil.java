package avatar.util.basic;

import avatar.global.basicConfig.ConfigMsg;
import avatar.util.system.StrUtil;

/**

 */
public class CommodityUtil {
    /**

     */
    public static boolean normalFlag(int commodityType){
        return StrUtil.strToList(ConfigMsg.normalCommodity).contains(commodityType);
    }
}
