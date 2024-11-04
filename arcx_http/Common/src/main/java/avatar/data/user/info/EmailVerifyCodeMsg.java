package avatar.data.user.info;

import java.io.Serializable;

/**

 */
public class EmailVerifyCodeMsg implements Serializable {
    
    private String email;

    
    private String verifyCode;

    
    private long sendTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
