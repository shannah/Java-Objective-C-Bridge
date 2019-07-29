/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import ca.weblite.objc.mappers.NSObjectMapping;
import ca.weblite.objc.mappers.PointerMapping;
import ca.weblite.objc.mappers.ScalarMapping;
import ca.weblite.objc.mappers.StringMapping;
import ca.weblite.objc.mappers.StructureMapping;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Maps Objective-C types to Java types.   This provides automatic conversion
 * to message inputs and outputs (unless coercion is disabled in the message
 * request).  In many cases, it just passes the values straight through (e.g. 
 * primitive types.  Notably, Java Strings are mapped to NSStrings 
 * if the signature of the argument context is an NSString, and NSObjects
 * are mapped as Proxy wrapper objects.
 * @author shannah
 */
public class TypeMapper implements TypeMapping {
    
    /**
     * Singleton instance to the TypeMapper
     */
    static TypeMapper instance;
    
    /**
     * Obtains the singleton instance of the TypeMapper
     * @return 
     */
    public static TypeMapper getInstance(){
        if ( instance == null ){
            instance = new TypeMapper();
        }
        return instance;
    }
    
    
    /**
     * Maps signatures to the corresponding TypeMapping object.  Signatures
     * are <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encodings</a>.
     */
    Map<String, TypeMapping> mappers = new HashMap<String,TypeMapping>();
    
    public TypeMapper(){
        init();
    }
    
    
    private void init(){
        
        
        
        
        addMapping(new ScalarMapping(), "cCiIsSfdlLqQB[:b?#v".split(""));
        addMapping(new StringMapping(), "*".split(""));
        addMapping(new PointerMapping(), "^");
        addMapping(new NSObjectMapping(), "@");
        addMapping(new StructureMapping(), "{");
        
        
        
        
        
        
        
    }
    
    /**
     * Adds a TypeMapping that is meant to handle one or more signatures.
     * @param mapping The TypeMapping object meant to handle conversions for
     * the given signatures.
     * @param signatures One or more signatures following <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encodings</a>.
     * @return Self for chaining.
     */
    public TypeMapper addMapping(TypeMapping mapping, String... signatures){
        
        for ( int i=0; i<signatures.length; i++){
            mappers.put(signatures[i], mapping);
        }
        return this;
    }
    
    /**
     * Converts a C variable to the corresponding Java type based on the 
     * specified signature.  By default, this will map scalars straight
     * across without change.  Strings are mapped to NSStrings.
     * @param cVar The C variable that is to be converted.
     * @param signature The signature that provides the context of what
     * the variable is expected to be in the Objective-C runtime.  This 
     * follows <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encoding</a> conventions.
     * 
     * @param root This should be the root TypeMapping.  Usually you just pass
     * TypeMapper.getInstance() here.
     * @return The converted Java object.
     */
    @Override
    public Object cToJ(Object cVar, String signature, TypeMapping root) {
        String prefixes = "rnNoORV";
        int offset = 0;
        while ( prefixes.indexOf(signature.charAt(offset)) != -1 ){
            offset++;
            if ( offset > signature.length()-1 ){
                break;
            }
        }
        if ( offset > 0 ){
            signature = signature.substring(offset);
        }
        
        String firstChar = signature.substring(0,1);
        TypeMapping mapping = mappers.get(firstChar);
        if ( mapping == null ){
            // We couldn't find a mapper for this type
            throw new RuntimeException("No mapper registered for type "+firstChar);
        } else {
            return mapping.cToJ(cVar, signature, root);
        }
    }

    /**
     * Converts a Java variable to the corresponding C type based on the 
     * specified signature.  By default, this will map scalars straight
     * across without change.  Strings are mapped to NSStrings.
     * 
     * <h5>Example Usage</h5>
     * <p>The following is a modified snippet from the NSObject class that shows
     * (roughly) how the jToC method is used to take the output of a Java method
     * and set the return value in an NSInvocation object to a corresponding
     * C type.:</p>
     * <script src="https://gist.github.com/3970051.js?file=Sample.java"></script>
     * 
     * @param jVar The C variable that is to be converted.
     * @param signature The signature that provides the context of what
     * the variable is expected to be in the Objective-C runtime.  This 
     * follows <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encoding</a> conventions.
     * 
     * @param root This should be the root TypeMapping.  Usually you just pass
     * TypeMapper.getInstance() here.
     * @return The converted C value.
     */
    @Override
    public Object jToC(Object jVar, String signature, TypeMapping root) {
        String prefixes = "rnNoORV";
        int offset = 0;
        while ( prefixes.indexOf(signature.charAt(offset)) != -1 ){
            offset++;
            if ( offset > signature.length()-1 ){
                break;
            }
        }
        if ( offset > 0 ){
            signature = signature.substring(offset);
        }
        
        String firstChar = signature.substring(0,1);
        TypeMapping mapping = mappers.get(firstChar);
        if ( mapping == null ){
            // We couldn't find a mapper for this type
            throw new RuntimeException("No mapper registered for type "+firstChar);
        } else {
            return mapping.jToC(jVar, signature, root);
        }
    }
    
}
