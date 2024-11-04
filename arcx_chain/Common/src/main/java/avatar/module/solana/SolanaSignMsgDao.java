package avatar.module.solana;

import avatar.entity.solana.SolanaSignMsgEntity;
import avatar.util.GameData;
import avatar.util.solana.SolanaUtil;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class SolanaSignMsgDao {
    private static final SolanaSignMsgDao instance = new SolanaSignMsgDao();
    public static final SolanaSignMsgDao getInstance() {
        return instance;
    }

    //=========================db===========================

    /**

     */
    public boolean loadDbBySign(String signature){
        String sql = "select id from solana_sign_msg where signature=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{signature});
        boolean flag = StrUtil.listSize(list)>0;
        if(!flag){
            
            insert(SolanaUtil.initSolanaSignMsgEntity(signature));
        }
        return flag;
    }

    /**

     */
    private void insert(SolanaSignMsgEntity entity) {
        GameData.getDB().insert(entity);
    }

}
