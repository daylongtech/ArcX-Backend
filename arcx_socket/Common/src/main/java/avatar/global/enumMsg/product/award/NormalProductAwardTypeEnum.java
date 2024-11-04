package avatar.global.enumMsg.product.award;

import java.util.*;

/**

 */
public enum NormalProductAwardTypeEnum {


    private int code;
    private String name;

    NormalProductAwardTypeEnum(int code, String name){
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
    public static List<NormalProductAwardTypeEnum> loadAll(){
        NormalProductAwardTypeEnum[] enumArr = NormalProductAwardTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (NormalProductAwardTypeEnum enumMsg : NormalProductAwardTypeEnum.values()) {
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
