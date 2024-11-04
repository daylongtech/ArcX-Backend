package avatar.data.product.normalProduct;

import avatar.data.product.general.ResponseGeneralMsg;

import java.util.Map;

/**

 */
public class InnerProductJsonMapMsg {
    
    private int cmd;

    
    private int hostId;

    
    private Map<String, Object> dataMap;

    
    private int status;

    
    private String time;

    
    private int productId;

    
    private int userId;

    
    private ResponseGeneralMsg responseGeneralMsg;

    
    private ProductGeneralParamsMsg productGeneralParamsMsg;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ResponseGeneralMsg getResponseGeneralMsg() {
        return responseGeneralMsg;
    }

    public void setResponseGeneralMsg(ResponseGeneralMsg responseGeneralMsg) {
        this.responseGeneralMsg = responseGeneralMsg;
    }

    public ProductGeneralParamsMsg getProductGeneralParamsMsg() {
        return productGeneralParamsMsg;
    }

    public void setProductGeneralParamsMsg(ProductGeneralParamsMsg productGeneralParamsMsg) {
        this.productGeneralParamsMsg = productGeneralParamsMsg;
    }
}
