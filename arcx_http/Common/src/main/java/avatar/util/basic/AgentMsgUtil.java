package avatar.util.basic;

import avatar.data.basic.agent.AgentConnectMsg;
import avatar.entity.basic.agent.AgentAreaMsgEntity;
import avatar.entity.basic.agent.AgentDefaultMsgEntity;
import avatar.entity.basic.ip.IpAddressEntity;
import avatar.module.basic.agent.AgentAreaMsgDao;
import avatar.module.basic.agent.AgentConnectListDao;
import avatar.module.basic.agent.AgentDefaultMsgDao;
import avatar.module.basic.agent.AgentNationAreaMsgDao;
import avatar.module.basic.ip.IpAddressDao;
import avatar.util.LogUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.List;
import java.util.Map;

/**

 */
public class AgentMsgUtil {
    /**

     */
    public static void loadAgentWebsocketList(String userIp, List<AgentConnectMsg> agentWebsocketList,
            Map<String, Object> dataMap) {
        boolean specifiedFlag = false;
        String nation = "";
        String specifyHttp = "";
        if(!StrUtil.checkEmpty(userIp)) {
            IpAddressEntity ipAdressEntity = IpAddressDao.getInstance().loadByIp(userIp);
            String specifyIp = "";
            String specifyPort = "";
            if (ipAdressEntity != null && !StrUtil.checkEmpty(ipAdressEntity.getNation())) {
                nation = ipAdressEntity.getNation().trim();
                int areaId = loadNationArea(nation);
                if (areaId > 0) {
                    
                    AgentAreaMsgEntity areaMsgEntity = AgentAreaMsgDao.getInstance()
                            .loadByArea(areaId);
                    if (areaMsgEntity != null) {
                        specifiedFlag = true;
                        specifyIp = areaMsgEntity.getSocketIp();//socket ip
                        specifyPort = areaMsgEntity.getSocketPort();
                        specifyHttp = areaMsgEntity.getHttpDomain();
                    }
                }
            }
            
            if(specifiedFlag){
                agentWebsocketList.add(initAgentConnectMsg(specifyIp, specifyPort));
            }
        }
        
        List<AgentConnectMsg> areaList = AgentConnectListDao.getInstance().loadMsg();
        if(areaList.size()>0){
            areaList.forEach(msg->{
                if(!addConnectMsg(agentWebsocketList, msg.getIp(), msg.getPort())){
                    agentWebsocketList.add(initAgentConnectMsg(msg.getIp(), msg.getPort()));
                }
            });
        }
        
        if(StrUtil.checkEmpty(specifyHttp)){
            String defaultNation = AgentDefaultMsgDao.getInstance().loadMsg().getDefaultNation();
            if(!StrUtil.checkEmpty(defaultNation)){
                
                int areaId = loadNationArea(nation);
                if (areaId > 0) {
                    
                    AgentAreaMsgEntity areaMsgEntity = AgentAreaMsgDao.getInstance()
                            .loadByArea(areaId);
                    if(areaMsgEntity!=null){
                        
                        if(StrUtil.checkEmpty(specifyHttp)){
                            specifyHttp = areaMsgEntity.getHttpDomain();
                        }
                    }
                }
            }
        }
        dataMap.put("wsTbln", agentWebsocketList);
        dataMap.put("svDm", specifyHttp);


    }

    /**

     */
    private static int loadNationArea(String nation) {
        
        int areaId = AgentNationAreaMsgDao.getInstance().loadByNation(nation);
        if(areaId==0){
            
            String defaultNation = loadWebsocketDefaultNation();
            if(!StrUtil.checkEmpty(defaultNation)){
                
                areaId = AgentNationAreaMsgDao.getInstance().loadByNation(defaultNation);
                if(areaId>0) {

                            nation, defaultNation);
                }
            }
        }
        return areaId;
    }

    /**

     */
    private static AgentConnectMsg initAgentConnectMsg(String ip, String port) {
        AgentConnectMsg msg = new AgentConnectMsg();
        msg.setIp(ip);//ip
        msg.setPort(port);
        return msg;
    }

    /**

     */
    public static void initAgentConnectMsg(List<AgentAreaMsgEntity> list, List<AgentConnectMsg> retList) {
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                if(!addConnectMsg(retList, entity.getSocketIp(), entity.getSocketPort())){
                    AgentConnectMsg msg = new AgentConnectMsg();
                    msg.setIp(entity.getSocketIp());
                    msg.setPort(entity.getSocketPort());
                    retList.add(msg);
                }
            });
        }
    }

    /**

     */
    private static boolean addConnectMsg(List<AgentConnectMsg> list, String ip, String port) {
        boolean flag = false;
        if(list.size()>0){
            for(AgentConnectMsg msg : list ){
                if(msg.getIp().equals(ip) && msg.getPort().equals(port)){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**

     */
    private static String loadWebsocketDefaultNation() {
        AgentDefaultMsgEntity entity = AgentDefaultMsgDao.getInstance().loadMsg();
        return entity==null?"":entity.getDefaultNation();
    }
}
