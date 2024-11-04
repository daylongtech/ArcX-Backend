package avatar.module.product.instruct;

import avatar.entity.product.instruct.InstructPresentEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class InstructPresentDao {
    private static final InstructPresentDao instance = new InstructPresentDao();
    public static final InstructPresentDao getInstance(){
        return instance;
    }

    /**

     */
    public String loadByName(String name){
        String instruct = loadCache(name);
        if(instruct==null){
            
            InstructPresentEntity entity = loadDbByName(name);
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
        Object obj = GameData.getCache().get(ProductPrefixMsg.INSTRUCT_PRESENT+"_"+name);
        if(obj!=null){
            return obj.toString();
        }
        return null;
    }

    /**

     */
    private void setCache(String name, String instruct){
        GameData.getCache().set(ProductPrefixMsg.INSTRUCT_PRESENT+"_"+name, instruct);
    }

    //=========================db===========================

    /**

     */
    private InstructPresentEntity loadDbByName(String name) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        String sql = SqlUtil.getSql("instruct_present", paramsMap).toString();
        return GameData.getDB().get(InstructPresentEntity.class, sql, new Object[]{});
    }

}
