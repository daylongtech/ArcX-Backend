package avatar.service.system;

import avatar.event.CommonNotifyConfigEvent;
import avatar.event.ListenInternalEvent;
import avatar.net.session.Session;
import avatar.util.GameData;

/**

 */
public class HandleNotifyConfigService {

    @ListenInternalEvent(CommonNotifyConfigEvent.type)
    public void handle(CommonNotifyConfigEvent event){
        String accessToken = event.getAccessToken();
        Session session = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
        if(session == null){
            return;
        }
    }
}
