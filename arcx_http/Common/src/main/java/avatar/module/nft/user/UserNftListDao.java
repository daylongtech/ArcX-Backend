package avatar.module.nft.user;

import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class UserNftListDao {
    private static final UserNftListDao instance = new UserNftListDao();
    public static final UserNftListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<String> loadMsg(int userId){
        List<String> list = loadCache(userId);
        if(list==null){
            list = loadDbList(userId);
            
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<String> loadCache(int userId){
        return (List<String>)
                GameData.getCache().get(NftPrefixMsg.USER_NFT_LIST+"_"+userId);
    }

    /**

     */
    private void setCache(int userId, List<String> list){
        GameData.getCache().set(NftPrefixMsg.USER_NFT_LIST+"_"+userId, list);
    }

    /**

     */
    public void removeCache(int userId){
        GameData.getCache().removeCache(NftPrefixMsg.USER_NFT_LIST+"_"+userId);
    }

    //=========================db===========================

    /**

     */
    private List<String> loadDbList(int userId) {
        String sql = "select nft_code from sell_gold_machine_msg where user_id=? order by status desc,lv desc,create_time ";
        List<String> list = GameData.getDB().listString(sql, new Object[]{userId});
        return StrUtil.strRetList(list);
    }
}
