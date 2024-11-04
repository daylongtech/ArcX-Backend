package avatar.util.solana;

import avatar.entity.solana.SolanaSignMsgEntity;
import avatar.global.enumMsg.system.TokensTypeEnum;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.LogUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class SolanaUtil {
    /**

     */
    public static SolanaSignMsgEntity initSolanaSignMsgEntity(String signature) {
        SolanaSignMsgEntity entity = new SolanaSignMsgEntity();
        entity.setSignature(signature);
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void dealUserBalance(int accountType, String accountMsg, double realNum) {
        if(realNum>=1) {
            if (accountType == TokensTypeEnum.AXC.getCode() || accountType == TokensTypeEnum.USDT.getCode()) {
                
                int userId = Web3GameShiftAccountDao.getInstance().loadDbUser(accountType, accountMsg);
                UserCostLogUtil.rechargeBalance(userId, accountType, realNum);
            }
        }else{

                    accountMsg, realNum);
        }
    }
}
