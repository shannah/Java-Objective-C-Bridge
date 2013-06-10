/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.mappers;

import ca.weblite.objc.TypeMapping;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class PointerMapping implements TypeMapping {
    
    Map<String,TypeMapping> mappers = new HashMap<String, TypeMapping>();
    
    public PointerMapping(){
        
        
        
    }

    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        if ( Pointer.class.isInstance(cVar)) return cVar;
        return new Pointer((Long)cVar);
    }

    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        // After some difficult deliberation I've decided that it is 
        // better to require a Pointer or long to be passed in places
        // where Objective-C expects a pointer.
        return jVar;
    }
    
}
