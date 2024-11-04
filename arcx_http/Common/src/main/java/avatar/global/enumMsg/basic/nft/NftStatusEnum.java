package avatar.global.enumMsg.basic.nft;

import java.util.*;

/**

 */
public enum NftStatusEnum {




    ;

    private int code;
    private String name;

    NftStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**

     */
    public static List<NftStatusEnum> loadAll(){
        NftStatusEnum[] enumArr = NftStatusEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (NftStatusEnum pay : NftStatusEnum.values()) {
            map.put(pay.getCode(), pay.getName());
        }
        return map;
    }

    /**

     */
    public static String getNameByCode(int code){
        return toMap().get(code);
    }

}
