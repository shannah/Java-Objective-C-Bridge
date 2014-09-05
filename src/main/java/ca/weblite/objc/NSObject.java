/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import ca.weblite.objc.annotations.Msg;
import com.sun.jna.ptr.LongByReference;
import java.lang.reflect.Method;
import static ca.weblite.objc.RuntimeUtils.*;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerTool;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.PointerByReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * The base class for objects that can interact with the Objective-C runtime.
 * NSObjects are connected to both an Objective-C peer object, and an Objective-C
 * parent object.  The peer is a reflection of the object in Objective-C.  It is
 * a WLProxy object that will simply forward messages from Objective-C to Java.
 * 
 * <p>The parent object is used as a sort of superclass so that messages that aren't
 * explicitly handled by the Java class can be handled by the superclass.</p>
 * 
 * <h3>Simple Example</h3>
 * 
 * <p>The following example shows a subclass of NSObject that is used as a delegate
 * for an NSOpenPanel.  Notice, that, by using the {@literal @}Msg annotation, the
 * start() method is effectively called via Objective-C.  Similarly, the panelSelectionDidChange()
 * method is called by the NSOpenPanel class to respond to events when the user clicks on 
 * a different item in the open dialog.</p>
 * <script src="https://gist.github.com/3966989.js?file=NSOpenPanelSample.java"></script>
 * 
 * <p>If you run this application, it will open an NSOpenPanel modal dialog and allow you to 
 * select a file.  If you run this program and select a single file, the output will look
 * something like:</p>
 * 
 * <script src="https://gist.github.com/3966989.js?file=output.txt"></script>
 * 
 * 
 * @see NSOpenPanelSample
 * 
 * @author shannah
 */
public class NSObject extends Proxy implements PeerableRecipient {
    
    
    
    /**
     * Pointer to the parent objective-c object of this object.
     */
    public Pointer parent;
    
    /**
     * Pointer to the objective-c class of the parent object.
     */
    private Pointer cls;
    
    /**
     * Maps string selectors to java methods for this class.
     */
    private static  Map<Class,Map<String,Method>> methodMap = new HashMap<Class,Map<String,Method>>();
    
    /**
     * Returns the method map for a particular class.  The Map that is returned maps string selectors
     * to Method objects.
     * @param cls The class whose map we wish to obtain
     * @return The map that maps string selectors
     */
    protected static Map<String,Method> getMethodMap(Class cls){
        Map<String,Method> mm = methodMap.get(cls);
        if ( mm == null ){
            mm = new HashMap<String,Method>();
            Method[] methods = cls.getMethods();
            for ( int i=0; i<methods.length; i++){
                Method method = methods[i];
                Msg message = (Msg)method.getAnnotation(Msg.class);
                if ( message != null){
                    mm.put(message.selector(), method);
                    
                }
            }
            methodMap.put(cls, mm);
        }
        return mm;
    }
    
     
    public NSObject(String className){
        this();
        init(className);
    } 
    
    /**
     * Creates null proxy (i.e. a proxy around a null pointer).  In order
     * to make this class functional and register it with the objective-c
     * runtime, you still need to call one of the init() method variants.
     */
    public NSObject(){
        super();

    }
    
    /**
     * Creates an NSObject to wrap (i.e. send messages to) the specified
     * Objective-C object.  This doesn't actually register an object yet
     * with the Objective-C runtime.  You must still call init() to do this.
     * 
     * @param peer 
     */
    public NSObject(Pointer peer){
        super(peer);
    }
    
    /**
     * Creates a null proxy using the specified client as the default client
     * with which to send messages to the objective-c runtime.
     * @param c The client that should be used to send messages in this
     * object.
     */
    public NSObject(Client c){
        super(c);
    }
    
