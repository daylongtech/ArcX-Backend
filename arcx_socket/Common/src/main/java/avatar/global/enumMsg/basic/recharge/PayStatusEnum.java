package avatar.global.enumMsg.basic.recharge;

/**

 */
public enum PayStatusEnum {







    private int code;

    private String name;

    PayStatusEnum(int code, String name) {
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
