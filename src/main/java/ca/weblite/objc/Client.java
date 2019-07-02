/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;
import static ca.weblite.objc.RuntimeUtils.*;
import com.sun.jna.Pointer;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>An object-oriented wrapper around the RuntimeUtils static methods for
 * interacting with the Objective-C runtime.</p>
 * 
 * <p>A client object stores settings about whether to coerce inputs and/or
 * outputs from messages.  There are two global instances of this class
 * that are used most often:</p>
 * <ol>
 *  <li><strong><code>Client.getInstance()</code></strong> : The default 
 *      client with settings to coerce both inputs and outputs.  If you
 *      need to make a direct call to the Objective-C runtime, this 
 *      is usually the client that you would use.
 *  </li>
 *  <li><strong><code>Client.getRawClient()</code></strong> : Reference
 *      to a simple client that is set to <em>not</em> coerce input and 
 *      output. This is handy if you want to pass in the raw Pointers
 *      as parameters, and receive raw pointers as output.
 *  </li>
 * </ol>
 * @author shannah
 */
public class Client {
    
    /**
     * Reference to the default client instance with type coercion enabled
     * for both inputs and outputs.
     */
    private static Client instance;
    
    /**
     * Retrieves the global reference to a client that has both input coercion
     * and output coercion enabled.
     * @return Singleton instance.
     */
    public static Client getInstance(){
        if ( instance == null ){
            instance = new Client();
        }
        return instance;
    }
    
    /**
     * Reference to a simple client that has type coercion disabled for both
     * inputs and outputs.
     */
    private static Client rawClient;
    
    /**
     * Retrieves singleton instance to a simple client that has type coercion 
     * disabled for both inputs and outputs.
     * @return 
     */
    public static Client getRawClient(){
        if ( rawClient == null ){
            rawClient = new Client();
            rawClient.coerceInputs = false;
            rawClient.coerceOutputs = false;
        }
        return rawClient;
    }
    
    /**
     * Flag to indicate whether inputs to messages should be coerced.  If
     * this flag is true, than any Java inputs will be converted to their
     * corresponding C types using the TypeMapper class and its corresponding
     * TypeMapping subclasses.
     * 
     */
    boolean coerceInputs=true;
    
    /**
     * Flag to indicate whether the output of messages should be coerced. If
     * this flag is true, then any C outputs from messages will be converted
     * to their corresponding Java types using the TypeMapper class.
     */
    boolean coerceOutputs=true;
    
    
  
    /**
     * Set the coerceInputs flag.  Setting this to true will cause all subsequent
     * requests to coerce the input (i.e. convert Java parameters to corresponding
     * C-types).
     * 
     * 
     * @param coerceInputs Whether to coerce inputs to messages.
     * @return Self for chaining.
     * 
     * @see TypeMapper
     * @see TypeMapping
     */
    public Client setCoerceInputs(boolean coerceInputs){
        this.coerceInputs = coerceInputs;
        return this;
    }
    
    /**
     * Sets the coerceOutputs flag.  Setting this to true will cause all subsequent
     * requests to coerce the output (i.e.convert C return values to corresponding
     * Java types.
     * @param coerceOutputs Whether to coerce the outputs of messages.
     * @return Self for chaining.
     * 
     * @see TypeMapper
     * @see TypeMapping
     */
    public Client setCoerceOutputs(boolean coerceOutputs){
        this.coerceOutputs = coerceOutputs;
        return this;
    }
    
    
    /**
     * Returns the coerceInputs flag.  If this returns true, then it means that
     * the client is currently set to coerce all inputs to messages.
     * 
     * @return True if input coercion is enabled.
     * 
     * @see TypeMapper
     * @see TypeMapping
     */
    public boolean getCoerceInputs(){
        return coerceInputs;
    }
    
    /**
     * Returns the coerceOutputs flag.  If this returns true, then it means that
     * the client is currently set to coerce all outputs of messages.
     * 
     * @return True if output coercion is enabled.
     * 
     * @see TypeMapper
     * @see TypeMapping
     */
    public boolean getCoerceOutputs(){
        return coerceOutputs;
    }
    
    /**
     * Sends a message to an Objective-C object.
     * <pre>
     * {@code
     * String hello = (String)Client.getInstance()
     *      .send(cls("NSString"), sel("stringWithUTF8String:"), "Hello");
     * }
     * </pre>
     * @param receiver A pointer to the object to which the message is being sent.
     * @param selector A pointer to the selector to call on the target.
     * @param args Variable arguments of the message.
     * @return The return value of the message call.
     */
    public Object send(Pointer receiver, Pointer selector, Object... args){
        return msg(coerceOutputs, coerceInputs,receiver, selector, args);
    }
    
