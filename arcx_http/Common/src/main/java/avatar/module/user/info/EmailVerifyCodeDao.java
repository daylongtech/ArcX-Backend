package avatar.module.user.info;

import avatar.data.user.info.EmailVerifyCodeMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.login.LoginUtil;

/**

 */
public class EmailVerifyCodeDao {
    private static final EmailVerifyCodeDao instance = new EmailVerifyCodeDao();
    public static final EmailVerifyCodeDao getInstance(){
        return instance;
    }

    /**

     */
    public EmailVerifyCodeMsg loadMsg(String email){
        
        EmailVerifyCodeMsg msg = loadCache(email);
        if(msg==null){
            
            msg = LoginUtil.initEmailVerifyCodeMsg(email);
            
            setCache(email, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**

     */
    private EmailVerifyCodeMsg loadCache(String email){
        return (EmailVerifyCodeMsg) GameData.getCache().get(UserPrefixMsg.EMAIL_VERIFY_CODE+"_"+email);
    }

    /**

     */
    public void setCache(String email, EmailVerifyCodeMsg msg){
        GameData.getCache().set(UserPrefixMsg.EMAIL_VERIFY_CODE+"_"+email, msg);
    }


}
