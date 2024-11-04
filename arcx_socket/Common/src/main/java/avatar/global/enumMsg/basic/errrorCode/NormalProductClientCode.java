package avatar.global.enumMsg.basic.errrorCode;

import java.util.HashMap;
import java.util.Map;

/**

 */
public enum NormalProductClientCode {






    ;


    private int code;

    private String name;

    NormalProductClientCode(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}


    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (ClientCode clientCode : ClientCode.values()) {
            map.put(clientCode.getCode(), clientCode.getName());
        }
        return map;
    }

    /**

     */
    public static String getErrorMsgByStatus(int status){
        return toMap().get(status);
    }

}
