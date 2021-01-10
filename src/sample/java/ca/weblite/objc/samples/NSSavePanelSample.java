package ca.weblite.objc;

import ca.weblite.objc.annotations.Msg;
import static ca.weblite.objc.RuntimeUtils.*;

/**
 *
 * @author shannah
 */
public class NSSavePanelSample extends NSObject {
    
    public NSSavePanelSample(){
        super();
        init("NSObject");
    }
    @Msg(selector="panelSelectionDidChange:", signature="v@:@")
    public void panelSelectionDidChange(Proxy sender){
        System.out.println("---------In panel selection did change---------");
    }
    
    
    @Msg(selector="start", signature="v@:")
    public void start(){
        Proxy savePanel = getClient().sendProxy("NSSavePanel", "savePanel");
        savePanel.send("setDelegate:", this);
        savePanel.send("runModal");
        
    }
    
    public static void main(String[] args){
        NSSavePanelSample sample = new NSSavePanelSample();
        
        // Any interaction with the GUI must happen on the main thread
        // for cocoa, so we'll use NSObject's performSelectorOnMainThread:
        // message to 
        sample.send("performSelectorOnMainThread:withObject:waitUntilDone:", sel("start"), sample, true);
    }
    
    
}
