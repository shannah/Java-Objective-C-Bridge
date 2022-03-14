package ca.weblite.objc;

import org.junit.jupiter.api.Test;

/**
 *
 * @author shannah
 */
public class FiveArgTest {

    /**
     * https://github.com/shannah/Java-Objective-C-Bridge/issues/44
     */
    @Test
    public void testFiveArgs44() {
        Client.getInstance().sendProxy("NSURL", "URLByResolvingBookmarkData:options:relativeToURL:bookmarkDataIsStale:error:", null, 1024, null, false, null);

    }

    
}
