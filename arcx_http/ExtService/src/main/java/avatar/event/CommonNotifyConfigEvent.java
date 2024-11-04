package avatar.event;

/**

 */
public class CommonNotifyConfigEvent extends InternalEvent{
    public static final String type = "CommonNotifyConfigEvent";
    public CommonNotifyConfigEvent(int userId) {
        super(userId);
    }
}
