package avatar.util.thirdpart;

import avatar.entity.basic.ip.IpAddressEntity;
import avatar.global.basicConfig.thirdpart.IcretAdressMsg;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;


/**

 */
public class IcretAddressUtil {
    /**

     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    /**

     */
    public static String loadAddressByIp(String ip){
        String json = "";
        String urlSend = IcretAdressMsg.ipHost +"?IP_ADDR=" + ip;   
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + IcretAdressMsg.ipAppCode);
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                json = read(httpURLCon.getInputStream());
            }
        } catch (MalformedURLException e) {

        } catch (UnknownHostException e) {

        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        }
        return json;
    }

    /**

     */
    public static IpAddressEntity initIpAddress(String ip, String address) {
        String enShort = "";
        String enName = "";
        String global = "";
        String nation = "";
        String province = "";
        String city = "";
        String adcode = "";
        double lon = 0;
        double lat = 0;
        try {
            Map<String, Object> dataMap = JsonUtil.strToMap(address);
            if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("ENTITY")) {
                Map<String, Object> entityMap = (Map<String, Object>) dataMap.get("ENTITY");
                if(entityMap!=null && entityMap.size()>0 && entityMap.containsKey("INPUT_IP_ADDRESS")) {
                    Map<String, Object> resultMap = (Map<String, Object>) entityMap.get("INPUT_IP_ADDRESS");
                    if (resultMap != null && resultMap.size() > 0) {
                        
                        if (resultMap.containsKey("NATION_NAME_EN_ABBR")) {
                            enShort = (String) resultMap.get("NATION_NAME_EN_ABBR");
                            if (StrUtil.checkEmpty(enShort) || enShort.equals("null")) {
                                enShort = "";
                            }
                        }
                        
                        if (resultMap.containsKey("NATION_NAME_EN")) {
                            enName = (String) resultMap.get("NATION_NAME_EN");
                            if (StrUtil.checkEmpty(enName) || enName.equals("null")) {
                                enName = "";
                            }
                        }
                        
                        if (resultMap.containsKey("GLOBAL")) {
                            global = (String) resultMap.get("GLOBAL");
                            if (StrUtil.checkEmpty(global) || global.equals("null")) {
                                global = "";
                            }
                        }
                        
                        if (resultMap.containsKey("NATION")) {
                            nation = (String) resultMap.get("NATION");
                            if (StrUtil.checkEmpty(nation) || nation.equals("null")) {
                                nation = "";
                            }
                        }
                        
                        if (resultMap.containsKey("PROVINCE")) {
                            province = (String) resultMap.get("PROVINCE");
                            if (StrUtil.checkEmpty(province) || province.equals("null")) {
                                province = "";
                            }
                        }
                        
                        if (resultMap.containsKey("CITY")) {
                            city = (String) resultMap.get("CITY");
                            if (StrUtil.checkEmpty(city) || city.equals("null")) {
                                city = "";
                            }
                        }
                        
                        if (resultMap.containsKey("DISTRICT")) {
                            adcode = resultMap.get("DISTRICT").toString();
                            if (StrUtil.checkEmpty(adcode) || adcode.equals("null")) {
                                adcode = "";
                            }
                        }
                        //GPS
                        if (resultMap.containsKey("GPS") && resultMap.get("GPS") != null
                                && !StrUtil.checkEmpty(resultMap.get("GPS").toString())) {
                            String gps = resultMap.get("GPS").toString();
                            if (gps.contains(",")) {
                                
                                lon = Double.parseDouble(gps.split(",")[0]);
                                
                                lat = Double.parseDouble(gps.split(",")[1]);
                            }
                        }
                        if (StrUtil.checkEmpty(nation)) {

                        }
                        IpAddressEntity entity = new IpAddressEntity();
                        entity.setIp(ip);//ip
                        entity.setEnShort(enShort);
                        entity.setEnName(enName);
                        entity.setGlobal(global);
                        entity.setNation(nation);
                        entity.setProvince(province);
                        entity.setCity(city);
                        entity.setAdcode(adcode);
                        entity.setLon(lon);
                        entity.setLat(lat);
                        entity.setCreateTime(TimeUtil.getNowTimeStr());
                        return entity;
                    }
                }
            } else {

                return null;
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            return null;
        }
        return null;
    }
}
