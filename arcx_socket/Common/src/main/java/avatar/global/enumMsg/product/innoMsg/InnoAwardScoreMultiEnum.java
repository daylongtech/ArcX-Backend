package avatar.global.enumMsg.product.innoMsg;

import java.util.HashMap;
import java.util.Map;

/**

 */
public enum InnoAwardScoreMultiEnum {
    BIG_WIN(1,"big win"),
    HUGE_WIN(2,"huge win"),
    MEGA_WIN(3,"mega win"),
    ;

    private int code;
    private String name;

    InnoAwardScoreMultiEnum(int code, String name){
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
        for (InnoAwardScoreMultiEnum awardTypeEnum : InnoAwardScoreMultiEnum.values()) {
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
