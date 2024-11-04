package avatar.util.system;

import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.util.basic.system.CheckUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 */
public class ParamsUtil {

    /**

     */
    public static int intParmasNotNull(Map<String, Object> map, String paramsName) {
        return Integer.parseInt(map.get(paramsName).toString());
    }

    /**

     */
    public static int intStatusParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName)==null?-1:Integer.parseInt(map.get(paramsName).toString());
    }

    /**

     */
    public static int intParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName)==null?0:Integer.parseInt(map.get(paramsName).toString());
    }

    /**

     */
    public static double doubleParmasNotNull(Map<String, Object> map, String paramsName) {
        return Double.parseDouble(map.get(paramsName).toString());
    }

    /**

     */
    public static String stringParmasNotNull(Map<String, Object> map, String paramsName) {
        return map.get(paramsName).toString();
    }

    /**

     */
    public static String stringParmasNotReplace(Map<String, Object> map, String paramsName) {
        return map.get(paramsName).toString();
    }

    /**

     */
    public static String stringParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName)==null?"":map.get(paramsName).toString();
    }

    /**

     */
    public static Object objectParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName);
    }

    /**

     */
    public static String operateMsg(String name, Object desc) {
        return "【"+name+"】"+desc;
    }

    /**

     */
    public static boolean isSuccess(int status) {
        return status == ClientCode.SUCCESS.getCode();
    }

    /**

     */
    public static List<Integer> idList(Map<String, Object> map) {
        List<Integer> idList = new ArrayList<>();
        Object object = map.get("idList");
        if(object!=null){
            String objStr = object.toString();
            idList = JsonUtil.strToStrIntegerList(objStr);
        }
        return idList;
    }

    /**

     */
    public static boolean isVerifyError(int status) {
        return status == ClientCode.VERIFY_SIGN_ERROR.getCode();
    }

    /**

     */
    public static boolean isConfirm(int flag){
        return flag== YesOrNoEnum.YES.getCode();
    }

    /**

     */
    public static String httpRequestProduct(String route, Map<String, Object> paramsMap){
        StringBuilder retStr = new StringBuilder();
        String name = CheckUtil.loadDomainName();
        if(!StrUtil.checkEmpty(name)) {
            retStr.append(name);
            retStr.append(route);
            if(paramsMap.keySet().size()>0){
                retStr.append("?");
                List<String> keyList = new ArrayList<>(paramsMap.keySet());
                for(int i=0;i<keyList.size();i++){
                    if(i>0){
                        retStr.append("&");
                    }
                    String paramsName = keyList.get(i);
                    retStr.append(paramsName);
                    retStr.append("=");
                    retStr.append(paramsMap.get(paramsName));
                }
            }
        }
        return retStr.toString();
    }

}
