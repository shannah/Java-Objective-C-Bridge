/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;


/**
 * An interface for an object that can receive messages from the Objective-C
 * runtime.  In order to receive messages, the object should be passed to the 
 * RuntimeUtils.createProxy() method.
 * 
 * <p>The NSObject class is a concrete implementation of this interface that
 * contains all of the plumbing necessary to operate in the world of the 
 * Objective-C runtime.  It is probably best to just subclass NSObject rather
 * than implement your own Recipient class.</p>
 * @author shannah
 * @see NSObject
 * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSProxy_Class/Reference/Reference.html#//apple_ref/doc/uid/TP40003719">NSProxy Class Reference</a>
 */
public interface Recipient {
    
    /**
     * Returns the method signature for a specified selector.
     * @param selector The pointer to the selector to check.
     * @return Pointer to the NSMethodSignature object for the specified selector.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSMethodSignature_Class/Reference/Reference.html#//apple_ref/doc/uid/TP40003685">NSMethodSignature Class Reference</a>
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSProxy_Class/Reference/Reference.html#//apple_ref/doc/uid/TP40003719">forwardInvocation: Method reference (from NSProxy)</a>
     */
    public long methodSignatureForSelector(long selector);
    
    /**
     * Handles the invocation of a method on the recipient.  Typically this should
     * either be handled by a java method, or routed to some parent object that
     * is being proxied.  
     * @param invocation The NSInvocation object.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSInvocation_Class/Reference/Reference.html#//apple_ref/doc/uid/TP40003671">NSInvocation Class Reference</a>
     * @see NSObject.methodSignatureForSelector() For a concrete imlementation.
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/Foundation/Classes/NSProxy_Class/Reference/Reference.html#//apple_ref/doc/uid/TP40003719">forwardInvocation: Method reference (from NSProxy)</a>
     */
    public void forwardInvocation(long invocation);
    
    /**
     * Checks to see if this object responds to the specified selector.
     * @param selector
     * @return True if the object responds to the specified selector.
     */
    public boolean respondsToSelector(long selector);
    
}