    /**
     * Creates a proxy for the specified objective-c object.
     * @param c The client that should be used for sending messages via this proxy.
     * 
     * @param peer The peer object.
     */
    public NSObject(Client c, Pointer peer){
        super(c, peer);
    }
    
    
    /**
     * Initializes this object and registers it with the Objective-C runtime.
     * @param parent A pointer to a parent object that is used as a sort of 
     *  super class.  I.e. messages that this object doesn't handle will be
     * passed to this parent object transparently in the background.  It
     * acts 100% as a superclass would.
     * @return Self for chaining.
     */
    public NSObject init(Pointer parent){
        this.cls = Runtime.INSTANCE.object_getClass(parent);
        this.parent = parent;
        
        if ( this.peer == Pointer.NULL ){
            this.peer = new Pointer(RuntimeUtils.createProxy(this));
        }
        
        return this;
    }
    
    
    /**
     * Initializes this object and registers it with the Objective-C runtime.
     * @param parent A pointer to a parent object that is used as a sort of 
     *  super class.  I.e. messages that this object doesn't handle will be
     * passed to this parent object transparently in the background.  It
     * acts 100% as a superclass would.
     * @param cls The name of the class to use as the super class for this object.
     * @return Self for chaining.
     */
    public NSObject init(String cls){
        Pointer res = Client.getRawClient().sendPointer(cls, "alloc");
        Client.getRawClient().sendPointer(res, "init");
        return init(res);
        
    }
    /*
    @Msg(selector="valueForKey:", like="NSObject.valueForKey:")
    public Object valueForKey(String key){
        try {
            Field fld = this.getClass().getField(key);
            return fld.get(this);
        } catch (Exception ex) {
           
        }
        Pointer sig = this.methodSignatureForSelector(sel("valueForKey:"));
        Proxy invocation = Client.getInstance().sendProxy("NSInvocation", "invocationWithMethodSignature:", sig);
        invocation.send("setTarget:", parent);
        Pointer nsKey = str(key);
        
        invocation.send("setArgument:AtIndex:", nsKey, 2);
        this.forwardInvocationToParent(invocation.getPeer());
        Pointer p = new PointerByReference().getPointer();
        invocation.send("getReturnValue:", p );
        
    }
   */
    
    

    /**
     * Returns the java method that responds to a specific selector for the
     * current object.
     * @param selector The 
     * @return The method object that handles the specified selector (or null
     * if none is specified).
     * 
     * @see RuntimeUtils.sel()
     */
    public Method methodForSelector(String selector){
        return getMethodMap(this.getClass()).get(selector);
        
    }

    /**
     * Returns the NSMethodSignature (Objective-C) object pointer for the 
     * specified selector.  If there is a Java method registered with this
     * selector, then it will return its signature.  Otherwise it will
     * return the method signature of the parent object.
     * @param selector
     * @return Pointer to an NSMethodSignature object.
     * 
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSMethodSignature_Class/Reference/Reference.html">NSMethodSignature Class Reference</a>
     */
    public Pointer methodSignatureForSelector(Pointer selector){
        long res = methodSignatureForSelector(PointerTool.getPeer(selector));
        return new Pointer(res);
    }
    
    /**
     * Returns the NSMethodSignature (Objective-C) object pointer for the 
     * specified selector.  If there is a Java method registered with this
     * selector, then it will return its signature.  Otherwise it will
     * return the method signature of the parent object.
     * @param selector
     * @return Pointer to an NSMethodSignature object.
     * 
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSMethodSignature_Class/Reference/Reference.html">NSMethodSignature Class Reference</a>
     */
    @Override
    public long methodSignatureForSelector(long lselector) {
        
        Pointer selector = new Pointer(lselector);
        Method method = methodForSelector(selName(selector));
        if ( method != null){
            Msg message = (Msg)method.getAnnotation(Msg.class);
            if ( !"".equals(message.signature()) ){
                long res =  PointerTool.getPeer(
                        msgPointer(cls("NSMethodSignature"), "signatureWithObjCTypes:", message.signature())
                );
                return res;
            } else if ( !"".equals(message.like())){
                String[] parts = message.like().split("\\.");
                Proxy instance = client.chain(parts[0], "alloc").chain("init");
                Pointer out = msgPointer(instance.getPeer(), "methodSignatureForSelector:", sel(parts[1]));
                return PointerTool.getPeer(out);
            } 
            
        }
        
        return PointerTool.getPeer(msgPointer(parent, "methodSignatureForSelector:", selector));

    }
    
