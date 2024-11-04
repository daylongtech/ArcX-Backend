package avatar.module.basic.ip;

import avatar.entity.basic.ip.IpAddressEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.IcretAddressUtil;

/**

 */
public class IpAddressDao {
    private static final IpAddressDao instance = new IpAddressDao();
    public static final IpAddressDao getInstance(){
        return instance;
    }

    /**

     */
    public IpAddressEntity loadByIp(String ip) {
        
        IpAddressEntity entity = loadCache(ip);
        if(entity==null){
            
            entity = loadDbByIp(ip);
            if(entity!=null){
                
                setCache(ip, entity);
            }else{
                
                String address = IcretAddressUtil.loadAddressByIp(ip);
                if(!StrUtil.checkEmpty(address)){
                    
                    entity = insert(ip, address);
                    if(entity!=null){
                        
                        setCache(ip, entity);
                    }
                }
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private IpAddressEntity loadCache(String ip){
        return (IpAddressEntity) GameData.getCache().get(PrefixMsg.IP_ADDRESS+"_"+ip);
    }

    /**

     */
    private void setCache(String ip, IpAddressEntity entity){
        GameData.getCache().set(PrefixMsg.IP_ADDRESS+"_"+ip, entity);
    }

    //=========================db===========================

    /**

     */
    private IpAddressEntity loadDbByIp(String ip) {
        String sql = "select * from ip_address where ip=?";
        return GameData.getDB().get(IpAddressEntity.class, sql, new Object[]{ip});
    }

    /**

     */
    private IpAddressEntity insert(String ip, String address) {
        
        IpAddressEntity entity = IcretAddressUtil.initIpAddress(ip, address);
        if(entity==null){
            return null;
        }
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            return entity;
        }else{
            return null;
        }
    }
}
