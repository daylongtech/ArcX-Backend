package avatar.util.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**

 */
public class FileUtil {
    /**

     * @param filePath
     * @return
     */
    public static List<String> readTxtFile(String filePath){
        List<String> list = new ArrayList<>();
        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ 
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    System.out.println(lineTxt);
                    if(lineTxt.contains("、")) {
                        String[] strArr = lineTxt.split("、");
                        for(String str : strArr){
                            list.add(str);
                        }
                    }
                }
                read.close();
            }else{

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return list;
    }

}
