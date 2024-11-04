package avatar.util.system;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**

 */
public class ExtConfig {
    private static ExtConfig config=new ExtConfig();
    public static ExtConfig getInstance(){return config;}
    private static final Properties p=new Properties();
    
    public static String localPublicIp;
    public static void init() {
        InputStream in = null;
        in = ExtConfig.class.getClassLoader().getResourceAsStream("extConfig.properties");
        try {
            p.load(in);
            localPublicIp = p.getProperty("localPublicIp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
