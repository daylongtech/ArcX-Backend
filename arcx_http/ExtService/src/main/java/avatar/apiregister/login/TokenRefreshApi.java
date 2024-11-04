package avatar.apiregister.login;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.LoginHttpCmdName;
import avatar.net.session.Session;
import avatar.service.login.LoginDealService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**

 */
@Service
public class TokenRefreshApi extends SystemEventHttpHandler<Session> {
    protected TokenRefreshApi() {
        super(LoginHttpCmdName.TOKEN_REFRESH);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        
        LoginDealService.refreshToken(map, session);
    }
}
