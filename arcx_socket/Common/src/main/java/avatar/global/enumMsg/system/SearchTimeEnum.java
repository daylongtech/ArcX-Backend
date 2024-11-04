package avatar.global.enumMsg.system;

/**

 */
public enum SearchTimeEnum {




    ;

    private int code;
    private String name;
    private int month;

    SearchTimeEnum(int code, String name, int month){
        this.code = code;
        this.name = name;
        this.month = month;
    }

    public int getCode(){
        return code;
    }

    public int getMonth(){return month;}
}
