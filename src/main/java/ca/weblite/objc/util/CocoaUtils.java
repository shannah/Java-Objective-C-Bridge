/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.util;

import ca.weblite.objc.NSObject;
import static ca.weblite.objc.RuntimeUtils.sel;
import ca.weblite.objc.annotations.Msg;

/**
 * <p>CocoaUtils class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class CocoaUtils {
    
    
    /**
     * <p>dispatch_async.</p>
     *
     * @param r a {@link java.lang.Runnable} object.
     */
    public static void dispatch_async(final Runnable r){
        (new NSObject("NSObject"){
            @Msg(selector="run", like="NSObject.finalize")
            public void run(){
                r.run();
            }
        }).send("performSelectorOnMainThread:withObject:waitUntilDone:", sel("run"), null, false);
    }
    
    
    /**
     * <p>dispatch_sync.</p>
     *
     * @param r a {@link java.lang.Runnable} object.
     */
    public static void dispatch_sync(final Runnable r){
        (new NSObject("NSObject"){
            @Msg(selector="run", like="NSObject.finalize")
            public void run(){
                r.run();
            }
        }).send("performSelectorOnMainThread:withObject:waitUntilDone:", sel("run"), null, true);
    }
    
}
