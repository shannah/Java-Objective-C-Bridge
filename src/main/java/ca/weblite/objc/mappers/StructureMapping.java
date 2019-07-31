/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.mappers;

import ca.weblite.objc.TypeMapping;
import com.sun.jna.Structure;

/**
 * <p>StructureMapping class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 */
public class StructureMapping implements TypeMapping {

    /** {@inheritDoc} */
    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        return cVar;
    }

    /** {@inheritDoc} */
    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        return jVar;
    }
    
}
