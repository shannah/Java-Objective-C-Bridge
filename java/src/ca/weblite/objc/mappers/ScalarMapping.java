/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.mappers;

import ca.weblite.objc.TypeMapping;

/**
 *
 * @author shannah
 */
public class ScalarMapping implements TypeMapping {

    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        //System.out.println("C to J for signature "+signature);
        char firstChar = signature.charAt(0);
        if ( Long.class.isInstance(cVar) || long.class.isInstance(cVar)){
            long cObj = (Long)cVar;
            switch (firstChar){
                case 'i':
                case 'I':
                case 's':
                case 'S':
                    return new Long(cObj).intValue();
                case 'c':
                    return new Long(cObj).byteValue();
                case 'B':
                    return cObj > 0L ? true:false;
            }
        }
        
        return cVar;
    }

    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        return jVar;
    }
    
}
