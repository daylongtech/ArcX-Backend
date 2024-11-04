package avatar.module.product.instruct;

import avatar.entity.product.instruct.InstructCatchDollEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class InstructCatchDollDao {
    private static final InstructCatchDollDao instance = new InstructCatchDollDao();
    public static final InstructCatchDollDao getInstance(){
        return instance;
    }

    /**

     */
    public String loadByName(String name){
        String instruct = loadCache(name);
        if(instruct==null){
            
            InstructCatchDollEntity entity = loadDbByName(name);
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
        Object obj = GameData.getCache().get(ProductPrefixMsg.INSTRUCT_CATCH_DOLL+"_"+name);
        if(obj!=null){
            return obj.toString();
        }
        return null;
    }

    /**

     */
    private void setCache(String name, String instruct){
        GameData.getCache().set(ProductPrefixMsg.INSTRUCT_CATCH_DOLL+"_"+name, instruct);
    }

    //=========================db===========================

    /**

     */
    private InstructCatchDollEntity loadDbByName(String name) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        String sql = SqlUtil.getSql("instruct_catch_doll", paramsMap).toString();
        return GameData.getDB().get(InstructCatchDollEntity.class, sql, new Object[]{});
    }

}
