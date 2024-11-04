package avatar.apiregister.user.knapsack;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.UserHttpCmdName;
import avatar.net.session.Session;
import avatar.service.user.UserInfoService;
import avatar.service.user.UserKnapsackService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**

 */
@Service
public class PropertyKnapsackApi extends SystemEventHttpHandler<Session> {
    protected PropertyKnapsackApi() {
        super(UserHttpCmdName.PROPERTY_KNAPSACK);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        
        UserKnapsackService.propertyKnapsack(map, session);
    }
}
