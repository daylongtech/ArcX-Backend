package avatar.module.user.thirdpart;

import avatar.global.enumMsg.system.TokensTypeEnum;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import com.sun.org.apache.regexp.internal.RE;

import java.util.List;

/**

 */
public class Web3GameShiftAccountDao {
    private static final Web3GameShiftAccountDao instance = new Web3GameShiftAccountDao();
    public static final Web3GameShiftAccountDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**

     */
    public int loadDbUser(int accountType, String accountMsg){
        String sql = "select user_id from web3_game_shift_account where ";
        if(accountType==TokensTypeEnum.AXC.getCode()){
            //axc
            sql += " axc_account =? ";
        }else if(accountType==TokensTypeEnum.USDT.getCode()){
            //usdt
            sql += " usdt_account =? ";
        }else{
            return 0;
        }
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{accountMsg});
        return StrUtil.listNum(list);
    }
}