    /**
     * Forwards an NSInvocation to the parent object to be handled.  The parent will
     * handle the invocation (if it contains an appropriate selector), but the peer
     * will still be treated as the "Self" of the message.  I.e. this acts exactly
     * like calling super() in an OO language.
     * @param invocation Pointer to the objective-c NSInvocation object.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSInvocation_Class/Reference/Reference.html">NSInvocation Class Reference</a>
     */
    public void forwardInvocationToParent(Pointer invocation){
        forwardInvocationToParent(PointerTool.getPeer(invocation));
    }
    
    
    /**
     * Forwards an NSInvocation to the parent object to be handled.  The parent will
     * handle the invocation (if it contains an appropriate selector), but the peer
     * will still be treated as the "Self" of the message.  I.e. this acts exactly
     * like calling super() in an OO language.
     * @param invocation Pointer to the objective-c NSInvocation object.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSInvocation_Class/Reference/Reference.html">NSInvocation Class Reference</a>
     */
    public void forwardInvocationToParent(long linvocation){
        Pointer invocation = new Pointer(linvocation);
        Client rawClient = Client.getRawClient();
        
        
        Pointer sig = msgPointer(invocation, "methodSignature");
        Proxy pSig = new Proxy(rawClient, sig);
        Pointer selector = msgPointer(invocation, "selector");
        long numArgs = (Long)pSig.send("numberOfArguments");
        long respondsToSelector = msg(parent, "respondsToSelector:", selector );
        if ( respondsToSelector > 0 ){
            long impl = msg(parent, "methodForSelector:", selector);
            Pointer pImpl = new Pointer(impl);
            Function func = Function.getFunction(pImpl);
            long returnType = (Long)pSig.send("methodReturnType");
            String strReturnType = new Pointer(returnType).getString(0);
            String prefixes = "rnNoORV";
            int offset = 0;
            while ( prefixes.indexOf(strReturnType.charAt(offset)) != -1 ){
                offset++;
                if ( offset > strReturnType.length()-1 ){
                    break;
                }
            }
            if ( offset > 0 ){
                strReturnType = strReturnType.substring(offset);
            }
            
            Object[] args = new Object[new Long(numArgs).intValue()];
            args[0] = peer;
            args[1] = parent;
            for ( int i=2; i<numArgs; i++){
                long argumentSigAddr = (Long)pSig.send("getArgumentTypeAtIndex:", i);
                String argumentSignature = new Pointer(argumentSigAddr).getString(0);
                LongByReference ptrRef = new LongByReference();
                msg(invocation, "getArgument:atIndex:", ptrRef.getPointer(), i);
                args[i] = ptrRef.getValue();
            }
            char retTypeChar = strReturnType.charAt(0);
            
            Class retType = null;
            switch ( retTypeChar){
                case 'v':
                    retType = void.class; break;
                    
                case 'f':
                    retType = float.class; break;
                case 'd':
                    retType = double.class; break;
                
                case '*':
                    retType = String.class; break;
                    
                case 'i':
                case 'I':
                case 's':
                case 'S':
                case 'c':
                case 'C':
                case 'B':
                    
                    retType = int.class;break;
                
                    
                case 'l':
                case 'L':
                case 'q':
                case 'Q':
                    retType = long.class;break;
                
                case '@':
                case '#':
                case ':':
                case '^':
                case '?':
                    retType = Pointer.class; break;
                default:
                    // If we don't know how to handle the return type properly, 
                    // then let's just give up and pass it to the parent object
                    // the normal way
                    //System.out.println("We give up... passing "+sel(selector)+" to parent");
                    msg(invocation, "invokeWithTarget:", parent);
                    return;
                    
                
                    
                    
            }
            
            Object retVal = func.invoke(retType, args);
            
            if ( !void.class.equals(retType)){
                // We need to set the return value.
                
                if ( retVal == null ){
                    retVal = 0L;
                }
                Pointer retValRef = RuntimeUtils.getAsReference(retVal, strReturnType);
                msg(invocation, "setReturnValue:", retValRef );
               
            }
           
            
            
        } else {
            throw new RuntimeException("Object does not handle selector "+selName(selector));
        }
    }
    
