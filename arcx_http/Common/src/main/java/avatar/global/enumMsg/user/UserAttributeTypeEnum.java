package avatar.global.enumMsg.user;

import java.util.*;

/**

 */
public enum UserAttributeTypeEnum {







    ;

    private int code;
    private String name;

    UserAttributeTypeEnum(int code, String name){
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
    public static List<UserAttributeTypeEnum> loadAll(){
        UserAttributeTypeEnum[] enumArr = UserAttributeTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (UserAttributeTypeEnum enumMsg : UserAttributeTypeEnum.values()) {
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
