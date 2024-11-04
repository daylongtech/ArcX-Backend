package avatar.util.basic.system;

import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.global.basicConfig.ConfigMsg;
import avatar.global.enumMsg.system.ServerTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.basic.SystemConfigDao;

/**

 */
public class CheckUtil {
    /**

     */
    public static boolean isTestEnv(){
        
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return entity!=null && entity.getServerType()!= ServerTypeEnum.ONLINE.getCode();
    }

    /**

     */
    public static boolean isSystemMaintain(){
        
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return entity.getSystemMaintain()== YesOrNoEnum.YES.getCode();
    }

    /**

     */
    public static String loadDomainName(){
        
        String name = ConfigMsg.localHttpName;
        if(!CheckUtil.isTestEnv()){
            name = ConfigMsg.onlineHttpName;
        }
        return name;
    }

}
