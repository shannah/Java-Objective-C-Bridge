package ca.weblite.objc;

import ca.weblite.objc.Client;
import ca.weblite.objc.NSObject;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.annotations.Msg;
import static ca.weblite.objc.RuntimeUtils.*;

/**
 *
 * @author shannah
 */
public class LoadNibSample extends NSObject {
    
    Proxy window;
    
    public LoadNibSample(){
        super();
        init("NSObject");
        
    }
    
    @Msg(selector="setWindow:", like="NSSavePanel.setTitle:")
    public void setWindow(Proxy window){
        this.window = window;
    }
    
    @Msg(selector="window", like="NSObject.description")
    public Proxy window(){
        return this.window;
    }
    
    @Msg(selector="windowDidLoad", like="NSWindowController.windowDidLoad")
    public void windowDidLoad(){
        System.out.println("Window did load");
    }
    
    @Msg(selector="applicationDidFinishLaunching:", like="NSWindow.setTitle:")
    public void applicationDidFinishLaunching(Proxy notification){
        System.out.println("App did finish launching");
    }
    
    @Msg(selector="doMyAction:", like="NSWindow.setTitle")
    public void doMyAction(Proxy sender){
        System.out.println("Action was performed");
    }
    
    @Msg(selector="startApplication", like="NSObject.finalize")
    public void startApplication(){
        Client c = getClient();
        Proxy app = c.chain("NSApplication", "sharedApplication");
       
        Proxy topLevelObjects = c.chain("NSMutableArray", "array");
        
        
        Proxy filesOwner = c.chain(
                "NSDictionary", 
                    "dictionaryWithObjectsAndKeys:", 
                        this.getPeer(),  
                        str("NSOwner"), 
                        topLevelObjects.getPeer(),
                        str("NSTopLevelObjects"),
                        null
        );

        
        app.send("setDelegate:", this);
        
        
        
        long res = (Long)c.send("NSBundle", "loadNibFile:externalNameTable:withZone:", "MainMenu.nib", filesOwner.getPeer(), null);
        int numTopLevelObjects = ((Long)topLevelObjects.send("count")).intValue();
        
        Proxy mainWindow = null;
        
        for ( int i=0; i<numTopLevelObjects; i++){
            Proxy obj = (Proxy)topLevelObjects.send("objectAtIndex:", i);
            if ( (Long)obj.send("isKindOfClass:", cls("NSWindow")) > 0 ){
                mainWindow = obj;
            } else {
                
                obj.dispose(false);
            }
        }
        
        //Proxy windowController = c.chain("NSWindowController", "alloc").chain("initWithWindow:", mainWindow.getPeer());
        //System.out.println("Window loaded? "+windowController.send("isWindowLoaded"));
        //System.out.println("Window owner: "+windowController.send("owner"));
        //System.out.println("Bundle loaded "+res);
        //System.out.println("Num top level objects "+topLevelObjects.send("count"));
        //System.out.println("Main window "+app.send("mainWindow"));
        
        
        
        app.send("run");
        
        
        
        
    }
    public static void main(String[] args){
        //NativeLibrary library = NativeLibrary.getInstance("AppKit");
        //Pointer addr = library.getGlobalVariableAddress("NSNibOwner");
        
        //System.out.println("Top level objects :"+str(addr.getLong(0)));System.exit(0);
        LoadNibSample app = new LoadNibSample();
        app.send("performSelectorOnMainThread:withObject:waitUntilDone:", sel("startApplication"), app, 1);
    }
}
