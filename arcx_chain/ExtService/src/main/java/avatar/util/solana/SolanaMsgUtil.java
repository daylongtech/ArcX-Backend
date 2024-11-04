package avatar.util.solana;

import avatar.global.basicConfig.LocalSolanaConfigMsg;
import avatar.global.basicConfig.OnlineSolanaConfigMsg;
import avatar.util.basic.system.CheckUtil;

/**

 */
public class SolanaMsgUtil {
    /**

     */
    public static String domainName(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.domain;
        }else{
            
            return OnlineSolanaConfigMsg.domain;
        }
    }

    /**

     */
    public static String solSubAcccount(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.solSubAccount;
        }else{
            
            return OnlineSolanaConfigMsg.solSubAccount;
        }
    }

    /**

     */
    public static String axcSubAcccount(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.axcSubAccount;
        }else{
            
            return OnlineSolanaConfigMsg.axcSubAccount;
        }
    }

    /**

     */
    public static String usdtSubAcccount(){
        if(CheckUtil.isTestEnv()){
            
            return LocalSolanaConfigMsg.usdtSubAccount;
        }else{
            
            return OnlineSolanaConfigMsg.usdtSubAccount;
        }
    }

}
