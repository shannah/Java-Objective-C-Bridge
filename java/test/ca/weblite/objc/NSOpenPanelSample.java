/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import ca.weblite.objc.annotations.Msg;
import static ca.weblite.objc.RuntimeUtils.*;

/**
 *
 * @author shannah
 */
public class NSOpenPanelSample extends NSObject {
    
    public NSOpenPanelSample(){
        super();
        init("NSObject");
    }
    @Msg(selector="panelSelectionDidChange:", signature="v@:@")
    public void panelSelectionDidChange(Proxy sender){
        System.out.println("---------In panel selection did change---------");
    }
    
    
    @Msg(selector="start", signature="v@:")
    public void start(){
        Proxy openPanel = getClient().sendProxy("NSOpenPanel", "openPanel");
        openPanel.send("setDelegate:", this);
        int result =  openPanel.sendInt("runModal");
        if ( result == 1 ){
            // File was selected
            // Use the -[URLs] message on NSOpen panel to get an NSArray
            // of the selected files
            Proxy selectedUrls = openPanel.sendProxy("URLs");
            System.out.println("The following urls were selected :"+selectedUrls);
        } else {
            // File was not selected.
            System.out.println("No file was selected");
        }
        
    }
    
    public static void main(String[] args){
        NSOpenPanelSample sample = new NSOpenPanelSample();
        
        // Any interaction with the GUI must happen on the main thread
        // for cocoa, so we'll use NSObject's performSelectorOnMainThread:
        // message to 
        sample.send("performSelectorOnMainThread:withObject:waitUntilDone:", sel("start"), sample, true);
    }
    
    
}