    /**
     * Sends a message to an Objective-C object.  String selector version.
     * 
     * <pre>
     * {@code
     * String hello = (String)Client.getInstance()
     *      .send(cls("NSString"), "stringWithUTF8String:", "Hello");
     * }
     * </pre>
     * @param receiver A pointer to the object to which the message is being sent.
     * @param selector The string selector.  (E.g. "addObject:atIndex:")
     * @param args Variable arguments of the message.
     * @return The return value of the message call.
     */
    public Object send(Pointer receiver, String selector, Object... args){
        return send(receiver, sel(selector), args);
    }
   
    /**
     * Sends a message to an Objective-C object.  String target and Pointer
     * selector version.  Typically this variant is used when you need to 
     * call a class method (e.g. to instantiate a new object).  In this case
     * the receiver is interpreted as a class name.
     * <pre>
     * {@code
     * String hello = (String)Client.getInstance()
     *      .send("NSString", sel("stringWithUTF8String:"), "Hello");
     * }
     * </pre>
     * 
     * @param receiver The name of a class to send the message to.
     * @param selector Pointer to the selector.
     * @param args Variable arguments of the message.
     * @return The return value of the message call.
     */
    public Object send(String receiver, Pointer selector, Object... args){
        return send(cls(receiver), selector, args);
    }
    
    /**
     * Sends a message to an Objective-C object.  String target and String
     * selector version.  Typically this variant is used when you need to 
     * call a class method (e.g. to instantiate a new object).  In this case
     * the receiver is interpreted as a class name.
     * <pre>
     * {@code
     * String hello = (String)Client.getInstance()
     *      .send("NSString", "stringWithUTF8String:", "Hello");
     * }
     * </pre>
     * 
     * @param receiver The name of a class to send the message to.
     * @param selector Pointer to the selector.
     * @param args Variable arguments of the message.
     * @return The return value of the message call.
     */
    public Object send(String receiver, String selector, Object... args){
        return send(cls(receiver), sel(selector), args);
    }
    
    
    /**
     * Sends a message to an Objective-C object.  Peerable target/Pointer selector
     * variant.  This variant is used if you have a Peerable object that is
     * wrapping the Object pointer.  E.g. Both the Proxy class and NSObject
     * class implement this interface. 
     * <pre>
     * {@code
     * Proxy array = Client.getInstance().sendProxy("NSMutableArray", sel("array"));
     * String hello = (String)Client
     *      .getInstance().send(array, sel("addObject:atIndex"), "Hello", 2);
     * }
     * </pre>
     * 
     * @param receiver The object to which we are sending the message.
     * @param selector Pointer to the selector.
     * @param args Variable arguments of the message.
     * @return The return value of the message call.
     */
    public Object send(Peerable proxy, Pointer selector, Object... args){
        return send(proxy.getPeer(), selector, args);
    }
    
    /**
     * Sends a message to an Objective-C object.  Peerable target/String selector
     * variant.  This variant is used if you have a Peerable object that is
     * wrapping the Object pointer.  E.g. Both the Proxy class and NSObject
     * class implement this interface. 
     * <pre>
     * {@code
     * Proxy array = Client.getInstance().sendProxy("NSMutableArray", "array");
     * String hello = (String)Client
     *      .getInstance().send(array, "addObject:atIndex", "Hello", 2);
     * }
     * </pre>
     * 
     * @param receiver The object to which we are sending the message.
     * @param selector Pointer to the selector.
     * @param args Variable arguments of the message.
     * @return The return value of the message call.
     */
    public Object send(Peerable proxy, String selector, Object... args){
        return send(proxy.getPeer(), sel(selector), args);
    }
    
    
    /**
     * A wrapper around send() to ensure that a pointer is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A pointer result from the message invocation.
     */
    public Pointer sendPointer(Pointer receiver, Pointer selector, Object... args){
        //System.out.println("In sendPointer for "+selName(selector));
        Object res = send(receiver, selector, args);
        if ( Pointer.class.isInstance(res)){
            return (Pointer)res;
        } else if ( Proxy.class.isInstance(res)){
            return ((Proxy)res).getPeer();
        } else if ( long.class.isInstance(res) || Long.class.isInstance(res)){
            return new Pointer((Long)res);
        } else {
            return (Pointer)res;
        }
        
    }
    
