package ca.weblite.objc.mappers;

import ca.weblite.objc.TypeMapping;

/**
 * <p>StructureMapping class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class StructureMapping implements TypeMapping {
    /**
     * Singleton instance.
     */
    public static final StructureMapping INSTANCE = new StructureMapping();

    private StructureMapping() { }
    
    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        return cVar;
    }

    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        return jVar;
    }
    
}
