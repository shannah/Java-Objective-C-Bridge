/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import com.sun.jna.Pointer;

/**
 * An interface for an object that has an Objective-C peer.
 * @author shannah
 */
public interface Peerable {
    public Pointer getPeer();
    public void setPeer(Pointer peer);
}
