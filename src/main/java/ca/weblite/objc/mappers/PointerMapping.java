package ca.weblite.objc.mappers;

import com.sun.jna.Pointer;

import ca.weblite.objc.TypeMapping;

/**
 * <p>PointerMapping class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class PointerMapping implements TypeMapping {
    /**
     * Singleton instance.
     */
    public static final PointerMapping INSTANCE = new PointerMapping();

    private PointerMapping() { }

    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        if ( Pointer.class.isInstance(cVar)) return cVar;
        return new Pointer((long) cVar);
    }

    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        // After some difficult deliberation I've decided that it is 
        // better to require a Pointer or long to be passed in places
        // where Objective-C expects a pointer.
        return jVar;
    }
    
}