    /**
     * Handles a method invocation.  This will first check to see if there is a matching
     * Java method in this class (method requires the @Msg annotation), and call that 
     * method if it is available.  Otherwise it will obtain the method implementation from
     * the parent class and execute it.  The return value is added to the NSInvocation object.
     * 
     * This method is used by the Native WLProxy to pipe all messages to this object's peer
     * through Java so that it has a chance to process it.
     * @param invocation NSInvocation Objective-C object that is to be invoked.
     * 
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSInvocation_Class/Reference/Reference.html">NSInvocation Class Reference</a>
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSProxy_Class/Reference/Reference.html">NSProxy forwardInvocation Documentation</a>
     */
    public void forwardInvocation(Pointer invocation){
        forwardInvocation(PointerTool.getPeer(invocation));
    }
    
    
    /**
     * Handles a method invocation.  This will first check to see if there is a matching
     * Java method in this class (method requires the @Msg annotation), and call that 
     * method if it is available.  Otherwise it will obtain the method implementation from
     * the parent class and execute it.  The return value is added to the NSInvocation object.
     * 
     * This method is used by the Native WLProxy to pipe all messages to this object's peer
     * through Java so that it has a chance to process it.
     * @param invocation NSInvocation Objective-C object that is to be invoked.
     * 
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSInvocation_Class/Reference/Reference.html">NSInvocation Class Reference</a>
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSProxy_Class/Reference/Reference.html">NSProxy forwardInvocation Documentation</a>
     */
    @Override
    public void forwardInvocation(long linvocation) {
        Pointer invocation = new Pointer(linvocation);
        
        Client rawClient = Client.getRawClient();
        
        
        Pointer sig = msgPointer(invocation, "methodSignature");
        Proxy pSig = new Proxy(rawClient, sig);
        Pointer selector = msgPointer(invocation, "selector");
        long numArgs = (Long)pSig.send("numberOfArguments");
        
        Method method = methodForSelector(selName(selector));
        if ( method != null){
            
            Msg message = (Msg)method.getAnnotation(Msg.class);
            if ( true ||  !"".equals(message.signature()) ){
                // Perform the method and provide the correct output for the invocation
               

                Object[] args = new Object[new Long(numArgs).intValue()-2];
                for ( int i=2; i<numArgs; i++){
                    
                    long argumentSigAddr = (Long)pSig.send("getArgumentTypeAtIndex:", i);
                    String argumentSignature = new Pointer(argumentSigAddr).getString(0);
                   
                    if ( "fd".indexOf(argumentSignature.substring(0,1)) != -1 ){
                        DoubleByReference ptrRef = new DoubleByReference();
                        
                        msg(invocation, "getArgument:atIndex:", ptrRef.getPointer(), i);
                        
                        args[i-2] = TypeMapper
                                    .getInstance()
                                    .cToJ(
                                        ptrRef.getValue(),
                                        //argPtr.toNative(),
                                        argumentSignature, 
                                        TypeMapper.getInstance()
                                );
                    } else {
                        LongByReference ptrRef = new LongByReference();
                        
                        msg(invocation, "getArgument:atIndex:", ptrRef.getPointer(), i);
                       
                        args[i-2] = TypeMapper
                                    .getInstance()
                                    .cToJ(
                                        ptrRef.getValue(),
                                        //argPtr.toNative(),
                                        argumentSignature, 
                                        TypeMapper.getInstance()
                                );
                    }
                   
                    
                  
                }
               
                try {
                    method.setAccessible(true);
                    Object res = method.invoke(this, args);
                    
                    // We should release the arguments now since we retained them before
                    // to prevent memory leaks.
                    for ( int i=0; i<args.length; i++){
                        Proxy.release(args[i]);
                    }
                    
                    long returnType = (Long)pSig.send("methodReturnType");
                    
                    String strReturnType = new Pointer(returnType).getString(0);
                    
                   
                    res = TypeMapper
                            .getInstance()
                            .jToC(res, strReturnType, TypeMapper.getInstance());
                    
                    if ( !"v".equals(strReturnType)){
                       
                        Object retVal = res == null ? new PointerByReference(Pointer.NULL).getPointer() : RuntimeUtils.getAsReference(res, strReturnType);
                        msg(invocation, "setReturnValue:",  retVal);
                    }
                    
                    return;
                } catch (Exception ex){
                    ex.printStackTrace(System.err);
                    throw new RuntimeException(ex);
                }

            }
        }
        // If we send using invokeWithTarget, then we will use the method of the parent
        // and set "self" to the parent for the method call.  We want the parent's 
        // method, but we want to use ourself as the "self".
        

        this.forwardInvocationToParent(invocation);
    }

