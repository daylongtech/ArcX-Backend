package avatar.event;

/**

 */
public class CommonNotifyConfigEvent extends InternalEvent{
    public static final String type = "CommonNotifyConfigEvent";
    public CommonNotifyConfigEvent(String accessToken) {
        super(accessToken);
    }
}
