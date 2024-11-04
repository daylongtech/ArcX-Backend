package avatar.util.thirdpart;

import avatar.data.thirdpart.Web3WalletMsg;
import avatar.entity.user.thirdpart.Web3AxcAccountEntity;
import avatar.module.user.thirdpart.Web3AxcAccountDao;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**

 */
public class Web3Util {

    /**

     */
    private static Web3AxcAccountEntity initWeb3AxcAccountEntity(int userId, Web3WalletMsg msg) {
        Web3AxcAccountEntity entity = new Web3AxcAccountEntity();
        entity.setUserId(userId);
        entity.setAxcAccount(msg.getAta());
        entity.setAccountPk(msg.getAccount().getPk());
        entity.setAccountSk(msg.getAccount().getSk());
        entity.setCreateTime(TimeUtil.getNowTimeStr());
        return entity;
    }

    /**

     */
    public static void addAxcAccount(int userId) {
        
        Web3AxcAccountEntity entity = Web3AxcAccountDao.getInstance().loadByMsg(userId);
        if(entity==null){
            
            Web3WalletMsg msg = SolanaUtil.createAccount("", SolanaUtil.axcMintPubkey());
            
            if(!StrUtil.checkEmpty(msg.getAta()) && !StrUtil.checkEmpty(msg.getAccount().getPk()) &&
                    !StrUtil.checkEmpty(msg.getAccount().getSk())){
                Web3AxcAccountDao.getInstance().insert(initWeb3AxcAccountEntity(userId, msg));
            }
        }
    }
}
