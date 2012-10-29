/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import com.sun.jna.Pointer;
import java.util.ArrayList;
import java.util.List;

/**
 * A structure the encapsulates a message.  This is an optional alternative
 * way of sending messages to the Objective-C runtime.
 * 
 * @see Client.send(Message...)
 * @see RuntimeUtils.msg(Message...)
 * @see Proxy.send(Message...)
 * @author shannah
 */
public class Message {
    
    /**
     * Status identifier of a message to indicate that it has been skipped.
     * 
     */
    public static final int STATUS_SKIPPED=1;
    
    /**
     * Status identifier of a message to indicate that is has been cancelled.
     */
    public static final int STATUS_CANCELLED=2;
    
    /**
     * Status identifier of a message to indicated that it has been completed.
     */
    public static final int STATUS_COMPLETED=3;
    
    /**
     * Status identifier of a message to indicate that it is ready to be sent.
     */
    public static final int STATUS_READY=0;
    
    /**
     * The target of the message.
     */
    public Pointer receiver;
    
    /**
     * The selector of the message.
     */
    public Pointer selector;
    
    /**
     * List of arguments to pass to the method invocation.
     */
    public List args = new ArrayList();
    
    /**
     * Placeholder for the result of the message. (i.e. return value).
     */
    public Object result;
    
    /**
     * If there was en error in the message handling, the error will be saved
     * here.
     */
    public Exception error;
    
    /**
     * The current status of the message.  Before running, its status should
     * be STATUS_READY, and after running, it should be STATUS_COMPLETED.  If,
     * for some reason it has been cancelled or skipped, then it could have
     * those statuses also.
     */
    public int status = 0;
    
    public boolean 
            /**
             * Whether to coerce the input of the message.
             */
            coerceInput, 
            /**
             * Whether to coerce the output of the message.
             */
            coerceOutput;
    
    
    public boolean 
            /**
             * Whether the input was, in fact coerced (set when the message
             * is run).
             */
            inputWasCoerced, 
            /**
             * Whether the output was, in fact, coerced.  Set when the message
             * is run.
             */
            outputWasCoerced;
    
    public Message 
            /**
             * Reference to the next message in the message chain.
             */
            next, 
            /**
             * Reference to the previous message in the message chain.
             */
            previous;
    
    
    /**
     * Method that is called just before the message is sent.  This can be 
     * overridden to change the parameters, skip the message, or cancel the message
     * chain altogether.
     */
    public void beforeRequest(){
        
    }
    
    /**
     * Method that is called just after the message is send and response received.
     * This can be overridden to do post processing, like changing the settings
     * of subsequent messages in the chain or doing processing based on the 
     * output of the message.
     */
    public void afterResponse(){
        
    }
    
    
}
