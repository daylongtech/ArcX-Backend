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
        int userId = event.getUserId();
        Session session = GameData.getSessionManager().getSessionByUserId(userId);
        if(session == null){
            return;
        }
    }
}
