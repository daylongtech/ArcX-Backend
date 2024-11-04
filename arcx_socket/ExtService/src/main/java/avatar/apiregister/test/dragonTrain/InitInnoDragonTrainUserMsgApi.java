package avatar.apiregister.test.dragonTrain;

import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.facade.SystemEventHttpHandler;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.TestHttpCmdName;
import avatar.module.activity.dragonTrain.user.DragonTrainUserMsgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.basic.general.CheckUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**

 */
@Service
public class InitInnoDragonTrainUserMsgApi extends SystemEventHttpHandler<Session> {
    protected InitInnoDragonTrainUserMsgApi() {
        super(TestHttpCmdName.INIT_INNO_DRAGON_TRAIN_USER_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");
        int num = ParamsUtil.intParmas(map, "num");
        if(CheckUtil.isTestEnv()) {
            
            if(UserInfoDao.getInstance().loadByUserId(userId)!=null){
                if(num<0 || num>6){

                }else {
                    
                    DragonTrainUserMsgEntity entity = DragonTrainUserMsgDao.getInstance().loadByUserId(userId);
                    entity.setDragonNum(num);
                    boolean flag = DragonTrainUserMsgDao.getInstance().update(entity);
                    if(!flag){

                    }
                }
            }else{

            }
        }
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
    }
}
