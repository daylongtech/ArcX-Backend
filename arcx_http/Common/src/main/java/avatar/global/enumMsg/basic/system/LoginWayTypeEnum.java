package avatar.global.enumMsg.basic.system;

import java.util.*;

/**

 */
public enum LoginWayTypeEnum {



    ;

    private int code;
    private String name;

    LoginWayTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    /**

     */
    public static List<LoginWayTypeEnum> loadAll(){
        LoginWayTypeEnum[] enumArr = LoginWayTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }
    
    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (LoginWayTypeEnum enumMsg : LoginWayTypeEnum.values()) {
            map.put(enumMsg.getCode(), enumMsg.getName());
        }
        return map;
    }

    /**

     */
    public static String getNameByCode(int code){
        return toMap().get(code);
    }
}
