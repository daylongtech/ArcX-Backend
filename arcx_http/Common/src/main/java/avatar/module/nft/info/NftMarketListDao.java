package avatar.module.nft.info;

import avatar.global.enumMsg.basic.nft.NftStatusEnum;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**

 */
public class NftMarketListDao {
    private static final NftMarketListDao instance = new NftMarketListDao();
    public static final NftMarketListDao getInstance(){
        return instance;
    }

    /**

     */
    public List<String> loadMsg(){
        List<String> list = loadCache();
        if(list==null){
            list = loadDbList();
            
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**

     */
    private List<String> loadCache(){
        return (List<String>)
                GameData.getCache().get(NftPrefixMsg.NFT_MARKET_LIST);
    }

    /**

     */
    private void setCache(List<String> list){
        GameData.getCache().set(NftPrefixMsg.NFT_MARKET_LIST, list);
    }

    /**

     */
    public void removeCache(){
        GameData.getCache().removeCache(NftPrefixMsg.NFT_MARKET_LIST);
    }

    //=========================db===========================

    /**

     */
    private List<String> loadDbList() {
        String sql = "select nft_code from sell_gold_machine_msg where status=? order by sale_num ";
        List<String> list = GameData.getDB().listString(sql, new Object[]{NftStatusEnum.LISTING.getCode()});
        return StrUtil.strRetList(list);
    }
}
