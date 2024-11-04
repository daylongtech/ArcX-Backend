package avatar.net.http;

import avatar.global.linkMsg.http.NoticeHttpCmdName;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class PostMethod {

    /**

     */
    public static boolean isGeneralPost(String uri){
        List<String> list = new ArrayList<>();
        boolean flag = false;
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**

     */
    public static boolean isCallBack(String uri){
        List<String> list = new ArrayList<>();
        list.add(NoticeHttpCmdName.SYSTEM_NOTICE);
        boolean flag = false;
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**

     */
    public static boolean isNoCheckCallBack(String uri){
        List<String> list = new ArrayList<>();
        boolean flag = false;
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**

     */
    public static boolean isNoConstainIp(String uri){
        List<String> list = new ArrayList<>();
        boolean flag = false;
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**

     */
    public static boolean isPost(String uri){
        List<String> list = new ArrayList<>();
        return list.contains(uri);
    }
}
