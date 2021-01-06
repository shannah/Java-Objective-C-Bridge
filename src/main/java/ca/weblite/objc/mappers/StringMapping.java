package ca.weblite.objc.mappers;

import com.sun.jna.Pointer;

import ca.weblite.objc.TypeMapping;

/**
 * <p>StringMapping class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class StringMapping implements TypeMapping {
    /**
     * Singleton instance.
     */
    public static final StringMapping INSTANCE = new StringMapping();
    
    private StringMapping() { }
    
    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        return new Pointer((Long)cVar).getString(0);
    }

    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        return jVar;
    }
    
}
