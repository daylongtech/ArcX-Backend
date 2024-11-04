package avatar.global.enumMsg.basic.recharge;

import java.util.*;

/**

 */
public enum PayTypeEnum {

    ;

    private int code;
    private String name;

    PayTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**

     */
    public static List<PayTypeEnum> loadAll(){
        PayTypeEnum[] enumArr = PayTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (PayTypeEnum pay : PayTypeEnum.values()) {
            map.put(pay.getCode(), pay.getName());
        }
        return map;
    }

    /**

     */
    public static String getPayMsgByType(int payType){
        return toMap().get(payType);
    }

}
