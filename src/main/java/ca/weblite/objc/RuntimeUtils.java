package ca.weblite.objc;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;

import ca.weblite.nativeutils.NativeUtils;

/**
 * A Java class with static methods that interact with the Objective-C runtime.
 * These methods wrap the *very* low level methods provided by the Runtime class
 * to provide a slightly higher level of abstraction.
 *
 * <p>It is helpful to perform a static import of the methods in this class if you
 * will be using the objc runtime library, as it contains many utility methods
 * to deal with Objective-C primitives.  E.g. the cls() method can return the
 * Pointer to a class given its name.  The clsName() returns the name of the
 * class specified by a Pointer(). </p>
 *
 * <p>There are also many variants of the msg() method for sendng messages to
 * objects in the Objective-C runtime.</p>
 *
 * @author shannah
 * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ObjCRuntimeRef/Reference/reference.html">Objective-C Runtime Reference</a>
 * @version $Id: $Id
 * @since 1.1
 */
public class RuntimeUtils {
    
    
    /**
     * Short reference to the runtime instance for interacting with Objective-C
     */
    private static final Runtime rt = Runtime.INSTANCE;
    
    
    private RuntimeUtils() {
        
    }
    
    static {
        String libraryPath = "/libjcocoa.dylib";
        try {
            NativeUtils.loadLibraryFromJar(libraryPath);
            init();
        } catch (IOException ioException) {
            throw new UncheckedIOException("Failed loading library " + libraryPath, ioException);
        }
    }
    
    /**
     * Returns a pointer to the class for specific class name.
     * <pre>
     * {@code
     * Pointer nsObject = cls("NSObject");
     * }</pre>
     *
     * @param name The name of the class to retrieve.
     * @return The pointer to the class structure.
     */
    public static Pointer cls(String name){
        return rt.objc_lookUpClass(name);
    }
    
    /**
     * Returns a pointer to a class given a Peerable object which wraps
     * the pointer.  Effectively this just calls peer.getPeer() and
     * returns its value.  A convenience method.
     *
     * @param peer The peer object that is wrapping the Objective-C class
     * object.
     * @return A Pointer to the Objective-C class.
     */
    public static Pointer cls(Peerable peer){
        return peer.getPeer();
    }
    
    /**
     * Returns the name of an objective C class specified by the given class pointer.
     *
     * @param cls The pointer to the class whose name we wish to retrieve.
     * @return The name of the class.
     * @see Runtime#class_getName(Pointer)
     */
    public static String clsName(Pointer cls){
        return rt.class_getName(cls);
    }
    
    /**
     * A wrapper for the clsName() method given a Peerable object that wraps
     * the class pointer.  This will return the class name.
     *
     * @param peer a {@link ca.weblite.objc.Peerable} object.
     * @see Runtime#class_getName(Pointer)
     * @return a {@link java.lang.String} object.
     */
    public static String clsName(Peerable peer){
        return clsName(peer.getPeer());
    }
    
    /**
     * Returns a pointer to the selector specified by the given selector name.
     *
     * @param name a {@link java.lang.String} object.
     * @return Pointer to an Objective-C message selector.
     * @see <a href="http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/objectivec/Chapters/ocSelectors.html">Objective-C Selectors Reference</a>
     */
    public static Pointer sel(String name){
        return rt.sel_getUid(name);
    }
    
    
    /**
     * Returns a pointer to the selector that is wrapped by a Peerable object.
     *
     * @param peer a {@link ca.weblite.objc.Peerable} object.
     * @return Pointer to a specified selector.
     */
    public static Pointer sel(Peerable peer){
        return peer.getPeer();
    }
    
    /**
     * Returns the name of a selector.
     *
     * @param sel Pointer to a selector.
     * @return The name of the selector.
     */
    public static String selName(Pointer sel){
        return rt.sel_getName(sel);
    }
    
    /**
     * Returns the name of a selector.
     *
     * @return The name of the selector.
     * @param peer a {@link ca.weblite.objc.Peerable} object.
     */
    public static String selName(Peerable peer){
        return selName(peer.getPeer());
    }
    
