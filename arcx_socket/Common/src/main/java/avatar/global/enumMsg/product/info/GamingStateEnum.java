package avatar.global.enumMsg.product.info;

/**

 */
public enum GamingStateEnum {




    ;

    private int code;
    private String name;

    GamingStateEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }
}
