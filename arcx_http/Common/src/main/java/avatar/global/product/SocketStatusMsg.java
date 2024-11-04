package avatar.global.product;

import java.io.Serializable;

/**

 */
public class SocketStatusMsg implements Serializable {
    
    private boolean isNormal;

    
    private long time;

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
