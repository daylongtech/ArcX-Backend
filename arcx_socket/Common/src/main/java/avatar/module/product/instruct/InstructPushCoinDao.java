package avatar.module.product.instruct;

import avatar.entity.product.instruct.InstructPushCoinEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class InstructPushCoinDao {
    private static final InstructPushCoinDao instance = new InstructPushCoinDao();
    public static final InstructPushCoinDao getInstance(){
        return instance;
    }

    /**

     */
    public String loadByName(String name){
        String instruct = loadCache(name);
        if(instruct==null){
            
            InstructPushCoinEntity entity = loadDbByName(name);
            if(entity!=null){
                instruct = entity.getInstruct();
                setCache(name, instruct);
            }
        }
        return instruct;
    }

    //=========================cache===========================

    /**

     */
    private String loadCache(String name){
        Object obj = GameData.getCache().get(ProductPrefixMsg.INSTRUCT_PUSH_COIN+"_"+name);
        if(obj!=null){
            return obj.toString();
        }
        return null;
    }

    /**

     */
    private void setCache(String name, String instruct){
        GameData.getCache().set(ProductPrefixMsg.INSTRUCT_PUSH_COIN+"_"+name, instruct);
    }

    //=========================db===========================

    /**

     */
    private InstructPushCoinEntity loadDbByName(String name) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        String sql = SqlUtil.getSql("instruct_push_coin", paramsMap).toString();
        return GameData.getDB().get(InstructPushCoinEntity.class, sql, new Object[]{});
    }

}
