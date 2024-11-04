package avatar.global.enumMsg.basic.nft;

import java.util.*;

/**

 */
public enum NftTypeEnum {




    ;

    private int code;
    private String name;

    NftTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**

     */
    public static List<NftTypeEnum> loadAll(){
        NftTypeEnum[] enumArr = NftTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (NftTypeEnum pay : NftTypeEnum.values()) {
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
