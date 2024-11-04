package avatar.util.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**

 */
public class ListUtil {

    /**

     */
    public static void fillPageMsg(Map<String, Object> map, int dataSumNum, int pageNum, int pageSize){
        int dataNum = 0;
        if(pageNum<=(dataSumNum/pageSize)){
            dataNum = pageSize;
        }else{
            if(pageNum==(dataSumNum/pageSize+1)){
                dataNum = dataSumNum%pageSize;
            }
        }
        int pageSumNum = dataSumNum/pageSize;
        if(dataSumNum%pageSize>0){
            pageSumNum += 1;
        }
        map.put("dataSumNum", dataSumNum);
        map.put("dataNum", dataNum);
        map.put("pageNum", pageNum);
        map.put("pageSumNum", pageSumNum);
    }

    /**

     * @return
     */
    public static List getPageList(int pageNum, int pageSize, List dealList){
        if(dealList!=null) {
            int startNum = (pageNum - 1) * pageSize;
            int endNum = startNum + pageSize;
            List list = new ArrayList(dealList);
            int size = list == null ? 0 : list.size();
            
            List newList = new ArrayList<>();
            if (size > 0) {
                if (startNum < (size)) {
                    if (endNum <= (size - 1)) {
                        newList = (List) list.stream().skip(startNum).limit(pageSize).collect(Collectors.toList());
                    } else {
                        newList = (List) list.stream().skip(startNum).limit(size - startNum).collect(Collectors.toList());
                    }
                } else if (startNum == (size - 1)) {
                    newList.add(list.get(startNum));
                }
            }
            return newList;
        }else{
            return new ArrayList();
        }
    }
}
