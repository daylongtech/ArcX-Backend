package avatar.global.enumMsg.basic.system;

/**

 */
public enum ServerTypeEnum {




    private int code;

    private String name;

    ServerTypeEnum(int code, String name) {
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
