package avatar.global.enumMsg.product.info;

/**

 */
public enum ProductSecondTypeEnum {







    ;

    private int code;
    private String name;
    private String enName;

    ProductSecondTypeEnum(int code, String name, String enName){
        this.code = code;
        this.name = name;
        this.enName = enName;
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public String getEnName() {
        return enName;
    }

}
