package ca.weblite.objc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * A JNA wrapper around the objective-c runtime.  This contains all of the functions
 * needed to interact with the runtime (e.g. send messages, etc..).
 *
 * <h2>Sample Usage</h2>
 * <script src="https://gist.github.com/3974488.js?file=SampleLowLevelAPI.java"></script>
 *
 * @author shannah
 * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ObjCRuntimeRef/Reference/reference.html">Objective-C Runtime Reference</a>
 * @version $Id: $Id
 * @since 1.1
 */
public interface Runtime extends Library {
    /** Constant <code>INSTANCE</code> */
    public static Runtime INSTANCE = (Runtime)Native.loadLibrary("objc.A", Runtime.class);

    public long acceptNSRange(Object o);
    
    /**
     * <p>objc_lookUpClass.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_lookUpClass(String name);
    /**
     * <p>class_getName.</p>
     *
     * @param id a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String class_getName(Pointer id);
    /**
     * <p>class_getProperty.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_getProperty(Pointer cls, String name);
    /**
     * <p>class_getSuperclass.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_getSuperclass(Pointer cls);
    /**
     * <p>class_getVersion.</p>
     *
     * @param theClass a {@link com.sun.jna.Pointer} object.
     * @return a int.
     */
    public int class_getVersion(Pointer theClass);
    /**
     * <p>class_getWeakIvarLayout.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String class_getWeakIvarLayout(Pointer cls);
    /**
     * <p>class_isMetaClass.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @return a boolean.
     */
    public boolean class_isMetaClass(Pointer cls);
    /**
     * <p>class_getInstanceSize.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @return a int.
     */
    public int class_getInstanceSize(Pointer cls);
    /**
     * <p>class_getInstanceVariable.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_getInstanceVariable(Pointer cls, String name);
    /**
     * <p>class_getInstanceMethod.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param aSelector a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_getInstanceMethod(Pointer cls, Pointer aSelector);
    /**
     * <p>class_getClassMethod.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param aSelector a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_getClassMethod(Pointer cls, Pointer aSelector);
    
    /**
     * <p>class_getIvarLayout.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String class_getIvarLayout(Pointer cls);
    /**
     * <p>class_getMethodImplementation.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param name a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_getMethodImplementation(Pointer cls, Pointer name);
    /**
     * <p>class_getMethodImplementation_stret.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param name a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_getMethodImplementation_stret(Pointer cls, Pointer name);
    /**
     * <p>class_replaceMethod.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param name a {@link com.sun.jna.Pointer} object.
     * @param imp a {@link com.sun.jna.Pointer} object.
     * @param types a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_replaceMethod(Pointer cls, Pointer name, Pointer imp, String types);
    /**
     * <p>class_respondsToSelector.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param sel a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_respondsToSelector(Pointer cls, Pointer sel);
    /**
     * <p>class_setIvarLayout.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param layout a {@link java.lang.String} object.
     */
    public void class_setIvarLayout(Pointer cls, String layout);
    /**
     * <p>class_setSuperclass.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param newSuper a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer class_setSuperclass(Pointer cls, Pointer newSuper);
    /**
     * <p>class_setVersion.</p>
     *
     * @param theClass a {@link com.sun.jna.Pointer} object.
     * @param version a int.
     */
    public void class_setVersion(Pointer theClass, int version);
    /**
     * <p>class_setWeakIvarLayout.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param layout a {@link java.lang.String} object.
     */
    public void class_setWeakIvarLayout(Pointer cls, String layout);
    /**
     * <p>ivar_getName.</p>
     *
     * @param ivar a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String ivar_getName(Pointer ivar);
    /**
     * <p>ivar_getOffset.</p>
     *
     * @param ivar a {@link com.sun.jna.Pointer} object.
     * @return a long.
     */
    public long ivar_getOffset(Pointer ivar);
    /**
     * <p>ivar_getTypeEncoding.</p>
     *
     * @param ivar a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String ivar_getTypeEncoding(Pointer ivar);
    /**
     * <p>method_copyArgumentType.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @param index a int.
     * @return a {@link java.lang.String} object.
     */
    public String method_copyArgumentType(Pointer method, int index);
    /**
     * <p>method_copyReturnType.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String method_copyReturnType(Pointer method);
    /**
     * <p>method_exchangeImplementations.</p>
     *
     * @param m1 a {@link com.sun.jna.Pointer} object.
     * @param m2 a {@link com.sun.jna.Pointer} object.
     */
    public void method_exchangeImplementations(Pointer m1, Pointer m2);
    /**
     * <p>method_getArgumentType.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @param index a int.
     * @param dst a {@link com.sun.jna.Pointer} object.
     * @param dst_len a long.
     */
    public void method_getArgumentType(Pointer method, int index, Pointer dst, long dst_len);
    /**
     * <p>method_getImplementation.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer method_getImplementation(Pointer method);
    /**
     * <p>method_getName.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer method_getName(Pointer method);
    /**
     * <p>method_getNumberOfArguments.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @return a int.
     */
    public int method_getNumberOfArguments(Pointer method);
    /**
     * <p>method_getReturnType.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @param dst a {@link com.sun.jna.Pointer} object.
     * @param dst_len a long.
     */
    public void method_getReturnType(Pointer method, Pointer dst, long dst_len);
    /**
     * <p>method_getTypeEncoding.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String method_getTypeEncoding(Pointer method);
    /**
     * <p>method_setImplementation.</p>
     *
     * @param method a {@link com.sun.jna.Pointer} object.
     * @param imp a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer method_setImplementation(Pointer method, Pointer imp);
    /**
     * <p>objc_allocateClassPair.</p>
     *
     * @param superclass a {@link com.sun.jna.Pointer} object.
     * @param name a {@link java.lang.String} object.
     * @param extraBytes a long.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_allocateClassPair(Pointer superclass, String name, long extraBytes);
    /**
     * <p>objc_copyProtocolList.</p>
     *
     * @param outCount a {@link com.sun.jna.Pointer} object.
     * @return an array of {@link com.sun.jna.Pointer} objects.
     */
    public Pointer[] objc_copyProtocolList(Pointer outCount);
    /**
     * <p>objc_getAssociatedObject.</p>
     *
     * @param object a {@link com.sun.jna.Pointer} object.
     * @param key a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_getAssociatedObject(Pointer object, String key);
    /**
     * <p>objc_getClass.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_getClass(String name);
    /**
     * <p>objc_getClassList.</p>
     *
     * @param buffer a {@link com.sun.jna.Pointer} object.
     * @param bufferlen a int.
     * @return a int.
     */
    public int objc_getClassList(Pointer buffer, int bufferlen);
    /**
     * <p>objc_getFutureClass.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_getFutureClass(String name);
    /**
     * <p>objc_getMetaClass.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_getMetaClass(String name);
    /**
     * <p>objc_getProtocol.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_getProtocol(String name);
    /**
     * <p>objc_getRequiredClass.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer objc_getRequiredClass(String name);
    /**
     * <p>objc_msgSend.</p>
     *
     * @param theReceiver a {@link com.sun.jna.Pointer} object.
     * @param theSelector a {@link com.sun.jna.Pointer} object.
     * @param arguments a {@link java.lang.Object} object.
     * @return a long.
     */
    public long objc_msgSend(Pointer theReceiver, Pointer theSelector);
    public long objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg);

    public long objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2);

    public long objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3);
    public long objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4);
    public long objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4, Object arg5);
    public long objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6);
    public long objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7);



    /**
     * <p>objc_msgSendSuper.</p>
     *
     * @param superClassStruct a {@link com.sun.jna.Pointer} object.
     * @param op a {@link com.sun.jna.Pointer} object.
     * @param arguments a {@link java.lang.Object} object.
     * @return a long.
     */
    public long objc_msgSendSuper(Pointer superClassStruct, Pointer op, Object... arguments);
    /**
     * <p>objc_msgSendSuper_stret.</p>
     *
     * @param superClassStruct a {@link com.sun.jna.Pointer} object.
     * @param op a {@link com.sun.jna.Pointer} object.
     * @param arguments a {@link java.lang.Object} object.
     * @return a long.
     */
    public long objc_msgSendSuper_stret(Pointer superClassStruct, Pointer op);
    /**
     * <p>objc_msgSend_fpret.</p>
     *
     * @param self a {@link com.sun.jna.Pointer} object.
     * @param op a {@link com.sun.jna.Pointer} object.
     * @param arguments a {@link java.lang.Object} object.
     * @return a double.
     */
    public double objc_msgSend_fpret(Pointer self, Pointer op);
    public double objc_msgSend_fpret(Pointer self, Pointer op, Object arg);
    public double objc_msgSend_fpret(Pointer self, Pointer op, Object arg, Object arg2);
    public double objc_msgSend_fpret(Pointer self, Pointer op, Object arg, Object arg2, Object arg3);
    public double objc_msgSend_fpret(Pointer self, Pointer op, Object arg, Object arg2, Object arg3, Object arg4);
    public double objc_msgSend_fpret(Pointer self, Pointer op, Object arg, Object arg2, Object arg3, Object arg4, Object arg5);
    public double objc_msgSend_fpret(Pointer self, Pointer op, Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6);
    public double objc_msgSend_fpret(Pointer self, Pointer op, Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7);
    /**
     * <p>objc_msgSend_stret.</p>
     *
     * @param stretAddr a {@link com.sun.jna.Pointer} object.
     * @param theReceiver a {@link com.sun.jna.Pointer} object.
     * @param theSelector a {@link com.sun.jna.Pointer} object.
     * @param arguments a {@link java.lang.Object} object.
     */
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector);
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object arg);
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object arg, Object arg2);
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object arg, Object arg2, Object arg3);
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object arg, Object arg2, Object arg3, Object arg4);
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object arg, Object arg2, Object arg3, Object arg4, Object arg5);
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6);
    public void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7);

    /**
     * <p>objc_registerClassPair.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     */
    public void objc_registerClassPair(Pointer cls);
    /**
     * <p>objc_removeAssociatedObjects.</p>
     *
     * @param object a {@link com.sun.jna.Pointer} object.
     */
    public void objc_removeAssociatedObjects(Pointer object);
    /**
     * <p>objc_setAssociatedObject.</p>
     *
     * @param object a {@link com.sun.jna.Pointer} object.
     * @param key a {@link com.sun.jna.Pointer} object.
     * @param value a {@link com.sun.jna.Pointer} object.
     * @param policy a {@link com.sun.jna.Pointer} object.
     */
    public void objc_setAssociatedObject(Pointer object, Pointer key, Pointer value, Pointer policy);
    /**
     * <p>objc_setFutureClass.</p>
     *
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @param name a {@link java.lang.String} object.
     */
    public void objc_setFutureClass(Pointer cls, String name);
    /**
     * <p>object_copy.</p>
     *
     * @param obj a {@link com.sun.jna.Pointer} object.
     * @param size a long.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_copy(Pointer obj, long size);
    /**
     * <p>object_dispose.</p>
     *
     * @param obj a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_dispose(Pointer obj);
    /**
     * <p>object_getClass.</p>
     *
     * @param object a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_getClass(Pointer object);
    /**
     * <p>object_getClassName.</p>
     *
     * @param obj a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String object_getClassName(Pointer obj);
    /**
     * <p>object_getIndexedIvars.</p>
     *
     * @param obj a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_getIndexedIvars(Pointer obj);
    /**
     * <p>object_getInstanceVariable.</p>
     *
     * @param obj a {@link com.sun.jna.Pointer} object.
     * @param name a {@link java.lang.String} object.
     * @param outValue a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_getInstanceVariable(Pointer obj, String name, Pointer outValue);
    /**
     * <p>object_getIvar.</p>
     *
     * @param object a {@link com.sun.jna.Pointer} object.
     * @param ivar a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_getIvar(Pointer object, Pointer ivar);
    /**
     * <p>object_setClass.</p>
     *
     * @param object a {@link com.sun.jna.Pointer} object.
     * @param cls a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_setClass(Pointer object, Pointer cls);
    /**
     * <p>object_setInstanceVariable.</p>
     *
     * @param obj a {@link com.sun.jna.Pointer} object.
     * @param name a {@link java.lang.String} object.
     * @param value a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer object_setInstanceVariable(Pointer obj, String name, Pointer value);
    /**
     * <p>object_setIvar.</p>
     *
     * @param object a {@link com.sun.jna.Pointer} object.
     * @param ivar a {@link com.sun.jna.Pointer} object.
     * @param value a {@link com.sun.jna.Pointer} object.
     */
    public void object_setIvar(Pointer object, Pointer ivar, Pointer value);
    /**
     * <p>property_getAttributes.</p>
     *
     * @param property a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String property_getAttributes(Pointer property);
    /**
     * <p>protocol_conformsToProtocol.</p>
     *
     * @param proto a {@link com.sun.jna.Pointer} object.
     * @param other a {@link com.sun.jna.Pointer} object.
     * @return a boolean.
     */
    public boolean protocol_conformsToProtocol(Pointer proto, Pointer other);
    /**
     * <p>protocol_copyMethodDescriptionList.</p>
     *
     * @param protocol a {@link com.sun.jna.Pointer} object.
     * @param isRequiredMethod a boolean.
     * @param isInstanceMethod a boolean.
     * @param outCount a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Structure} object.
     */
    public Structure protocol_copyMethodDescriptionList(Pointer protocol, boolean isRequiredMethod, boolean isInstanceMethod, Pointer outCount);
    /**
     * <p>protocol_copyPropertyList.</p>
     *
     * @param proto a {@link com.sun.jna.Pointer} object.
     * @param outCount a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer protocol_copyPropertyList(Pointer proto, Pointer outCount);
    /**
     * <p>protocol_copyProtocolList.</p>
     *
     * @param proto a {@link com.sun.jna.Pointer} object.
     * @param outCount a {@link com.sun.jna.Pointer} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer protocol_copyProtocolList(Pointer proto, Pointer outCount);
    /**
     * <p>protocol_getMethodDescription.</p>
     *
     * @param proto a {@link com.sun.jna.Pointer} object.
     * @param aSel a {@link com.sun.jna.Pointer} object.
     * @param isRequiredMethod a boolean.
     * @param isInstanceMethod a boolean.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer protocol_getMethodDescription(Pointer proto, Pointer aSel, boolean isRequiredMethod, boolean isInstanceMethod);
    /**
     * <p>protocol_getName.</p>
     *
     * @param proto a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String protocol_getName(Pointer proto);
    /**
     * <p>protocol_getProperty.</p>
     *
     * @param proto a {@link com.sun.jna.Pointer} object.
     * @param name a {@link java.lang.String} object.
     * @param isRequiredProperty a boolean.
     * @param isInstanceProperty a boolean.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer protocol_getProperty(Pointer proto, String name, boolean isRequiredProperty, boolean isInstanceProperty);
    /**
     * <p>protocol_isEqual.</p>
     *
     * @param protocol a {@link com.sun.jna.Pointer} object.
     * @param other a {@link com.sun.jna.Pointer} object.
     * @return a boolean.
     */
    public boolean protocol_isEqual(Pointer protocol, Pointer other);
    /**
     * <p>sel_getName.</p>
     *
     * @param aSelector a {@link com.sun.jna.Pointer} object.
     * @return a {@link java.lang.String} object.
     */
    public String sel_getName(Pointer aSelector);
    
    /**
     * <p>sel_getUid.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer sel_getUid(String name);
    /**
     * <p>sel_isEqual.</p>
     *
     * @param lhs a {@link com.sun.jna.Pointer} object.
     * @param rhs a {@link com.sun.jna.Pointer} object.
     * @return a boolean.
     */
    public boolean sel_isEqual(Pointer lhs, Pointer rhs);
    /**
     * <p>sel_registerName.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link com.sun.jna.Pointer} object.
     */
    public Pointer sel_registerName(String name);
    
}