    /**
     * A wrapper around send() to ensure that a pointer is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A pointer result from the message invocation.
     */
    public Pointer sendPointer(Pointer receiver, String selector, Object... args){
        return sendPointer(receiver, sel(selector), args);
    }
    
    
    /**
     * A wrapper around send() to ensure that a pointer is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A pointer result from the message invocation.
     */
    public Pointer sendPointer(String receiver, Pointer selector, Object... args){
        return sendPointer(cls(receiver), selector, args);
    }
    
    
    /**
     * A wrapper around send() to ensure that a pointer is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A pointer result from the message invocation.
     */
    public Pointer sendPointer(String receiver, String selector, Object... args){
        return sendPointer(cls(receiver), sel(selector), args);
    }
    
    
    /**
     * A wrapper around send() to ensure that a Proxy object is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A Proxy object wrapper of the result from the message invocation.
     */
    public Proxy sendProxy(Pointer receiver, Pointer selector, Object... args){
        return (Proxy)send(receiver, selector, args);
    }
    
    /**
     * A wrapper around send() to ensure that a Proxy object is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A Proxy object wrapper of the result from the message invocation.
     */
    public Proxy sendProxy(String receiver, Pointer selector, Object... args){
        return sendProxy(cls(receiver), selector, args);
    }
    
    
    /**
     * A wrapper around send() to ensure that a Proxy object is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A Proxy object wrapper of the result from the message invocation.
     */
    public Proxy sendProxy(String receiver, String selector, Object... args){
        return sendProxy(cls(receiver), sel(selector), args);
    }
    
    
    /**
     * A wrapper around send() to ensure that a Proxy object is returned from the 
     * message.  
     * @param receiver The object to which the message is being sent.
     * @param selector The selector to call on the receiver.
     * @param args Variable arguments to the message.
     * @return A Proxy object wrapper of the result from the message invocation.
     */
    public Proxy sendProxy(Pointer receiver, String selector, Object... args){
        return sendProxy(receiver, sel(selector), args);
    }
    
    public Proxy chain(String cls, Pointer selector, Object... args){
        Pointer res = Client.getRawClient().sendPointer(cls, selector, args);
        return new Proxy(res);
        
    }
    
    /**
     * @deprecated
     * @param cls
     * @param selector
     * @param args
     * @return 
     */
    public Proxy chain(String cls, String selector, Object... args){
        return chain(cls, sel(selector), args);
    }
    
    
    /**
     * Creates a new peerable and receivable object of the specified class.
     * This will create the Objective-C peer and link it to this new class.
     * @param cls The class of the instance that should be created.
     * @return A PeerableRecipient object that is connected to an objective-c
     * peer object.
     */
    public PeerableRecipient newObject(Class<? extends PeerableRecipient> cls){
        try {
            PeerableRecipient instance = cls.newInstance();
            if ( instance.getPeer() == Pointer.NULL){
                Pointer peer = new Pointer(createProxy(instance));
                instance.setPeer(peer);
                
            }
            return instance;
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
        
    }
    
    /**
     * Sends an array of messages in a chain.
     * @param messages
     * @return 
     */
    public Object send(Message... messages){
        return RuntimeUtils.msg(messages);
    }
    
   
    /**
     * Builds a chain of messages that can be executed together at a later time.
     * @param parameters
     * @return 
     */
    public Message[] buildMessageChain(Object... parameters){
        List<Message> messages = new ArrayList<Message>();
        
       
        for (int i=0; i<parameters.length; i++){
            
            Message buffer = new Message();
            buffer.coerceInput = this.coerceInputs;
            buffer.coerceOutput = this.coerceOutputs;
            if ( String.class.isInstance(parameters[i])){
                if ( "_".equals(parameters[i])){
                    buffer.receiver = Pointer.NULL;
                } else {
                    buffer.receiver = cls((String)parameters[i]);
                }
            } else if ( Peerable.class.isInstance(parameters[i])){
                buffer.receiver = ((Peerable)parameters[i]).getPeer();
            } else {
                buffer.receiver = (Pointer)parameters[i];
            }
            i++;
            if ( String.class.isInstance(parameters[i])){
                buffer.selector = sel((String)parameters[i]);
            } else if ( Peerable.class.isInstance(parameters[i])){
                buffer.selector = ((Peerable)parameters[i]).getPeer();

            } else {
                buffer.selector = (Pointer)parameters[i];
            }
            i++;
            while ( i<parameters.length && parameters[i] != null ){
                buffer.args.add(parameters[i++]);

            }
            messages.add(buffer);

                
            
            
            
        }
        
        return messages.toArray(new Message[messages.size()]);
    }
    
    
}
