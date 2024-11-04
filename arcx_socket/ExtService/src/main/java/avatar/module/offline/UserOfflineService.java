package avatar.module.offline;

import avatar.event.InternalEventDispatcher;
import avatar.event.ListenInternalEvent;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.module.user.info.UserInfoDao;
import avatar.net.event.UserOfflineEvent;
import avatar.util.LogUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.product.ProductSocketUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserUtil;
import org.springframework.stereotype.Service;

/**

 */
@Service
public class UserOfflineService {
    private static final UserOfflineService instance = new UserOfflineService();

    public static UserOfflineService getInstance(){
        return instance;
    }

    public void init(){
        InternalEventDispatcher.getInstance().addEventListener(UserOfflineEvent.type , this.getClass());
    }

    @ListenInternalEvent(UserOfflineEvent.type)
    public void handlerUserOffline(UserOfflineEvent userOfflineEvent){
        String accessToken = userOfflineEvent.getAccessToken();
        if(!StrUtil.checkEmpty(accessToken) && !accessToken.equals(ConfigMsg.touristAccessToken)) {
            int userId = UserUtil.loadUserIdByToken(accessToken);
            if(userId>0) {
                

                if (UserInfoDao.getInstance().loadByUserId(userId) != null) {
                    
                    SendWebsocketMsgUtil.closeSocket(userId);
                    
                    ProductDealUtil.socketOffDeal(userId);
                    
                    ProductSocketUtil.dealOffLineSession(userId);
                }
            }
        }
    }
}
