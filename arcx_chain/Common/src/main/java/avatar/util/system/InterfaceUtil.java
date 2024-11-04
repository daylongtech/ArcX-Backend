package avatar.util.system;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class InterfaceUtil {
	/**



     */
    public static String interfaceUtil(String path,String data) {
    	String retStr = "";
        try {
            URL url = new URL(path);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;
            
//          conn.setRequestMethod("POST");

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"); 
            
            
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            out = new PrintWriter(conn.getOutputStream());
            
            out.print(data);
            
            out.flush();
            
            InputStream is = conn.getInputStream();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
            	System.out.println(str);
            }
            retStr = br.readLine();
            
            is.close();
            
            
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(retStr);
        return retStr;
    }
    
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            
            URLConnection connection = realUrl.openConnection();
            
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            connection.connect();
            
            Map<String, List<String>> map = connection.getHeaderFields();
            
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            
            in = new BufferedReader
                (new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**

     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String httpPostWithjson(String url, String json) throws IOException {
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            BasicResponseHandler handler = new BasicResponseHandler();
            StringEntity entity = new StringEntity(json, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            result = httpClient.execute(httpPost, handler);
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
}
