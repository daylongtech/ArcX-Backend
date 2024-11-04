package avatar.apiregister.user.opinion;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.UserHttpCmdName;
import avatar.net.session.Session;
import avatar.service.user.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**

 */
@Service
public class UserOpinionApi extends SystemEventHttpHandler<Session> {
    protected UserOpinionApi() {
        super(UserHttpCmdName.USER_OPINION);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        
        UserInfoService.userOpinion(map, session);
    }
}
