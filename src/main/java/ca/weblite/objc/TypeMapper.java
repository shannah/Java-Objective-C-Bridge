package ca.weblite.objc;

import ca.weblite.objc.mappers.NSObjectMapping;
import ca.weblite.objc.mappers.PointerMapping;
import ca.weblite.objc.mappers.ScalarMapping;
import ca.weblite.objc.mappers.StringMapping;
import ca.weblite.objc.mappers.StructureMapping;

/**
 *
 * Maps Objective-C types to Java types.   This provides automatic conversion
 * to message inputs and outputs (unless coercion is disabled in the message
 * request).  In many cases, it just passes the values straight through (e.g.
 * primitive types.  Notably, Java Strings are mapped to NSStrings
 * if the signature of the argument context is an NSString, and NSObjects
 * are mapped as Proxy wrapper objects.
 *
 * @author shannah
 * @version $Id: $Id
 * @since 1.1
 */
public class TypeMapper implements TypeMapping {
    /**
     * Singleton instance of the TypeMapper
     */
    public static final TypeMapper INSTANCE = new TypeMapper();
    
    /**
     * Obtains the singleton instance of the TypeMapper, i.e. {@link #INSTANCE}.
     *
     * @return singleton {@code TypeMapper} object.
     */
    public static TypeMapper getInstance(){
        return INSTANCE;
    }
    
    private TypeMapper() { }
    
    /**
     * Maps signatures to the corresponding TypeMapping object.  Signatures
     * are <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encodings</a>.
     */
    private static TypeMapping getMapping(char typeChar) {
        switch (typeChar) {
            case 'c': case 'C':
            case 'i': case 'I':
            case 's': case 'S':
            case 'f': case 'd':
            case 'l': case 'L':
            case 'q': case 'Q':
            case 'b': case 'B':
            case '[': case ':': case '?': case '#': case 'v':
                return ScalarMapping.INSTANCE;
            case '*':
                return StringMapping.INSTANCE;
            case '^':
                return PointerMapping.INSTANCE;
            case '@':
                return NSObjectMapping.INSTANCE;
            case '{':
                return StructureMapping.INSTANCE;
            default:
                throw new IllegalArgumentException("Unknown type: " + typeChar);
        }
    }
    
    /**
     * {@inheritDoc}
     *
     * Converts a C variable to the corresponding Java type based on the
     * specified signature.  By default, this will map scalars straight
     * across without change.  Strings are mapped to NSStrings.
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
        
        char typeChar = signature.charAt(0);
        TypeMapping mapping = getMapping(typeChar);
        return mapping.cToJ(cVar, signature, root);
    }

    /**
     * {@inheritDoc}
     *
     * Converts a Java variable to the corresponding C type based on the
     * specified signature.  By default, this will map scalars straight
     * across without change.  Strings are mapped to NSStrings.
     *
     * <h4>Example Usage</h4>
     * <p>The following is a modified snippet from the NSObject class that shows
     * (roughly) how the jToC method is used to take the output of a Java method
     * and set the return value in an NSInvocation object to a corresponding
     * C type.:</p>
     * <script src="https://gist.github.com/3970051.js?file=Sample.java"></script>
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
        
        char typeChar = signature.charAt(0);
        TypeMapping mapping = getMapping(typeChar);
        return mapping.jToC(jVar, signature, root);
    }
    
}