    /**
     * Sends a message to a specified class using the given selector.
     *
     * @param cls The name of the class.
     * @param msg The name of the selector.
     * @param args The arguments passed to the method.  These are passed raw
     * and will not be coerced.
     * @return The result of the message.  This may be a pointer, if the message
     * returns a pointer, or a value, if the method returns a BOOL, long, int,
     * short, byte, or char.  Note that you cannot use this method for
     * messages that return float and double values.  For these, you <em>must</em>
     * use msgDouble().  This is because doubles and floats are handled using
     * a different underlying runtime function (objc_msgSend_fpret). Alternatively
     * you can use the variation of msg() that takes coerceInput and coerceOutput
     * boolean values, as this will automatically choose the correct underlying
     * message function depending on the return type reported by the message
     * signature.
     * @see #msgDouble(String, String, Object...)
     */
    public static long msg(String cls, String msg, Object... args){
        return msg(cls(cls), msg, args);
    }
    
    /**
     * Sends a message to a specified class using the given selector.
     *
     * @param cls The name of the class.
     * @param msg The name of the selector.
     * @param args The arguments passed to the method.  These are passed raw
     * and will not be coerced.
     * @return The result of the message.  This may be a pointer, if the message
     * returns a pointer, or a value, if the method returns a BOOL, long, int,
     * short, byte, or char.  Note that you cannot use this method for
     * messages that return float and double values.  For these, you <em>must</em>
     * use msgDouble().  This is because doubles and floats are handled using
     * a different underlying runtime function (objc_msgSend_fpret). Alternatively
     * you can use the variation of msg() that takes coerceInput and coerceOutput
     * boolean values, as this will automatically choose the correct underlying
     * message function depending on the return type reported by the message
     * signature.
     * @see #msgDouble(String, Pointer, Object...)
     */
    public static long msg(String cls, Pointer msg, Object... args){
        return msg(cls(cls), msg, args);
    }
    
    
    /**
     * Sends a message to a specified class using the given selector.
     *
     * @param msg The name of the selector.
     * @param args The arguments passed to the method.  These are passed raw
     * and will not be coerced.
     * @return The result of the message.  This may be a pointer, if the message
     * returns a pointer, or a value, if the method returns a BOOL, long, int,
     * short, byte, or char.  Note that you cannot use this method for
     * messages that return float and double values.  For these, you <em>must</em>
     * use msgDouble().  This is because doubles and floats are handled using
     * a different underlying runtime function (objc_msgSend_fpret). Alternatively
     * you can use the variation of msg() that takes coerceInput and coerceOutput
     * boolean values, as this will automatically choose the correct underlying
     * message function depending on the return type reported by the message
     * signature.
     * @see #msgDouble(Pointer, String, Object...)
     * @param receiver a {@link com.sun.jna.Pointer} object.
     */
    public static long msg(Pointer receiver, String msg, Object... args){
        return objc_msgSend(receiver, sel(msg), args);


    }

