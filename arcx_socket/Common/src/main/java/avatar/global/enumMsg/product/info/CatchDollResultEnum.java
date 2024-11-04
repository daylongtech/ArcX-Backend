package avatar.global.enumMsg.product.info;

/**

 */
public enum CatchDollResultEnum {



    ;

    private int code;
    private String name;
    private String instruct;

    CatchDollResultEnum(int code, String name, String instruct){
        this.code = code;
        this.name = name;
        this.instruct = instruct;
    }

    public int getCode(){
        return code;
    }

    public String getInstruct(){
        return instruct;
    }
}
