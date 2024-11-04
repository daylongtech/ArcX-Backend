package avatar.global.enumMsg.product;

/**

 */
public enum ProductTypeEnum {



    ;

    private int code;
    private String name;

    ProductTypeEnum(int code, String name){
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
    public static String loadNameByCode(int code){
        ProductTypeEnum[] msgArr = ProductTypeEnum.values();
        for(ProductTypeEnum enumMsg : msgArr){
            if(enumMsg.code == code){
                return enumMsg.getName();
            }
        }
        return null;
    }
}
