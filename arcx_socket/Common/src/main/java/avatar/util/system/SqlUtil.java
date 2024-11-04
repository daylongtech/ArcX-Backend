package avatar.util.system;

import avatar.util.GameData;

import java.util.List;
import java.util.Map;

/**

 */
public class SqlUtil {
    /**

     */
    public static StringBuilder totalNumParams(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, String> likeParamsMap, List<Integer> userIdList){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(likeParamsMap.keySet().size()>0){
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0) {
            if (userIdList!=null && userIdList.size()>0) {
                whereNum += 1;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select count(id) from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            if(userIdList!=null && userIdList.size()>0){
                sb.append(andStr(addWhereNum));
                sb.append(" user_id in (").append(StrUtil.listToStr(userIdList)).append(") ");
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder totalNumParams(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, Integer> intParamsMap, Map<String, String> likeParamsMap, Map<String, String> equalsParamsMap){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0) {
            if (intParamsMap.keySet().size() > 0) {
                for (String key : intParamsMap.keySet()) {
                    if (intParamsMap.get(key)!=0) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (likeParamsMap.keySet().size() > 0) {
                for (String key : likeParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (equalsParamsMap.keySet().size() > 0) {
                for (String key : equalsParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select count(id) from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                    addWhereNum += 1;
                }
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder totalNumParams(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, Integer> intParamsMap, Map<String, String> likeParamsMap,
            Map<String, String> equalsParamsMap, String startTime, String endTime){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0) {
            if (intParamsMap.keySet().size() > 0) {
                for (String key : intParamsMap.keySet()) {
                    if (intParamsMap.get(key)!=0) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (likeParamsMap.keySet().size() > 0) {
                for (String key : likeParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (equalsParamsMap.keySet().size() > 0) {
                for (String key : equalsParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0 && !StrUtil.checkEmpty(startTime)) {
           whereNum += 1;
        }
        if(whereNum==0 && !StrUtil.checkEmpty(endTime)) {
            whereNum += 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select count(id) from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                    addWhereNum += 1;
                }
            }
            
            if(!StrUtil.checkEmpty(startTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time >= '").append(startTime).append("' ");
                addWhereNum += 1;
            }
            
            if(!StrUtil.checkEmpty(endTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time <= '").append(endTime).append("' ");
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder totalNumParams(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, String> likeParamsMap, String startTime, String endTime){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0) {
            if (likeParamsMap.keySet().size() > 0) {
                for (String key : likeParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0 && !StrUtil.checkEmpty(startTime)) {
            whereNum += 1;
        }
        if(whereNum==0 && !StrUtil.checkEmpty(endTime)) {
            whereNum += 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select count(id) from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            if(!StrUtil.checkEmpty(startTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time >= '").append(startTime).append("' ");
                addWhereNum += 1;
            }
            
            if(!StrUtil.checkEmpty(endTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time <= '").append(endTime).append("' ");
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder idList(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, Integer> intParamsMap, Map<String, String> likeParamsMap,
            Map<String, String> equalsParamsMap, List<String> orderList){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))){
                    whereNum += 1;
                    break;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select id from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                    addWhereNum += 1;
                }
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder idList(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, Integer> intParamsMap, Map<String, String> likeParamsMap,
            Map<String, String> equalsParamsMap, String startTime, String endTime, List<String> orderList){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0 && !StrUtil.checkEmpty(startTime)){
            whereNum += 1;
        }
        if(whereNum==0 && !StrUtil.checkEmpty(endTime)){
            whereNum += 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select id from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                    addWhereNum += 1;
                }
            }
            
            if(!StrUtil.checkEmpty(startTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time >= '").append(startTime).append("' ");
                addWhereNum += 1;
            }
            
            if(!StrUtil.checkEmpty(endTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time <= '").append(endTime).append("' ");
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder idList(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, String> likeParamsMap, String startTime, String endTime, List<String> orderList){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0 && !StrUtil.checkEmpty(startTime)){
            whereNum += 1;
        }
        if(whereNum==0 && !StrUtil.checkEmpty(endTime)){
            whereNum += 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select id from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            if(!StrUtil.checkEmpty(startTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time >= '").append(startTime).append("' ");
                addWhereNum += 1;
            }
            
            if(!StrUtil.checkEmpty(endTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time <= '").append(endTime).append("' ");
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder idList(String tableName, Map<String, Integer> statusParamsMap,
            Map<String, String> likeParamsMap, List<Integer> userIdList, List<String> orderList){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            if(userIdList!=null && userIdList.size()>0){
                whereNum += 1;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select id from ")
                .append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            if(userIdList!=null && userIdList.size()>0){
                sb.append(andStr(addWhereNum));
                sb.append(" user_id in (").append(StrUtil.listToStr(userIdList)).append(") ");
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder totalByIdList(List<Integer> idList, String tableName, List<String> orderList){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(tableName).append(" where id in (").append(StrUtil.listToStr(idList)).append(") ");
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuilder pageMsg(int pageNum, int pageSize){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" limit ").append((pageNum - 1) * pageSize).append(",").append(pageSize);
        return sqlBuilder;
    }

    /**

     */
    public static StringBuffer loadList(String tableName, Map<String,Integer> statusParamsMap, Map<String,Integer> intParamsMap,
            Map<String, String> likeParamsMap, Map<String, String> equalsParamsMap, List<String> orderList) {
        int whereNum = 0;
        int addWhereNum = 0;
        if (statusParamsMap.keySet().size() > 0) {
            for (String key : statusParamsMap.keySet()) {
                if (statusParamsMap.get(key)!=-1) {
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0) {
            if (intParamsMap.keySet().size() > 0) {
                for (String key : intParamsMap.keySet()) {
                    if (intParamsMap.get(key) > 0) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (likeParamsMap.keySet().size() > 0) {
                for (String key : likeParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (equalsParamsMap.keySet().size() > 0) {
                for (String key : equalsParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        
        sb.append("select * from ").append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)>0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(intParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                    addWhereNum += 1;
                }
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer loadList(String tableName, List<String> orderList) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ").append(tableName);
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer loadList(String tableName, Map<String, Object> equalsParamsMap, List<String> orderList) {
        int addWhereNum = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ").append(tableName);
        
        if(equalsParamsMap.keySet().size()>0){
            sb.append(" where ");
            for(String key : equalsParamsMap.keySet()){
                sb.append(andStr(addWhereNum));
                sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                addWhereNum += 1;
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer getSql(String tableName, Map<String, Object> equalsParamsMap) {
        int addWhereNum = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ").append(tableName);
        
        if(equalsParamsMap.keySet().size()>0){
            sb.append(" where ");
            for(String key : equalsParamsMap.keySet()){
                sb.append(andStr(addWhereNum));
                sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                addWhereNum += 1;
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer getSql(String tableName, Map<String,Integer> statusParamsMap,
            Map<String,Integer> intParamsMap, Map<String, String> likeParamsMap, Map<String, String> equalsParamsMap) {
        int whereNum = 0;
        int addWhereNum = 0;
        if (statusParamsMap.keySet().size() > 0) {
            for (String key : statusParamsMap.keySet()) {
                if (statusParamsMap.get(key)!=-1) {
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0) {
            if (intParamsMap.keySet().size() > 0) {
                for (String key : intParamsMap.keySet()) {
                    if (intParamsMap.get(key) > 0) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (likeParamsMap.keySet().size() > 0) {
                for (String key : likeParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0) {
            if (equalsParamsMap.keySet().size() > 0) {
                for (String key : equalsParamsMap.keySet()) {
                    if (!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ").append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)>0) {
                    if(addWhereNum>0){
                        sb.append(" and ");
                    }
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like '%").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            for(String key : equalsParamsMap.keySet()){
                if(!StrUtil.checkEmpty(equalsParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("='").append(equalsParamsMap.get(key)).append("' ");
                    addWhereNum += 1;
                }
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer appointListSql(String tableName, String appointPar,
            Map<String, Integer> intParamsMap, Map<String, String> likeParamsMap, List<String> orderList){
        int whereNum = 0;
        int addWhereNum = 0;
        if(intParamsMap.keySet().size()>0){
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            if(likeParamsMap.keySet().size()>0){
                for(String key : likeParamsMap.keySet()){
                    if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select ").append(appointPar).append(" from ").append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like %'").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer appointListSql(String tableName, String appointPar,
            Map<String, Integer> statusParamsMap, Map<String, String> likeParamsMap, String startTime, String endTime){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            if(likeParamsMap.keySet().size()>0){
                for(String key : likeParamsMap.keySet()){
                    if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        if(whereNum==0 && !StrUtil.checkEmpty(startTime)){
            whereNum += 1;
        }
        if(whereNum==0 && !StrUtil.checkEmpty(endTime)){
            whereNum += 1;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select ").append(appointPar).append(" from ").append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like %'").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
            
            if(!StrUtil.checkEmpty(startTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time >= '").append(startTime).append("' ");
                addWhereNum += 1;
            }
            
            if(!StrUtil.checkEmpty(endTime)){
                sb.append(andStr(addWhereNum));
                sb.append(" create_time <= '").append(endTime).append("' ");
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer appointListSql(String tableName, String appointPar,
            Map<String, Integer> statusParamsMap, Map<String, String> likeParamsMap){
        int whereNum = 0;
        int addWhereNum = 0;
        if(statusParamsMap.keySet().size()>0){
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1){
                    whereNum += 1;
                    break;
                }
            }
        }
        if(whereNum==0){
            if(likeParamsMap.keySet().size()>0){
                for(String key : likeParamsMap.keySet()){
                    if(!StrUtil.checkEmpty(likeParamsMap.get(key))){
                        whereNum += 1;
                        break;
                    }
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select ").append(appointPar).append(" from ").append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : statusParamsMap.keySet()){
                if(statusParamsMap.get(key)!=-1) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(statusParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
            for(String key : likeParamsMap.keySet()){
                if(!StrUtil.checkEmpty(likeParamsMap.get(key))) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append(" like %'").append(likeParamsMap.get(key)).append("%' ");
                    addWhereNum += 1;
                }
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer appointListSql(String tableName, String appointPar,
            Map<String, Integer> intParamsMap, List<String> orderList){
        int whereNum = 0;
        int addWhereNum = 0;
        if(intParamsMap.keySet().size()>0){
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0){
                    whereNum += 1;
                    break;
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select ").append(appointPar).append(" from ").append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
        }
        
        if(orderList.size()>0){
            sb.append(" order by ");
            for(int i=0;i<orderList.size();i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(orderList.get(i));
            }
        }
        return sb;
    }

    /**

     */
    public static StringBuffer delSql(String tableName, Map<String, Integer> intParamsMap){
        int whereNum = 0;
        int addWhereNum = 0;
        if(intParamsMap.keySet().size()>0){
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0){
                    whereNum += 1;
                    break;
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("delete from ").append(tableName);
        
        if(whereNum>0){
            sb.append(" where ");
            for(String key : intParamsMap.keySet()){
                if(intParamsMap.get(key)!=0) {
                    sb.append(andStr(addWhereNum));
                    sb.append(key).append("=").append(intParamsMap.get(key));
                    addWhereNum += 1;
                }
            }
        }
        return sb;
    }

    /**

     */
    private static StringBuilder andStr(int whereNum){
        StringBuilder sb = new StringBuilder();
        if(whereNum>0){
            sb.append(" and ");
        }
        return sb;
    }

    /**

     */
    public static void resetAutoId(String tableName) {
        String sql = "alter table "+tableName+" AUTO_INCREMENT 1";
        GameData.getDB().update(sql, new Object[]{});
    }

    /**

     */
    private static StringBuilder whereAndStr(int whereNum){
        StringBuilder sb = new StringBuilder();
        if(whereNum==0){
            sb.append(" where ");
        }else{
            sb.append(" and ");
        }
        return sb;
    }

    /**

     */
    public static void equalsParamStr(StringBuilder sqlBuilder, String columnName, Object value, int whereNum) {
        sqlBuilder.append(whereAndStr(whereNum));
        sqlBuilder.append(columnName).append("='").append(value).append("'");
    }

    /**

     */
    public static void startTimeParamStr(StringBuilder sqlBuilder, String columnName, Object value, int whereNum) {
        sqlBuilder.append(whereAndStr(whereNum));
        sqlBuilder.append(columnName).append(">='").append(value).append("'");
    }

    /**

     */
    public static void listParamStr(StringBuilder sqlBuilder, String columnName, List<Integer> list, int whereNum) {
        sqlBuilder.append(whereAndStr(whereNum));
        sqlBuilder.append(columnName).append(" in (").append(StrUtil.listToStr(list)).append(") ");
    }

}
