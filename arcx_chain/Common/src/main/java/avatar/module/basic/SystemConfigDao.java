package avatar.module.basic;

import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**

 */
public class SystemConfigDao {
    private static final SystemConfigDao instance = new SystemConfigDao();
    public static final SystemConfigDao getInstance() {
        return instance;
    }

    /**

     */
    public SystemConfigEntity loadMsg() {
        
        SystemConfigEntity entity = loadCache();
        if (entity==null) {
            
            entity = loadDbMsg();
            
            if (entity != null) {
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private SystemConfigEntity loadCache() {
        return (SystemConfigEntity)
                GameData.getCache().get(PrefixMsg.SYSTEM_CONFIG);
    }

    /**

     *
     */
    private void setCache(SystemConfigEntity entity) {
        GameData.getCache().set(PrefixMsg.SYSTEM_CONFIG, entity);
    }

    //=========================db===========================

    /**

     */
    private SystemConfigEntity loadDbMsg() {
        String sql = SqlUtil.getSql("system_config", new HashMap<>(), new HashMap<>(),
                new HashMap<>(), new HashMap<>()).toString();
        return GameData.getDB().get(SystemConfigEntity.class, sql, new Object[]{});
    }

}
