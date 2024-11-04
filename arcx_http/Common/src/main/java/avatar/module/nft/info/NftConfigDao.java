package avatar.module.nft.info;

import avatar.entity.nft.NftConfigEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;

/**

 */
public class NftConfigDao {
    private static final NftConfigDao instance = new NftConfigDao();
    public static final NftConfigDao getInstance() {
        return instance;
    }

    /**

     */
    public NftConfigEntity loadMsg() {
        
        NftConfigEntity entity = loadCache();
        if (entity==null) {
            
            entity = loadDbMsg();
            
            if (entity != null) {
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**

     */
    private NftConfigEntity loadCache() {
        return (NftConfigEntity)
                GameData.getCache().get(NftPrefixMsg.NFT_CONFIG);
    }

    /**

     *
     */
    private void setCache(NftConfigEntity entity) {
        GameData.getCache().set(NftPrefixMsg.NFT_CONFIG, entity);
    }

    //=========================db===========================

    /**

     */
    private NftConfigEntity loadDbMsg() {
        String sql = "select * from nft_config";
        return GameData.getDB().get(NftConfigEntity.class, sql, new Object[]{});
    }

}
