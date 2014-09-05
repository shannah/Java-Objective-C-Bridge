/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;


import ca.weblite.nativeutils.NativeUtils;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


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
 * @author shannah
 * 
 * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ObjCRuntimeRef/Reference/reference.html">Objective-C Runtime Reference</a>
 */
public class RuntimeUtils {
    
    
    /**
     * Short reference to the runtime instance for interacting with Objective-C
     */
    public static Runtime rt = Runtime.INSTANCE;
    
    /**
     * Flag to indicate whether the jcocoa native library was loaded successfully.
     * If it fails to load, then this flag will be false.  Therefore, if this
     * flag is false, you shouldn't try to use the the api at all.
     */
    public static boolean loaded = false;
    static {
        try {
            //System.loadLibrary("jcocoa");
            NativeUtils.loadLibraryFromJar("/libjcocoa.dylib");
            loaded = true;
        } catch (UnsatisfiedLinkError err){
            err.printStackTrace(System.err);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        init();
    }
    
    
    
    
    /**
     * Returns a pointer to the class for specific class name.
     * <pre>
     * {@code
     * Pointer nsObject = cls("NSObject");
     * }</pre>
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
     * @see Runtime.class_getName()
     */
    public static String clsName(Pointer cls){
        return rt.class_getName(cls);
    }
    
    /**
     * A wrapper for the clsName() method given a Peerable object that wraps
     * the class pointer.  This will return the class name.
     * @param peer
     * @return The class name of the objective-c class that is wrapped by the 
     * given Peerable object.
     * @see Runtime.class_getName()
     */
    public static String clsName(Peerable peer){
        return clsName(peer.getPeer());
    }
    
    /**
     * Returns a pointer to the selector specified by the given selector name.
     * @param name
     * @return Pointer to an Objective-C message selector.
     * @see <a href="http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/objectivec/Chapters/ocSelectors.html">Objective-C Selectors Reference</a>
     */
    public static Pointer sel(String name){
        return rt.sel_getUid(name);
    }
    
    
    /**
     * Returns a pointer to the selector that is wrapped by a Peerable object.
     * @param peer
     * @return Pointer to a specified selector.
     */
    public static Pointer sel(Peerable peer){
        return peer.getPeer();
    }
    
    /**
     * Returns the name of a selector.
     * @param sel Pointer to a selector.
     * @return The name of the selector.
     */
    public static String selName(Pointer sel){
        return rt.sel_getName(sel);
    }
    
    /**
     * Returns the name of a selector.
     * @param sel Peerable that wraps a pointer to a selector.
     * @return The name of the selector.
     */
    public static String selName(Peerable peer){
        return selName(peer.getPeer());
    }
    
    /**
     * Sends a message to a specified class using the given selector.
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
     * 
     * @see msgDouble()
     */
    public static long msg(String cls, String msg, Object... args){
        return msg(cls(cls), msg, args);
    }
    
    /**
     * Sends a message to a specified class using the given selector.
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
     * 
     * @see msgDouble()
     */
    public static long msg(String cls, Pointer msg, Object... args){
        return msg(cls(cls), msg, args);
    }
    
    
    /**
     * Sends a message to a specified class using the given selector.
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
     * 
     * @see msgDouble()
     */
    public static long msg(Pointer receiver, String msg, Object... args){
        return rt.objc_msgSend(receiver, sel(msg), args);
    }
    
    /**
     * Sends a message to a specified class using the given selector.
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
     * 
     * @see msgDouble()
     */
    public static long msg(Pointer receiver, Pointer selector, Object... args){
        return rt.objc_msgSend(receiver, selector, args);
    }
    
    /**
     * Wrapper around msg() that returns a Pointer. This should only be used for
     * sending messages that return Pointers (or Objects).
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
     * something else (like a float, double, string, or pointer).
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args The arguments passed to the message.
     * @return An int value returned by the message.
     */
    public static int msgInt(Pointer receiver, Pointer selector, Object... args){
        long res = msg(receiver, selector, args);
        return new Long(res).intValue();
    }
    
    /**
     * Wrapper around msg() that returns an int. This should only be used for
     * sending messages that return int-compatible numeric values.  E.g. 
     * byte, bool, long, int, short.  Do not use this if the message will return
     * something else (like a float, double, string, or pointer).
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
     * @param receiver The target of the message.
     * @param selector The selector for the message.
     * @param args Arguments passed to the message.
     * @return Boolean return value of the message.
     */
    public static boolean msgBoolean(Pointer receiver, Pointer selector, Object... args){
        long res = msg(receiver, selector, args);
        return res > 0L ? true : false;
    }
    
    /**
     * Wrapper around msg() that returns a boolean value.  This should only be used
     * for sending messages that return boolean-compatible numeric values.  Essentially
     * any non-zero value is interpreted (and returned) as true.  Zero values are 
     * interpreted as false.
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
        return rt.objc_msgSend_fpret(receiver, selector, args);
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
     * 
     */
    public static Object msg(boolean coerceReturn, boolean coerceArgs, Pointer receiver, Pointer selector, Object... args){
        Object[] originalArgs = args;
        
        Pointer methodSignature = msgPointer(receiver, "methodSignatureForSelector:", selector);
       
        int numArgs = (int)msg(methodSignature, "numberOfArguments");
        if ( numArgs ==2   &&  numArgs != args.length+2 ){
            throw new RuntimeException("Wrong argument count.  The selector "+selName(selector)+" requires "+(numArgs-2)+" arguments, but received "+args.length);
        }
        
        
        long returnTypePtr = msg(methodSignature, "methodReturnType");
        String returnTypeSignature = new Pointer(returnTypePtr).getString(0);
        if ( numArgs == 0 && returnTypeSignature == null ){
            return msg(receiver, selector, args);
        }
        
        
        if ( coerceArgs && args.length > 0 ){
            originalArgs = Arrays.copyOf(args, args.length);
            
            for ( int i=0; i<args.length; i++ ){
                ByteByReference out = new ByteByReference();
                
                long out2 = (long)msg(methodSignature, "getArgumentTypeAtIndex:", i+2);
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
        
        String returnTypeFirstChar = returnTypeSignature.substring(0,1);
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
      * @param peer
      * @return The Pointer to a native peer.
      */
     public static Pointer addr(Peerable peer){
         return peer.getPeer();
     }
     
     /**
      * Sends a batch of messages in sequence.
      * @param messages
      * @return The result of the last message sent.  The results of the 
      *  individual messages will also be stored inside the message objects themselves.
      * 
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
                    
                    m.result = msg(coerceOutput, coerceInput, m.receiver, m.selector, m.args.toArray(new Object[m.args.size()]));
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
      * @param str The Java string to convert.
      * @return Pointer to the NSString that corresponds to this string.
      */
    public static Pointer str(String str){
        return msgPointer("NSString", "stringWithUTF8String:", str);
    }
    
    /**
     * Converts A native NSString object to a Java string.
     * @param str
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
        
        String firstChar = signature.substring(0,1);
        String numeric = "iIsSlLqQfd";
        
       
        
        
        //String firstChar = signature.substring(0,1);
        switch ( signature.charAt(0)){
            case 'i':
            case 'I':
                if ( !int.class.isInstance(val) ){
                    if ( Number.class.isInstance(val) ){
                        val = ((Number)val).intValue();
                    } else if ( String.class.isInstance(val)){
                        val = Integer.parseInt((String)val);
                    } else {
                        throw new RuntimeException("Attempt to pass ineligible value to int: "+val);
                    }
                }
                return new IntByReference((Integer)val);
            case 's':
            case 'S':
                if ( !short.class.isInstance(val) ){
                    if ( Number.class.isInstance(val) ){
                        val = ((Number)val).shortValue();
                    } else if ( String.class.isInstance(val)){
                        val = new Integer(Integer.parseInt((String)val)).shortValue();
                    } else {
                        throw new RuntimeException("Attempt to pass ineligible value to short: "+val);
                    }
                }
                return new ShortByReference((Short)val);
                
            case 'l':
            case 'L':
            case 'q':
            case 'Q':
                if ( !long.class.isInstance(val) ){
                    if ( Number.class.isInstance(val) ){
                        val = ((Number)val).longValue();
                    } else if ( String.class.isInstance(val)){
                        val = new Long(Long.parseLong((String)val)).longValue();
                    } else {
                        throw new RuntimeException("Attempt to pass ineligible value to long: "+val);
                    }
                }
                return new LongByReference((Long)val);
                
            case 'f':
                if ( !float.class.isInstance(val) ){
                    if ( Number.class.isInstance(val) ){
                        val = ((Number)val).floatValue();
                    } else if ( String.class.isInstance(val)){
                        val = new Float(Float.parseFloat((String)val)).floatValue();
                    } else {
                        throw new RuntimeException("Attempt to pass ineligible value to long: "+val);
                    }
                }
                return new FloatByReference((Float)val);
                
            case 'd':
                if ( !double.class.isInstance(val) ){
                    if ( Number.class.isInstance(val) ){
                        val = ((Number)val).doubleValue();
                    } else if ( String.class.isInstance(val)){
                        val = new Double(Double.parseDouble((String)val)).doubleValue();
                    } else {
                        throw new RuntimeException("Attempt to pass ineligible value to long: "+val);
                    }
                }
                return new DoubleByReference((Double)val);
            case 'B':
            case 'b':
            case 'c':
            case 'C':
                if ( Number.class.isInstance(val) ){
                    val = ((Number)val).byteValue();
                } else if ( String.class.isInstance(val)){
                    val = new Byte(Byte.parseByte((String)val)).byteValue();
                } else {
                    throw new RuntimeException("Attempt to pass ineligible value to byte: "+val);
                }
                return new ByteByReference((Byte)val);
            case 'v':
                return null;
            case '^':
            default:
                ////System.out.println("Outputting pointer by reference for value "+val+" and signature "+signature);
                if ( val == null ){
                    try {
                        throw new RuntimeException("Checking stack trace for val "+val+" and signature "+signature);
                        
                    } catch (Exception ex){
                        ex.printStackTrace(System.err);
                    }
                }
                if ( Pointer.class.isInstance(val) ){
                    return new PointerByReference((Pointer)val);
                } else if ( Long.class.isInstance(val) || long.class.isInstance(val)){
                    return new PointerByReference(new Pointer((Long)val));
                } else {
                    throw new RuntimeException("Don't know what to do for conversion of value "+val+" and signature "+signature);
                }
                
            
                
        }
        
    }
    
    /**
     * Initializes the libjcocoa library.  This is called when the class is first
     * loaded.  It sets up the JNI environment that will be used there forward.
     */
    public static native void init();
    
    /**
     * Registers a Java object with the Objective-C runtime so that it can begin
     * to receive messages from it.  This will create an Objective-C proxy 
     * that passes messages to the Recipient object.  This step is automatically
     * handled by the NSObject class inside its init() method.
     * @param client
     * @return 
     */
    public static native long createProxy(Recipient client);
    
    /**
     * Returns the Java peer recipient for a native Objective-C object if it 
     * exists.  This will return null if nsObject is not an WLProxy object
     * that has been previously registered with a Recipient.
     * @param nsObject
     * @return 
     */
    public static native Recipient getJavaPeer(long nsObject);
    
    
    
}
