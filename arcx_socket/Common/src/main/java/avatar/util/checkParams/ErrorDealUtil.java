package avatar.util.checkParams;

import avatar.util.LogUtil;

/**

 */
public class ErrorDealUtil {
    /**

     */
    public static void printError(Exception e){
        LogUtil.getLogger().error(e.toString());
        for(StackTraceElement element : e.getStackTrace()){
            LogUtil.getLogger().error(element.toString());
        }
    }
}
