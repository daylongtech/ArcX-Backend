package avatar.service.test;

import avatar.global.basicConfig.basic.LocalSolanaConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.activity.WelfareUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.thirdpart.SolanaUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class TestService {
    /**

     */
    public static void clearSignMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();
        if(CheckUtil.isTestEnv()) {
            int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");
            int continueDay = ParamsUtil.intParmas(map, "continueDay");
            
            WelfareUtil.resetUserSignMsg(userId, continueDay);
        }else{

        }
        
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }

    /**

     */
    public static void serverTest(Map<String, Object> map, Session session) {
        
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
//        GameShiftUtil.register(1000004,"7856176776@qq.com");
//        GameShiftUtil.loadWalletAddress(100003);
//        SolanaUtil.createAccount(LocalSolanaConfigMsg.walleyAccount, SolanaUtil.usdtMintPubkey());
//        SolanaUtil.accountBalance("Ask2c73HoBMjDsP1JFZzF1niUf5bEBnMq9qTCz8hRgQC");
//        SolanaUtil.accountBalance("4bKD4RuruZDvGhDBsKKnYczU4dfXvGDsgLtQGKN1EEQy");
//        System.out.println(GameShiftUtil.getBalance(100002));
//        SolanaUtil.accountBalance("4bKD4RuruZDvGhDBsKKnYczU4dfXvGDsgLtQGKN1EEQy");
        SolanaUtil.accountBalance("CEQkasup5F2mWNoEC9oEhyWspsDtLuZxeTdXJxXaMrTQ");
    }
}
