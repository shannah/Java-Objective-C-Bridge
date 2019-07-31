/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import com.sun.jna.Pointer;

/**
 * An interface for an object that has an Objective-C peer.
 *
 * @author shannah
 * @version $Id: $Id
 */
public interface Peerable {
    /**
     * <p>getPeer.</p>
     *
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer getPeer();
    /**
     * <p>setPeer.</p>
     *
     * @param peer a {@link com.sun.jna.Pointer} object.
     */
    public void setPeer(Pointer peer);
}
