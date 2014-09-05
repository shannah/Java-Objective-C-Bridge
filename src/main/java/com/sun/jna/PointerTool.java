/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.jna;

/**
 *
 * @author shannah
 */
public class PointerTool {
    public static long getPeer(Pointer ptr){
        return ptr.peer;
    }
}
