package avatar.global.enumMsg.basic.commodity;

import java.util.*;

/**

 */
public enum PropertyTypeEnum {











    ;

    private int code;
    private String name;

    PropertyTypeEnum(int code, String name){
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
    public static List<PropertyTypeEnum> loadAll(){
        PropertyTypeEnum[] enumArr = PropertyTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (PropertyTypeEnum awardTypeEnum : PropertyTypeEnum.values()) {
            map.put(awardTypeEnum.getCode(), awardTypeEnum.getName());
        }
        return map;
    }

    /**

     */
    public static String getNameByCode(int code){
        return toMap().get(code);
    }
}
