package avatar.global.enumMsg.product.award;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**

 */
public enum EnergyExchangeGetTypeEnum {



    ;

    private int code;
    private String name;

    EnergyExchangeGetTypeEnum(int code, String name){
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
    public static List<EnergyExchangeGetTypeEnum> loadAll(){
        EnergyExchangeGetTypeEnum[] enumArr = EnergyExchangeGetTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }
    
    /**

     */
    public static String loadNameByCode(int code){
        EnergyExchangeGetTypeEnum[] msgArr = EnergyExchangeGetTypeEnum.values();
        for(EnergyExchangeGetTypeEnum enumMsg : msgArr){
            if(enumMsg.code == code){
                return enumMsg.getName();
            }
        }
        return null;
    }
}
