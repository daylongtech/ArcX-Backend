package avatar.util.excel;

import avatar.global.Config;

/**

 */
public class ExcelManager {
    private static final ExcelManager instance = new ExcelManager();
    public static final ExcelManager getInstance(){
        return instance;
    }

    private String getPath(){
        if(Config.getInstance().isWindowsOS()){
            return "../deploy/config/excel/";
        }else{
            return "./config/excel/";
        }
    }

    /**

     */
    public MyXlsReader buildReader(String fileName){
        fileName = String.format("%s%s", getPath() , fileName);
        MyXlsReader myXlsReader = new MyXlsReader(fileName);
        return myXlsReader;
    }

}
