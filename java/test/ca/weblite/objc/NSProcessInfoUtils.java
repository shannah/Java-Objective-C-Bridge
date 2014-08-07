import ca.weblite.objc.Client;
import ca.weblite.objc.Proxy;

/**
 * Created by IntelliJ IDEA.
 * User: remicartier (remi.cartier@gmail.com)
 * Date: 2014-08-06
 * Time: 8:13 PM
 */
public class NSProcessInfoUtils {

    private final static long NSActivityUserInitiated = (0x00FFFFFFL | (1L << 20));

    /**
     * To ensure Mac OS X doesn't slow down your app because of App Nap, call this method 
     * @param reason the reason for allowing the app to work at full speed
     * @return the activity id as a Proxy object
     */
    public static Proxy beginActivityWithOptions(String reason) {

        Client c = Client.getInstance();
        Proxy processInfo = c.sendProxy("NSProcessInfo", "processInfo");
        return processInfo.sendProxy("beginActivityWithOptions:reason:", NSActivityUserInitiated, reason);
    }

    /**
     * When the activity is finished, to re-enable app napping call this method 
     * @param activity previously returned by beginActivityWithOptions()
     */
    public static void endActivity(Proxy activity) {

        if (activity != null) {
            Client c = Client.getInstance();
            Proxy processInfo = c.sendProxy("NSProcessInfo", "processInfo");
            processInfo.send("endActivity:", activity);
        }
    }
}
