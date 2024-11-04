package avatar.util.basic;

import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.enumMsg.basic.system.ServerTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.basic.systemConfig.SystemConfigDao;

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
    public static String loadDomainName(){
        
        String name = ConfigMsg.localHttpName;
        if(!CheckUtil.isTestEnv()){
            name = ConfigMsg.onlineHttpName;
        }
        return name;
    }

    /**

     */
    public static boolean isSystemMaintain(){
        
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return entity.getSystemMaintain()== YesOrNoEnum.YES.getCode();
    }
}
