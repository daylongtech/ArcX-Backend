package avatar.global.enumMsg.basic.system;

import java.util.*;

/**

 */
public enum MobilePlatformTypeEnum {


    WEB(3,"web"),
    ;

    private int code;
    private String name;

    MobilePlatformTypeEnum(int code, String name){
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
    public static List<MobilePlatformTypeEnum> loadAll(){
        MobilePlatformTypeEnum[] enumArr = MobilePlatformTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (MobilePlatformTypeEnum enumMsg : MobilePlatformTypeEnum.values()) {
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
