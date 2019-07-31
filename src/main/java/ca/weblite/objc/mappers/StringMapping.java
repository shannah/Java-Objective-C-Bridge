/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.mappers;

import ca.weblite.objc.TypeMapping;
import com.sun.jna.Pointer;

/**
 * <p>StringMapping class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class StringMapping implements TypeMapping{

    /** {@inheritDoc} */
    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        System.out.println("Mapping string from cVar");
        return new Pointer((Long)cVar).getString(0);
    }

    /** {@inheritDoc} */
    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        return jVar;
    }
    
}