    /**
     * Generate the Runtime class name suffix in RuntimeMappings that defines
     * the appropriate version of objc_msgSend for the given arguments.
     * @param args The arguments to check.
     * @return The suffix.  This will be a binary string in which a 1 in the i'th
     *  index corresponds with a Structure.ByValue parameter.
     */
    private static String getArgsSuffix(Object... args) {
        StringBuilder sb = new StringBuilder();

        boolean foundStructByValue = false;
        for (Object o : args) {
            if (o instanceof Structure.ByValue) {
                foundStructByValue = true;
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        if (foundStructByValue) {
            return sb.toString();
        }
        return "";
    }

    /**
     * Gets the parameter types a call to objc_msgSend should use, including the first two
     * Pointer parameers.
     * @param args The input arguments.
     * @return The corresponding class types.  The output array will be longer than the input array by two, because
     *  of the two Pointer parameters at the beginning.
     */
    private static Class<?>[] getArgsParamTypes( Object... args) {
        Class<?>[] out = new Class<?>[args.length+2];
        out[0] = Pointer.class;
        out[1] = Pointer.class;
        for (int i=0; i<args.length; i++) {
            out[i+2] = (args[i] instanceof Structure.ByValue) ? Structure.ByValue.class: Object.class;
        }
        return out;
    }

    /**
     * Merges the parameters into a single array, for use in the {@link #objc_msgSend(Pointer, Pointer, Object...)}
     * method.
     * @param receiver The receiver of the message.
     * @param selector THe selector to call.
     * @param args The arguments.
     * @return The full parameter array.
     */
    private static Object[] merge(Pointer receiver, Pointer selector, Object... args) {
        Object[] out = new Object[args.length+2];
        out[0] = receiver;
        out[1] = selector;
        for (int i=0; i<args.length; i++) {
            out[i+2] = args[i];
        }
        return out;
    }

    /**
     *  A wrapper around the obj_msgSend() method to do preprocessing, and call the correct
     *  variant.  If any of the parameters are {@link Structure.ByValue}, then the dispatch will
     *  use reflection to find the correct JNA mapping.
     * @param receiver The receiver
     * @param selector The selector
     * @param args The arguments
     * @return The output
     */
    private static long objc_msgSend(Pointer receiver, Pointer selector, Object... args) {

        String argSuffix = getArgsSuffix(args);
        if (args.length <= 7 && argSuffix.isEmpty()) {
            switch (args.length) {
                case 0:
                    return rt.objc_msgSend(receiver, selector);
                case 1:
                    return rt.objc_msgSend(receiver, selector, args[0]);
                case 2:
                    return rt.objc_msgSend(receiver, selector, args[0], args[1]);
                case 3:
                    return rt.objc_msgSend(receiver, selector, args[0], args[1], args[2]);
                case 4:
                    return rt.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3]);
                case 5:
                    return rt.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3], args[4]);
                case 6:
                    return rt.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3], args[4], args[5]);
                case 7:
                    return rt.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                default:
                    throw new IllegalArgumentException("msg currently supports max 4 args");
            }
        } else {
            try {

                Class runtimeClass = RuntimeUtils.class.getClassLoader().loadClass("ca.weblite.objc.RuntimeMappings$Runtime"+argSuffix);
                Field runtimeInstanceField = runtimeClass.getField("INSTANCE");
                Object runtimeInstance = runtimeInstanceField.get(null);



                return (long)runtimeClass.getMethod("objc_msgSend", getArgsParamTypes(args)).invoke(runtimeInstance, merge(receiver, selector, args));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    
    /**
     * Sends a message to a specified class using the given selector.
     *
     * @param args The arguments passed to the method.  These are passed raw
     * and will not be coerced.
     * @return The result of the message.  This may be a pointer, if the message
     * returns a pointer, or a value, if the method returns a BOOL, long, int,
     * short, byte, or char.  Note that you cannot use this method for
     * messages that return float and double values.  For these, you <em>must</em>
     * use msgDouble().  This is because doubles and floats are handled using
     * a different underlying runtime function (objc_msgSend_fpret). Alternatively
     * you can use the variation of msg() that takes coerceInput and coerceOutput
     * boolean values, as this will automatically choose the correct underlying
     * message function depending on the return type reported by the message
     * signature.
     * @see #msgDouble(Pointer, Pointer, Object...)
     * @param receiver a {@link com.sun.jna.Pointer} object.
     * @param selector a {@link com.sun.jna.Pointer} object.
     */
    public static long msg(Pointer receiver, Pointer selector, Object... args){

        long out = objc_msgSend(receiver, selector, args);

        return out;
    }
    
    /**
     * Wrapper around msg() that returns a Pointer. This should only be used for
     * sending messages that return Pointers (or Objects).
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return A Pointer return value of the message.
     */
    public static Pointer msgPointer(Pointer receiver, Pointer selector, Object... args){
        long res = msg(receiver, selector, args);
        return new Pointer(res);
    }
    
    /**
     * Wrapper around msg() that returns a Pointer. This should only be used for
     * sending messages that return Pointers (or Objects).
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return A Pointer return value of the message.
     */
    public static Pointer msgPointer(Pointer receiver, String selector, Object... args){
        return msgPointer(receiver, sel(selector), args);
    }
    
    /**
     * Wrapper around msg() that returns a Pointer. This should only be used for
     * sending messages that return Pointers (or Objects).
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return A Pointer return value of the message.
     */
    public static Pointer msgPointer(String receiver, Pointer selector, Object... args){
        return msgPointer(cls(receiver), selector, args);
    }
    
    /**
     * Wrapper around msg() that returns a Pointer. This should only be used for
     * sending messages that return Pointers (or Objects).
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return A Pointer return value of the message.
     */
    public static Pointer msgPointer(String receiver, String selector, Object... args){
        return msgPointer(cls(receiver), sel(selector), args);
    }
    
    /**
     * Wrapper around msg() that returns an int. This should only be used for
     * sending messages that return int-compatible numeric values.  E.g.
     * byte, bool, long, int, short.  Do not use this if the message will return
     * something else (like a float, double, string, or pointer). Narrowing
     * primitive conversion will be applied; this will cause information loss
     * if the {@code long} result does not fit in the {@code int} value range.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return An int value returned by the message.
     */
    public static int msgInt(Pointer receiver, Pointer selector, Object... args){
        long res = msg(receiver, selector, args);
        return (int) res;
    }
    
    /**
     * Wrapper around msg() that returns an int. This should only be used for
     * sending messages that return int-compatible numeric values.  E.g.
     * byte, bool, long, int, short.  Do not use this if the message will return
     * something else (like a float, double, string, or pointer).
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return An int value returned by the message.
     */
    public static int msgInt(String receiver, Pointer selector, Object... args){
        return msgInt(cls(receiver), selector, args);
    }
    
    /**
     * Wrapper around msg() that returns an int. This should only be used for
     * sending messages that return int-compatible numeric values.  E.g.
     * byte, bool, long, int, short.  Do not use this if the message will return
     * something else (like a float, double, string, or pointer).
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return An int value returned by the message.
     */
    public static int msgInt(String receiver, String selector, Object... args){
        return msgInt(cls(receiver), sel(selector), args);
    }
    
    /**
     * Wrapper around msg() that returns an int. This should only be used for
     * sending messages that return int-compatible numeric values.  E.g.
     * byte, bool, long, int, short.  Do not use this if the message will return
     * something else (like a float, double, string, or pointer).
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return An int value returned by the message.
     */
    public static int msgInt(Pointer receiver, String selector, Object... args){
        return msgInt(receiver, sel(selector), args);
    }
    
    /**
     * Wrapper around msg() that returns a boolean value.  This should only be used
     * for sending messages that return boolean-compatible numeric values.  Essentially
     * any non-zero value is interpreted (and returned) as true.  Zero values are
     * interpreted as false.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return Boolean return value of the message.
     */
    public static boolean msgBoolean(Pointer receiver, Pointer selector, Object... args){
        long res = msg(receiver, selector, args);
        return res > 0L;
    }
    
    /**
     * Wrapper around msg() that returns a boolean value.  This should only be used
     * for sending messages that return boolean-compatible numeric values.  Essentially
     * any non-zero value is interpreted (and returned) as true.  Zero values are
     * interpreted as false.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return Boolean return value of the message.
     */
    public static boolean msgBoolean(String receiver, Pointer selector, Object... args){
        return msgBoolean(cls(receiver), selector, args);
    }
    
    /**
     * Wrapper around msg() that returns a boolean value.  This should only be used
     * for sending messages that return boolean-compatible numeric values.  Essentially
     * any non-zero value is interpreted (and returned) as true.  Zero values are
     * interpreted as false.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return Boolean return value of the message.
     */
    public static boolean msgBoolean(String receiver, String selector, Object... args){
        return msgBoolean(cls(receiver), sel(selector), args);
    }
    
    /**
     * Wrapper around msg() that returns a boolean value.  This should only be used
     * for sending messages that return boolean-compatible numeric values.  Essentially
     * any non-zero value is interpreted (and returned) as true.  Zero values are
     * interpreted as false.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return Boolean return value of the message.
     */
    public static boolean msgBoolean(Pointer receiver, String selector, Object... args){
        return msgBoolean(receiver, sel(selector), args);
    }
    
    /**
     * Wrapper around msg() that returns a string value.  This should only be used
     * for messages that return a CString.  Do not use this for messages that
     * return numeric values or Pointers to objects (e.g. NSString).  Use the
     * msg() variant that takes coerceInputs and coerceOutputs parameters if you
     * want NSStrings to be automatically converted to java Strings.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return The string return value of the message.
     */
    public static String msgString(Pointer receiver, Pointer selector, Object... args){
        long res = msg(receiver, selector, args);
        return new Pointer(res).getString(0);
    }
    
    /**
     * Wrapper around msg() that returns a string value.  This should only be used
     * for messages that return a CString.  Do not use this for messages that
     * return numeric values or Pointers to objects (e.g. NSString).  Use the
     * msg() variant that takes coerceInputs and coerceOutputs parameters if you
     * want NSStrings to be automatically converted to java Strings.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return The string return value of the message.
     */
    public static String msgString(String receiver, Pointer selector, Object... args){
        return msgString(cls(receiver), selector, args);
    }
    
    /**
     * Wrapper around msg() that returns a string value.  This should only be used
     * for messages that return a CString.  Do not use this for messages that
     * return numeric values or Pointers to objects (e.g. NSString).  Use the
     * msg() variant that takes coerceInputs and coerceOutputs parameters if you
     * want NSStrings to be automatically converted to java Strings.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return The string return value of the message.
     */
    public static String msgString(String receiver, String selector, Object... args){
        return msgString(cls(receiver), sel(selector), args);
    }
    
    /**
     * Wrapper around msg() that returns a string value.  This should only be used
     * for messages that return a CString.  Do not use this for messages that
     * return numeric values or Pointers to objects (e.g. NSString).  Use the
     * msg() variant that takes coerceInputs and coerceOutputs parameters if you
     * want NSStrings to be automatically converted to java Strings.
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return The string return value of the message.
     */
    public static String msgString(Pointer receiver, String selector, Object... args){
        return msgString(receiver, sel(selector), args);
    }
    
    /**
     * Sends a message that returns a double value.  This variant must be used
     * if the method returns a float or a double.  It doesn't actually wrap the
     * primitive msg() method..  Rather it uses a different core Objective-C method,
     * objc_msgSend_fpret().
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return The double return value of the message
     */
    public static double msgDouble(Pointer receiver, Pointer selector, Object... args){
        return objc_msgSend_fpret(receiver, selector, args);
    }

    /**
     * Flag to check if this is apple silicon. (arm64)
     */
    private static boolean isArm64 = System.getProperty("os.arch").equals("aarch64");

    /**
     * A wrapper for the objc_msgSend_fpret method.  Since this method is not available
     * in arm64, this will use a JNA mapping of objc_msgSend which returns double on that platform.
     * On Intel, it will dispatch to the correct objc_msgSend_fpret mapping, according to parameter.
     * @param receiver The receiver.
     * @param selector The selector.
     * @param args Arguments
     * @return The result.
     */
    private static double objc_msgSend_fpret(Pointer receiver, Pointer selector, Object... args) {
        if (isArm64) {
            switch (args.length) {
                case 0:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector);
                case 1:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector, args[0]);
                case 2:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector, args[0], args[1]);
                case 3:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector, args[0], args[1], args[2]);

                case 4:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3]);
                case 5:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3], args[4]);
                case 6:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3], args[4], args[5]);
                case 7:
                    return RuntmeArm64Extensions.INSTANCE.objc_msgSend(receiver, selector, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                default:
                    throw new IllegalArgumentException("objc_msgSend does not support "+args.length+" arguments yet");
            }

        }
        switch (args.length) {
            case 0:
                return rt.objc_msgSend_fpret(receiver, selector);
            case 1:
                return rt.objc_msgSend_fpret(receiver, selector, args[0]);
            case 2:
                return rt.objc_msgSend_fpret(receiver, selector, args[0], args[1]);
            case 3:
                return rt.objc_msgSend_fpret(receiver, selector, args[0], args[1], args[2]);

            case 4:
                return rt.objc_msgSend_fpret(receiver, selector, args[0], args[1], args[2], args[3]);
            case 5:
                return rt.objc_msgSend_fpret(receiver, selector, args[0], args[1], args[2], args[3], args[4]);
            case 6:
                return rt.objc_msgSend_fpret(receiver, selector, args[0], args[1], args[2], args[3], args[4], args[5]);
            case 7:
                return rt.objc_msgSend_fpret(receiver, selector, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            default:
                throw new IllegalArgumentException("objc_msgSend_fpret does not support "+args.length+" arguments yet");
        }
    }
    
    /**
     * Sends a message that returns a double value.  This variant must be used
     * if the method returns a float or a double.  It doesn't actually wrap the
     * primitive msg() method..  Rather it uses a different core Objective-C method,
     * objc_msgSend_fpret().
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return The double return value of the message
     */
    public static double msgDouble(String receiver, Pointer selector, Object... args){
        return msgDouble(cls(receiver), selector, args);
    }
    
    /**
     * Sends a message that returns a double value.  This variant must be used
     * if the method returns a float or a double.  It doesn't actually wrap the
     * primitive msg() method..  Rather it uses a different core Objective-C method,
     * objc_msgSend_fpret().
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return The double return value of the message
     */
    public static double msgDouble(String receiver, String selector, Object... args){
        return msgDouble(cls(receiver), sel(selector), args);
    }
    
    /**
     * Sends a message that returns a double value.  This variant must be used
     * if the method returns a float or a double.  It doesn't actually wrap the
     * primitive msg() method..  Rather it uses a different core Objective-C method,
     * objc_msgSend_fpret().
     *
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return The double return value of the message
     */
    public static double msgDouble(Pointer receiver, String selector, Object... args){
        return msgDouble(receiver, sel(selector), args);
    }

    private static void sleep50() {
        try {
            Thread.sleep(500);
        } catch (Exception ex){}
    }

    /**
     * Sends a message with the option of coercing the inputs and outputs. This variant
     * uses a higher level of abstraction than the standard msg() and msgXXX() methods.
     * If coerceReturn and coerceArgs are set to true, then this method will
     * convert the inputs from Java inputs to the corresponding Objective-C inputs.
     * Further it will return values that are more appropriate for use in Java.
     *
     * <p>Furthermore, this variant is smart about which variable of msg() to call
     * behind the scenes.  E.g. if the message signature specifies that this message
     * returns a double, it will automatically use the double variant of msg().</p>
     *
     * @param coerceReturn If true, then the return value will be mapped to an appropriate
     *  Java value using the TypeMapper class.
     * @param coerceArgs If true, then the inputs will be mapped from Java to appropriate
     * C values using the TypeMapper class.
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments to be passed in the message.
     * @return The return value of the message.  This may be a Pointer or a value depending
     *  on the return type of the message.
     */
    public static Object msg(boolean coerceReturn, boolean coerceArgs, Pointer receiver, Pointer selector, Object... args){

        Pointer methodSignature = msgPointer(receiver, "methodSignatureForSelector:", selector);
        if (Pointer.nativeValue(methodSignature) == 0L) {
            throw new RuntimeException(new NoSuchMethodException("Method cannot be found for signature "+Pointer.nativeValue(selector)));
        }
       
        int numArgs = (int)msg(methodSignature, "numberOfArguments");
        if ( numArgs >=2   &&  numArgs != args.length+2 ){
            throw new RuntimeException("Wrong argument count.  The selector "+selName(selector)+" requires "+(numArgs-2)+" arguments, but received "+args.length);
        }
        
        
        long returnTypePtr = msg(methodSignature, "methodReturnType");
        String returnTypeSignature = new Pointer(returnTypePtr).getString(0);
        if ( numArgs == 0 && returnTypeSignature == null ){
            return msg(receiver, selector, args);
        }
        
        
        if ( coerceArgs && args.length > 0 ){
            for ( int i=0; i<args.length; i++ ){
                long out2 = msg(methodSignature, "getArgumentTypeAtIndex:", i+2);
                String argumentTypeSignature = new Pointer(out2).getString(0);
                args[i] = TypeMapper.getInstance().jToC(args[i], argumentTypeSignature, TypeMapper.getInstance());
            }
        }
        
        
       String prefixes = "rnNoORV";
        int offset = 0;
        while ( prefixes.indexOf(returnTypeSignature.charAt(offset)) != -1 ){
            offset++;
            if ( offset > returnTypeSignature.length()-1 ){
                break;
            }
        }
        if ( offset > 0 ){
            returnTypeSignature = returnTypeSignature.substring(offset);
        }
        
        char returnTypeFirstChar = returnTypeSignature.charAt(0);
        if ( "[{(".indexOf(returnTypeFirstChar) ==-1 ){
            // We are not returning a structure so we'll just
            // do the message.
            
            // We need to handle doubles and floats separately
            if ( "df".indexOf(returnTypeFirstChar) != -1 ){
                Object res = msgDouble(receiver, selector, args);
                for ( int i=0; i<args.length; i++){
                    Proxy.release(args[i]);
                }
                return res;
            } else {
            
                long result = msg(receiver, selector, args);
                if ( coerceReturn ){
                    Object res2 =  TypeMapper.getInstance().cToJ(result, returnTypeSignature, TypeMapper.getInstance());
                    for ( int i=0; i<args.length; i++){
                        Proxy.release(args[i]);
                    }
                    return res2;
                } else {
                    for ( int i=0; i<args.length; i++){
                        Proxy.release(args[i]);
                    }
                    return result;
                }
            }
        }
        

        Object output =  msg(receiver, selector, args);
        for ( int i=0; i<args.length; i++){
            Proxy.release(args[i]);
        }
        return output;
    }
    
    
    /**
     * Returns the size of an array that is specified in a signature.
     *
     * @param signature The signature for a parameter using <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encodings</a>.
     * @return The size of the array.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C Type Encodings</a>
     */
    public static int arraySize(String signature){
        int typeIndex = 2;
        String digits = "0123456789";
            
        while ( digits.indexOf(signature.charAt(typeIndex++)) != -1 ){}
        
        return Integer.parseInt(signature.substring(1, typeIndex));
        
    }
    
     
     
     /**
      * Returns the pointer to the Native peer for a Peerable object.
      *
      * @param peer a {@link ca.weblite.objc.Peerable} object.
      * @return The Pointer to a native peer.
      */
     public static Pointer addr(Peerable peer){
         return peer.getPeer();
     }
     
     /**
      * Sends a batch of messages in sequence.
      *
      * @param messages a {@link ca.weblite.objc.Message} object.
      * @return a {@link java.lang.Object} object.
      */
     public static Object msg(Message... messages){
         for ( int i=0; i<messages.length; i++){
             if ( i>0 ){
                 messages[i].previous = messages[i-1];
             }
             if ( i< messages.length-1 ){
                 messages[i].next = messages[i+1];
             }
         }
         for ( int i=0; i<messages.length; i++){
             Message m = messages[i];
             if ( m.receiver == Pointer.NULL ){
                 m.receiver = (Pointer)m.previous.result;
             }
             m.beforeRequest();
             if ( m.status == Message.STATUS_SKIPPED ){
                 continue;
             } else if ( m.status == Message.STATUS_CANCELLED){
                 break;
             } else {
                 
                 boolean coerceInput = false;
                 boolean coerceOutput = false;
                 if ( i == messages.length -1 ){
                     coerceInput = m.coerceInput;
                     coerceOutput = m.coerceOutput;
                 }
                 
                 try {
                    
                    m.result = msg(coerceOutput, coerceInput, m.receiver, m.selector, m.args.toArray());
                 } catch (Exception ex){
                     m.error = ex;
                 }
                 m.status = Message.STATUS_COMPLETED;
             }
             
         }
         
         if ( messages.length > 0 ){
             return messages[messages.length-1].result;
         } else {
             throw new RuntimeException("Message queue was empty");
         }
     }
    
    /**
     * Converts a Java string to an NSString object.
     *
     * @param str The Java string to convert.
     * @return Pointer to the NSString that corresponds to this string.
     */
    public static Pointer str(String str){
        return msgPointer("NSString", "stringWithUTF8String:", str);
    }
    
    /**
     * Converts A native NSString object to a Java string.
     *
     * @param str a {@link com.sun.jna.Pointer} object.
     * @return A Java string.
     */
    public static String str(Pointer str){
        long ptr = msg(str, "UTF8String");
        return new Pointer(ptr).getString(0);
    }
    
    /**
     * Wraps the given value in the appropriate ByReference subclass according
     * to the provided signature.  This is a useful method in cases where
     * you need to pass a parameter to a C-function by reference, but you don't
     * necessarily know what type of data it is.
     *
     * @param val The value to be wrapped in a byReference.
     * @param signature The signature (using <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encodings</a>) of the value.
     * @return A pointer to the reference that can be passed in a C function.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C Type Encodings</a>
     */
    public static Pointer getAsReference(Object val, String signature){
        return getAsReferenceWrapper(val, signature).getPointer();
    }
    
    /**
     * Wraps the given value in the appropriate ByReference subclass according
     * to the provided signature.  This is a useful method in cases where
     * you need to pass a parameter to a C-function by reference, but you don't
     * necessarily know what type of data it is.
     *
     * @param val The value to be wrapped in a byReference.
     * @param signature The signature (using <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C type encodings</a>) of the value.
     * @return The ByReference object that contains the pointer that can be passed
     * to a c function.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C Type Encodings</a>
     */
    public static ByReference getAsReferenceWrapper(Object val, String signature){
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
        
        switch ( signature.charAt(0)){
            case 'i':
            case 'I':
                int intVal;
                if (val instanceof Number) {
                    intVal = ((Number) val).intValue();
                } else if (val instanceof String) {
                    intVal = Integer.parseInt((String) val);
                } else {
                    throw new RuntimeException("Attempt to pass ineligible value to int: "+val);
                }
                return new IntByReference(intVal);
            case 's':
            case 'S':
                short shortVal;
                if (val instanceof Number) {
                    shortVal = ((Number) val).shortValue();
                } else if (val instanceof String) {
                    shortVal = Short.parseShort((String) val);
                } else {
                    throw new RuntimeException("Attempt to pass ineligible value to short: "+val);
                }
                return new ShortByReference(shortVal);
                
            case 'l':
            case 'L':
            case 'q':
            case 'Q':
                long longVal;
                if (val instanceof Number) {
                    longVal = ((Number) val).longValue();
                } else if (val instanceof String) {
                    longVal = Long.parseLong((String) val);
                } else {
                    throw new RuntimeException("Attempt to pass ineligible value to long: "+val);
                }
                return new LongByReference(longVal);
                
            case 'f':
                float floatVal;
                if (val instanceof Number) {
                    floatVal = ((Number) val).floatValue();
                } else if (val instanceof String) {
                    floatVal = Float.parseFloat((String) val);
                } else {
                    throw new RuntimeException("Attempt to pass ineligible value to float: "+val);
                }
                return new FloatByReference(floatVal);
                
            case 'd':
                double doubleVal;
                if (val instanceof Number) {
                    doubleVal = ((Number) val).doubleValue();
                } else if (val instanceof String) {
                    doubleVal = Double.parseDouble((String) val);
                } else {
                    throw new RuntimeException("Attempt to pass ineligible value to double: "+val);
                }
                return new DoubleByReference(doubleVal);
            case 'B':
            case 'b':
            case 'c':
            case 'C':
                byte byteVal;
                if (val instanceof Boolean) {
                    byteVal = (byte) (Boolean.TRUE.equals(val) ? 1 : 0);
                } else if (val instanceof Number) {
                    byteVal = ((Number) val).byteValue();
                } else if (val instanceof String) {
                    byteVal = Byte.parseByte((String) val);
                } else {
                    throw new RuntimeException("Attempt to pass ineligible value to byte: " + val);
                }
                return new ByteByReference(byteVal);
            case 'v':
                return null;
            case '^':
            default:
                if (val instanceof Pointer) {
                    return new PointerByReference((Pointer)val);
                } else if (val instanceof Long) {
                    return new PointerByReference(new Pointer((Long) val));
                } else {
                    throw new RuntimeException("Don't know what to do for conversion of value "+val+" and signature "+signature);
                }      
        }
        
    }
    
    /**
     * Initializes the libjcocoa library.  This is called when the class is first
     * loaded.  It sets up the JNI environment that will be used there forward.
     */
    private static native void init();
    
    /**
     * Registers a Java object with the Objective-C runtime so that it can begin
     * to receive messages from it.  This will create an Objective-C proxy
     * that passes messages to the Recipient object.  This step is automatically
     * handled by the NSObject class inside its init() method.
     *
     * @param client a {@link ca.weblite.objc.Recipient} object.
     * @return a long.
     */
    public static native long createProxy(Recipient client);
    
    /**
     * Returns the Java peer recipient for a native Objective-C object if it
     * exists.  This will return null if nsObject is not an WLProxy object
     * that has been previously registered with a Recipient.
     *
     * @param nsObject a long.
     * @return a {@link ca.weblite.objc.Recipient} object.
     */
    public static native Recipient getJavaPeer(long nsObject);
    
    
    
}