    /**
     * Checks whether this object responds to the given selector.  This is used 
     * by the WLProxy (Objective-C peer object) to route requests for its NSProxy
     * respondsToSelector: message.  This will check to see if there is a registered
     * java method in the class that responds to the selector (based on the @Msg 
     * annotation).  Then it will check the parent object to see if it responds
     * to the selector.
     * @param selector Pointer to the selector to check.
     * 
     * @return True if either the java class or the parent Objective-c object 
     * responds to the specified selector.
     * 
     * @see RuntimeUtils.sel()
     * @see <a href="http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/objectivec/Chapters/ocSelectors.html">Objective-C selectors reference</a>
     */
    public boolean respondsToSelector(Pointer selector){
        return respondsToSelector(PointerTool.getPeer(selector));
    }
    
    
    /**
     * Checks whether this object responds to the given selector.  This is used 
     * by the WLProxy (Objective-C peer object) to route requests for its NSProxy
     * respondsToSelector: message.  This will check to see if there is a registered
     * java method in the class that responds to the selector (based on the @Msg 
     * annotation).  Then it will check the parent object to see if it responds
     * to the selector.
     * @param selector Pointer to the selector to check.
     * 
     * @return True if either the java class or the parent Objective-c object 
     * responds to the specified selector.
     * 
     * @see RuntimeUtils.sel()
     * @see <a href="http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/objectivec/Chapters/ocSelectors.html">Objective-C selectors reference</a>
     */
    @Override
    public boolean respondsToSelector(long lselector) {
        
        Pointer selector = new Pointer(lselector);
        Method method = methodForSelector(selName(selector));
        if ( method != null){
            
            return true;
            
        }
        return (msg(parent, "respondsToSelector:", selector ) > 0);
        
    }
    
    /**
     * @deprecated
     * @param selector
     * @param args
     * @return
     */
    @Override
    public NSObject chain(Pointer selector, Object... args){
        return (NSObject)super.chain(selector, args);
    }
    
    /**
     * @deprecated
     * @param selector
     * @param args
     * @return
     */
    @Override
    public NSObject chain(String selector, Object... args){
        return (NSObject)super.chain(selector, args);
    }
    
    /**
     * @deprecated
     * @param msgs
     * @return
     */
    @Override
    public NSObject chain(Message... msgs){
        return (NSObject)super.chain(msgs);
    }


    /**
     * @deprecated
     * @return 
     */
    public NSObject dealloc(){
        
        this.send("dealloc");
        
        
        return this;
        
    }
    
    

    
}
