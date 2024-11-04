package avatar.global.enumMsg.product.innoMsg;

import java.util.HashMap;
import java.util.Map;

/**

 */
public enum InnoProductBreakTypeEnum {







    ;

    private int code;
    private String name;

    InnoProductBreakTypeEnum(int code, String name){
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
        for (InnoProductBreakTypeEnum breakTypeEnum : InnoProductBreakTypeEnum.values()) {
            map.put(breakTypeEnum.getCode(), breakTypeEnum.getName());
        }
        return map;
    }

    /**

     */
    public static String getNameByCode(int code){
        return toMap().get(code);
    }
}
