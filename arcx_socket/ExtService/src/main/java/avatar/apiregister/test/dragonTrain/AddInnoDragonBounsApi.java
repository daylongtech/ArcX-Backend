package avatar.apiregister.test.dragonTrain;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.TestHttpCmdName;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.info.UserInfoDao;
import avatar.net.session.Session;
import avatar.task.product.innoMsg.InnoProductDragonWinPrizeDealTask;
import avatar.util.LogUtil;
import avatar.util.basic.general.CheckUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.trigger.SchedulerSample;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**

 */
@Service
public class AddInnoDragonBounsApi extends SystemEventHttpHandler<Session> {
    protected AddInnoDragonBounsApi() {
        super(TestHttpCmdName.ADD_INNO_DRAGON_BONUS);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");
        int productId = ParamsUtil.intParmasNotNull(map, "productId");
        if(CheckUtil.isTestEnv()){
            
            if(UserInfoDao.getInstance().loadByUserId(userId)!=null){
                if(ProductInfoDao.getInstance().loadByProductId(productId)==null){

                }else {
                    SchedulerSample.delayed(100, new InnoProductDragonWinPrizeDealTask(
                            productId, userId));
                }
            }else{

            }
        }
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
    }
}
