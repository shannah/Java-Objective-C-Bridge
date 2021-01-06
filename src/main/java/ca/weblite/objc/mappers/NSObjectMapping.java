package ca.weblite.objc.mappers;

import com.sun.jna.Pointer;

import ca.weblite.objc.Peerable;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.Runtime;
import ca.weblite.objc.RuntimeUtils;
import ca.weblite.objc.TypeMapping;
import ca.weblite.objc.jna.PointerTool;

/**
 * <p>NSObjectMapping class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class NSObjectMapping implements TypeMapping{
    
    /** {@inheritDoc} */
    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        //System.out.println("Mapping NSObject to Java "+cVar+" sig: "+signature);
        Pointer cObj = Pointer.NULL;
        if ( Pointer.class.isInstance(cVar) ){
            cObj = (Pointer)cVar;
        } else if (long.class.isInstance(cVar) || Long.class.isInstance(cVar) ){
            cObj = new Pointer((Long)cVar);
        } else {
            return cVar;
        }
        if ( (Pointer.NULL == cObj) || (cVar == null) || (cObj == null) || (PointerTool.getPeer(cObj) == 0L ) ){
            //System.out.println("The java value will be null");
            return null;
        }
        String clsName = Runtime.INSTANCE.object_getClassName(cObj);
        boolean isString = false;
        if ( "NSString".equals(clsName) || "NSTaggedPointerString".equals(clsName) || "NSMutableString".equals(clsName) || "__NSCFString".equals(clsName)){
            isString = true;
        }
        
        
        ////System.out.println("Checking if object is a string "+isString+", "+clsName);
        if ( isString  ){
            return RuntimeUtils.str(cObj);
        }
        Object peer = RuntimeUtils.getJavaPeer(PointerTool.getPeer(cObj));
        if ( peer == null ){
            return Proxy.load(cObj);
        } else {
            return peer;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        if ( jVar == null ){
            return Pointer.NULL;
        }
        if ( String.class.isInstance(jVar)){
            //////System.out.println("Converting string ["+jVar+"] to string");
            return RuntimeUtils.str((String)jVar);
        }
        if ( Peerable.class.isInstance(jVar)){
            return ((Peerable)jVar).getPeer();
        } else if (jVar instanceof Pointer) {
            return jVar;
        } else {
            throw new IllegalArgumentException("Unsupported value: " + jVar);
        }
    }
    
}
