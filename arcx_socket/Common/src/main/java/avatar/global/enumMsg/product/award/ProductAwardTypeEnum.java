package avatar.global.enumMsg.product.award;

import java.util.HashMap;
import java.util.Map;

/**

 */
public enum ProductAwardTypeEnum {



















    ;

    private int code;
    private String name;

    ProductAwardTypeEnum(int code, String name){
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
        for (ProductAwardTypeEnum awardTypeEnum : ProductAwardTypeEnum.values()) {
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
