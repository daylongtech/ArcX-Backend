package avatar.global.enumMsg.system;

import java.util.HashMap;
import java.util.Map;

/**

 */
public enum ClientCode {


    LIMIT(1000,"limit", "limit"),


























            "The maintenance personnel have been notified, please be patient!"),















    ;


    private int code;

    private String name;

    private String english;

    ClientCode(int code, String name, String english){
        this.code = code;
        this.name = name;
        this.english = english;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    public String getEnglish(){return english;}

    /**

     */
    public static Map<Integer, String> toEnglishMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (ClientCode clientCode : ClientCode.values()) {
            map.put(clientCode.getCode(), clientCode.getEnglish());
        }
        return map;
    }

    /**

     */
    public static String getErrorMsg(int status){
        return toEnglishMap().get(status);
    }

}
