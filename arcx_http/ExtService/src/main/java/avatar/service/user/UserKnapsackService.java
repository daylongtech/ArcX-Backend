package avatar.service.user;

import avatar.data.user.balance.PropertyKnapsackMsg;
import avatar.module.basic.systemMsg.PropertyListDao;
import avatar.net.session.Session;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ListUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserPropertyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
public class UserKnapsackService {

    /**

     */
    public static void propertyKnapsack(Map<String, Object> map, Session session) {
        List<PropertyKnapsackMsg> retList = new ArrayList<>();
        
        int status = CheckParamsUtil.checkAccessTokenPage(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);
            
            List<Integer> propertyList = PropertyListDao.getInstance().loadMsg();
            
            List<Integer> loadList = ListUtil.getPageList(ParamsUtil.pageNum(map),
                    ParamsUtil.pageSize(map), propertyList);
            if(loadList.size()>0) {
                
                UserPropertyUtil.propertyKnapsack(userId, loadList, retList);
            }
        }
        
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
    }
}
