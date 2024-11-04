package avatar.global.enumMsg.user;

import java.util.HashMap;
import java.util.Map;

/**

 */
public enum UserStatusEnum {



    ;

    private int code;
    private String name;

    UserStatusEnum(int code, String name){
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
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (UserStatusEnum enumMsg : UserStatusEnum.values()) {
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
