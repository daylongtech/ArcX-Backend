package avatar;

/**

 */
public class ExtServer {
    public static void main(String[] args){
        new GameConfig("hallServer", args).init(new ExtInit() , new InitExtDBConfing()).start();
    }

}
