package avatar.global.enumMsg.basic;

import java.util.*;

/**

 */
public enum CommodityTypeEnum {


    AXC(2,"AXC"),



    SOLANA(6,"SOL"),
    USDT(7,"UDST"),

    ;

    private int code;
    private String name;

    CommodityTypeEnum(int code, String name){
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
    public static List<CommodityTypeEnum> loadAll(){
        CommodityTypeEnum[] enumArr = CommodityTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**

     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (CommodityTypeEnum awardTypeEnum : CommodityTypeEnum.values()) {
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
