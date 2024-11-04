package avatar.global.enumMsg.product.info;

/**

 */
public enum ProductOperationEnum {



























    ;

    private int code;
    private String name;

    ProductOperationEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**

     */
    public static String loadByCode(int code){
        ProductOperationEnum[] msgArr = ProductOperationEnum.values();
        for(ProductOperationEnum enumMsg : msgArr){
            if(enumMsg.code == code){
                return enumMsg.getName();
            }
        }
        return null;
    }
}
