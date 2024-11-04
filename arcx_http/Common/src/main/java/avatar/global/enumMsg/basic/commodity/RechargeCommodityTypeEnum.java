package avatar.global.enumMsg.basic.commodity;

import java.util.*;

/**

 */
public enum RechargeCommodityTypeEnum {



    ;

    private int code;
    private String name;

    RechargeCommodityTypeEnum(int code, String name){
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
    public static List<RechargeCommodityTypeEnum> loadAll(){
        RechargeCommodityTypeEnum[] enumArr = RechargeCommodityTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (RechargeCommodityTypeEnum awardTypeEnum : RechargeCommodityTypeEnum.values()) {
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
