package avatar.module.activity.sign;

import avatar.entity.activity.sign.info.WelfareSignAwardEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**

 */
public class WelfareSignAwardDao {
    private static final WelfareSignAwardDao instance = new WelfareSignAwardDao();
    public static final WelfareSignAwardDao getInstance(){
        return instance;
    }

    /**

     */
    public ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> loadAll() {
        
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = loadCache();
        if(map==null){
            
            List<WelfareSignAwardEntity> list = loadDbAll();
            if(list!=null && list.size()>0){
                
                map = setCache(list);
            }
        }
        return map;
    }

    /**

     */
    public List<Integer> loadAllDay() {
        List<Integer> list = new ArrayList<>();
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = loadAll();
        if(map!=null && map.size()>0){
            list.addAll(map.keySet());
        }
        Collections.sort(list);
        return list;
    }

    //=========================cache===========================

    /**

     */
    private ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> loadCache(){
        return (ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>>) GameData.getCache().get(ActivityPrefixMsg.WELFARE_SIGN_AWARD);
    }

    /**

     */
    private ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> setCache(List<WelfareSignAwardEntity> list){
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = new ConcurrentHashMap<>();
        list.forEach(entity->{
            int day = entity.getDay();
            List<WelfareSignAwardEntity> msgList = map.get(day);
            if(msgList==null){
                msgList = new ArrayList<>();
            }
            msgList.add(entity);
            map.put(day, msgList);
        });
        GameData.getCache().set(ActivityPrefixMsg.WELFARE_SIGN_AWARD, map);
        return map;
    }

    //=========================db===========================

    /**

     */
    private List<WelfareSignAwardEntity> loadDbAll() {
        String sql = "select * from welfare_sign_award order by day,sequence";
        return GameData.getDB().list(WelfareSignAwardEntity.class, sql, new Object[]{});
    }

}
