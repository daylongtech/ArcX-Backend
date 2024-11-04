package avatar.global.enumMsg.system;

import java.util.HashMap;
import java.util.Map;

/**

 */
public enum ServerSideTypeEnum {
    CLAWER(1,"clawer"),
    COIN_WONDER(2,"coin wonder"),
    DAYLONG(3,"Pusher+"),
    META_PUSHER(4,"Metapusher"),
    METARCADE(5,"Metarcade"),

    ARCX(8,"Arcx"),
    ;

    private int code;
    private String name;

    ServerSideTypeEnum(int code, String name){
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
        for (ServerSideTypeEnum getType : ServerSideTypeEnum.values()) {
            map.put(getType.getCode(), getType.getName());
        }
        return map;
    }

    /**

     */
    public static String getNameByCode(int code){
        return toMap().get(code);
    }
}
