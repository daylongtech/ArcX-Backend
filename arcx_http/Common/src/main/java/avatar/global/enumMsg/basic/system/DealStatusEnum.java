package avatar.global.enumMsg.basic.system;

/**

 */
public enum DealStatusEnum {


    ;

    private int code;
    private String name;

    DealStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }
}
