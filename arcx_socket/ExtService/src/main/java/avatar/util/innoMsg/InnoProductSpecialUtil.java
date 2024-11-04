package avatar.util.innoMsg;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.product.InnoParamsUtil;

/**

 */
public class InnoProductSpecialUtil {
    /**

     */
    public static void specialAward(int userId, int productId, int awardType){
        
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        String host = productInfoEntity.getIp();
        int port = Integer.parseInt(productInfoEntity.getPort());
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        int uId = SyncInnoConnectUtil.loadHostId(host, port+"");
        
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_OPERATE_MSG, uId,
                InnoParamsUtil.initProductOperateMsg(productId, productInfoEntity, userId, awardType));
    }
}
