package avatar.global.enumMsg.log;

import java.util.*;

/**

 */
public enum UserOperateTypeEnum {












    ;

    private int code;
    private String name;

    UserOperateTypeEnum(int code, String name){
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
    public static List<UserOperateTypeEnum> loadAll(){
        UserOperateTypeEnum[] enumArr = UserOperateTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (UserOperateTypeEnum enumMsg : UserOperateTypeEnum.values()) {
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
