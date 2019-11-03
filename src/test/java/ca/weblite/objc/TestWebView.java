package ca.weblite.objc;

import static ca.weblite.objc.RuntimeUtils.*;
import ca.weblite.objc.annotations.Msg;
import com.sun.jna.Library;
import com.sun.jna.Native;

import javax.swing.JFrame;
/**
 *
 * @author shannah
 */
public class TestWebView extends NSObject {
    
    // JNA interface for webkit framework
    public static interface WebKit extends Library {
        public final static TestWebView.WebKit INSTANCE = (TestWebView.WebKit)Native.loadLibrary("WebKit", TestWebView.WebKit.class);
    }
    
    public TestWebView(){
        super("NSObject");
        
    }
    
    // Initialization method
    @Msg(selector="myInit", like="NSObject.finalize")
    public void myInit(){
        
        // Get objective-c client
        Client c = getClient();
        
        // Load the bundle code
        // Includes TestWindowController class
        Proxy bundle = c.sendProxy("NSBundle", "bundleWithPath:", "TestBundle.bundle");
        bundle.send("load");
        
        // Initialize  TestWindowController object used to work with the TestWindowController nib file.
        Proxy window = c.sendProxy("TestWindowController","alloc");
        window = window.sendProxy("initWithWindowNibPath:owner:", "TestBundle.bundle/Contents/Resources/TestWindowController.nib", window.getPeer());
        
        // Open the window
        window.send("showWindow:", window);
        
        // Get the webView in the window and load a URL in it
        Proxy webView = window.sendProxy("webView");
        webView.send("setMainFrameURL:", "http://www.google.com");
        
    }
    
    public void someFuncWithMultipleArgs(String arg1, int arg2){
        System.out.println("Arg 1 is "+arg1+"; arg 2 is "+arg2);
    }
    
    public static void main(String[] args){
        
        // Easy way to start event loop so app doesn't exit... start swing.
        new JFrame();
        
        // Load the WebKit framework
        Native.loadLibrary("WebKit", TestWebView.WebKit.class);
        
        
        final TestWebView v = new TestWebView();
        final String arg1 = "A first argument";
        final int arg2 = 100;
        
        (new NSObject("NSObject"){
            
            @Msg(selector="doBlock", like="NSObject.finalize")
            public void doBlock(){
                v.myInit();
                v.someFuncWithMultipleArgs(arg1, arg2);
            }
        }).send("performSelectorOnMainThread:withObject:waitUntilDone:", sel("doBlock"), null, 1);
        
        
        // Run on the main thread
        //v.send("performSelectorOnMainThread:withObject:waitUntilDone:", sel("myInit"), v, 1);
        
    }
}
