package avatar.global.enumMsg.basic.commodity;

import java.util.*;

/**

 */
public enum PropertyUseTypeEnum {

    ;

    private int code;
    private String name;

    PropertyUseTypeEnum(int code, String name){
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
    public static List<PropertyUseTypeEnum> loadAll(){
        PropertyUseTypeEnum[] enumArr = PropertyUseTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (PropertyUseTypeEnum awardTypeEnum : PropertyUseTypeEnum.values()) {
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
