/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.jna;

import com.sun.jna.Pointer;

/**
 * <p>PointerTool class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class PointerTool {
    /**
     * <p>getPeer.</p>
     *
     * @param ptr a {@link com.sun.jna.Pointer} object.
     * @return a long.
     */
    public static long getPeer(Pointer ptr){
        return Pointer.nativeValue(ptr);
    }
}
