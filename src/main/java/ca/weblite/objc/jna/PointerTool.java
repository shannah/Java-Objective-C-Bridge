/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.jna;

import com.sun.jna.Pointer;

/**
 *
 * @author shannah
 */
public class PointerTool {
    public static long getPeer(Pointer ptr){
        return Pointer.nativeValue(ptr);
    }
}
