package avatar.data.product.innoMsg;

/**

 */
public class InnoProductMsg {
    private String alias;

    private int userId;

    private String userName;

    private String imgUrl;

    private int serverSideType;

    private int productMulti;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
    }

    public int getProductMulti() {
        return productMulti;
    }

    public void setProductMulti(int productMulti) {
        this.productMulti = productMulti;
    }
}
