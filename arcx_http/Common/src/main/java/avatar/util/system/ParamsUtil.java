package avatar.util.system;

import avatar.global.enumMsg.basic.system.MobilePlatformTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.util.basic.CheckUtil;

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
        return map.get(paramsName).toString().trim();
    }

    /**

     */
    public static long longParmasNotNull(Map<String, Object> map, String paramsName) {
        return Long.parseLong(map.get(paramsName).toString());
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
    public static String ip(Map<String, Object> map) {
        String ip = ParamsUtil.stringParmas(map, "ip");
        if(!StrUtil.checkEmpty(ip) && ip.contains(",")){
            List<String> ipList = StrUtil.strToStrList(ip, ",");
            ip = ipList.get(0);
        }
        return ip;
    }

    /**

     */
    public static String accessToken(Map<String, Object> map) {
        return stringParmas(map, "aesTkn");
    }

    /**
     * mac ID
     */
    public static String macId(Map<String, Object> map) {
        return stringParmas(map, "mcCd");
    }

    /**

     */
    public static int productId(Map<String, Object> map) {
        return intParmas(map, "devId");
    }

    /**

     */
    public static int loginWayType(Map<String, Object> map) {
        return intParmas(map, "lgWt");
    }

    /**

     */
    public static int loadMobilePlatform(Map<String, Object> map) {
        String platform = loadPlatform(map);
        if(platform.equals("ios")){
            
            return MobilePlatformTypeEnum.APPLE.getCode();
        }else if(platform.equals("android")){
            
            return MobilePlatformTypeEnum.ANDROID.getCode();
        }else{
            //web
            return MobilePlatformTypeEnum.WEB.getCode();
        }
    }

    /**

     */
    public static String loadPlatform(Map<String, Object> map){
        return ParamsUtil.stringParmas(map, "plm");
    }

    /**

     */
    public static String versionCode(Map<String, Object> map) {
        return stringParmas(map, "vsCd");
    }

    /**

     */
    public static int userId(Map<String, Object> map) {
        int arcxUid = 0;
        String accessToken = accessToken(map);
        if(!StrUtil.checkEmpty(accessToken)){
            
            arcxUid = UserAccessTokenDao.getInstance().loadByToken(accessToken);
        }
        return arcxUid;
    }

    /**

     */
    public static String httpRequestDomain(String domainName, Map<String, Object> paramsMap){
        StringBuilder retStr = new StringBuilder();
        retStr.append(domainName);
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
        return retStr.toString();
    }

    /**

     */
    public static int pageNum(Map<String, Object> map){
        return ParamsUtil.intParmasNotNull(map, "pgNm");
    }

    /**

     */
    public static int pageSize(Map<String, Object> map){
        return ParamsUtil.intParmasNotNull(map, "pgAmt");
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

    /**

     */
    public static boolean isConfirm(int flag){
        return flag== YesOrNoEnum.YES.getCode();
    }

    /**

     */
    public static String nftCode(Map<String, Object> map){
        return ParamsUtil.stringParmasNotNull(map, "nftCd");
    }

}
