package avatar.module.basic.systemMsg;

import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**

 */
public class PropertyMsgDao {
    private static final PropertyMsgDao instance = new PropertyMsgDao();
    public static final PropertyMsgDao getInstance() {
        return instance;
    }

    /**

     */
    public PropertyMsgEntity loadMsg(int propertyType) {
        
        PropertyMsgEntity entity = loadCache(propertyType);
        if (entity==null) {
            
            entity = loadDbMsg(propertyType);
            
            setCache(propertyType, entity);
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private PropertyMsgEntity loadCache(int propertyType) {
        return (PropertyMsgEntity) GameData.getCache().get(PrefixMsg.PROPERTY_MSG+"_"+propertyType);
    }

    /**

     *
     */
    private void setCache(int propertyType, PropertyMsgEntity entity) {
        GameData.getCache().set(PrefixMsg.PROPERTY_MSG+"_"+propertyType, entity);
    }

    //=========================db===========================

    /**

     */
    private PropertyMsgEntity loadDbMsg(int propertyType) {
        String sql = "select * from property_msg where property_type=?";
        return GameData.getDB().get(PropertyMsgEntity.class, sql, new Object[]{propertyType});
    }

}
