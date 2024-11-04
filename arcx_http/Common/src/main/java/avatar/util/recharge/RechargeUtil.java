package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.recharge.gold.RechargeGoldInfoEntity;
import avatar.global.enumMsg.basic.commodity.RechargeCommodityTypeEnum;
import avatar.global.enumMsg.basic.recharge.PayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.recharge.gold.RechargeGoldInfoDao;
import avatar.util.nft.SellGoldMachineUtil;
import avatar.util.system.ParamsUtil;

import java.util.List;
import java.util.Map;

/**

 */
public class RechargeUtil {

    /**

     */
    public static void shoppingMall(int userId, Map<String, Object> dataMap) {
        if(!SuperPlayerUtil.isSuperPlayer(userId)) {
            dataMap.put("spPlyIfo", SuperPlayerUtil.rechargeSuperPlayerMsg());
        }
        dataMap.put("selCnMch", SellGoldMachineUtil.showImg());
        dataMap.put("cnTbln", RechargeGoldUtil.loadCoinList());
        dataMap.put("ppyIfo", RechargePropertyUtil.loadPropertyMsg(userId));
    }

    /**

     */
    public static boolean commodityCheckEmpty(int userId, int rechargeType, int commodityId) {
        boolean flag = false;
        if(rechargeType==RechargeCommodityTypeEnum.GOLD.getCode()){
            
            RechargeGoldInfoEntity entity = RechargeGoldInfoDao.getInstance().loadById(commodityId);
            if(entity==null || !ParamsUtil.isConfirm(entity.getActiveFlag())){
                flag = true;
            }
        }else if(rechargeType==RechargeCommodityTypeEnum.PROPERTY.getCode()){
            
            List<Integer> list = RechargePropertyUtil.userActiveList(userId);
            if(list.size()==0 || !list.contains(commodityId)){
                flag = true;
            }
        }
        return flag;
    }

    /**

     */
    public static int commodityDirectPurchase(Map<String, Object> map, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();
        int userId = ParamsUtil.userId(map);
        int rechargeType = ParamsUtil.intParmasNotNull(map, "rcgCmdTp");
        int commodityId = ParamsUtil.intParmas(map, "cmdId");
        int productId = ParamsUtil.productId(map);
        if(rechargeType==RechargeCommodityTypeEnum.SUPER_PLAYER.getCode()){
            
            status = SuperPlayerUtil.openSuperPlayer(userId, retList);
        }else if(rechargeType==RechargeCommodityTypeEnum.GOLD.getCode()){
            
            status = RechargeGoldUtil.rechargeGold(userId, productId, commodityId, retList);
        }else if(rechargeType==RechargeCommodityTypeEnum.PROPERTY.getCode()){
            
            status = RechargePropertyUtil.rechargeProperty(userId, commodityId, retList);
        }
        return status;
    }

    /**

     */
    public static boolean isDirectPayType(int payType) {
        return payType==PayTypeEnum.USDT.getCode();
    }
}
