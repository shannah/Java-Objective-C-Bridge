/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that allows a Java method to receive and process Objective-C 
 * messages.  This annotation is only useful for subclasses of ca.weblite.objc.NSObject.
 * 
 * 
 * <h3>Example Class Using Annotation TO Handle Objective-C messages</h3>
 * 
 * <script src="https://gist.github.com/3969983.js?file=TestCustomClass.java"></script>
 * 
 * <p>The following is some test code that uses the TestClass.  Notice that this
 * class can be interacted with the same as if it was an Objective-C object. Indeed,
 * Objective-C can call any of the methods marked by the {@literal @}Msg annotation
 * by simply sending it the appropriate message.</p>
 * 
 * <script src="https://gist.github.com/3970000.js?file=TestMsg.java"></script>
 * 
 * @author shannah
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Msg {
    
    /**
     * The name of the objective-c selector that can be used to target this method.
     * Selectors should be the full method name, including colons.  If you're overriding
     * the NSSavePanel's -setTitle: message, then the selector would be "setTitle:".
     * The -title message, on the other hand would just be "title" (without colon) 
     * because it takes no parameters.
     * @return 
     */
    String selector();
    
    /**
     * The signature of this message. This should be a valid Objective-C type 
     * encoding.  If this parameter is omitted, then you can optionally use 
     * the like() directive specify that the signature should be the same
     * as another Class' method.
     * 
     * @return The signature of the message.
     * 
     *
     * 
     * <h4>Example Signatures</h4>
     * <table>
     * <thead>
     *  <tr>
     *      <th>Method Type</th>
     *      <th>Signature</th>
     *      <th>Explanation</th>
     *  </tr>
     * </thead>
     * <tbody>
     *  <tr>
     *      <td>Returns NSString, takes no parameters</td>
     *      <td>{@literal @@}:</td>
     *      <td>The first "{@literal @}" indicates the return value, which is an Object,
     *          The second "{@literal @}" is a mandatory hidden parameter that refers
     *          to the receiver of the message.  The ":" is a hidden parameter
     *          that refers to the selector of the message.
     *      </td>
     *  </tr>
     *  <tr>
     *      <td>Returns void, takes an NSString as a parameter</td>
     *      <td>{@literal v@:@}</td>
     *      <td>The "v" indicates that there is no return value (i.e. void).  
     *          The first "{@literal @}" marks the hidden parameter that is the target
     *          of the message.  The ":" marks the hidden selector.  The final
     *          "{@literal @}" indicates that it takes an object as a parameter (specifically
     *          an NSString, but the signature doesn't distinguish.
     *      </td>
     *  </tr>
     * <tr>
     *      <td>Returns void, takes an C String (i.e. *char) as a parameter</td>
     *      <td>{@literal v@:*}</td>
     *      <td>The "v" indicates that there is no return value (i.e. void).  
     *          The first "{@literal @}" marks the hidden parameter that is the target
     *          of the message.  The ":" marks the hidden selector.  The final
     *          "{@literal *}" indicates that it takes a C String as the first parameter.
     *      </td>
     *  </tr>
     * </tbody>
     * </table>
     * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html">Objective-C Type Encodings</a>
     */
    String signature() default "";
    
    /**
     * Specifies that the signature should match that of another object's method.
     * This can make it easier for you to define methods without having to know
     * how to formulate the signature.
     * 
     * <p>E.g., if you have a method that takes no input, and returns an int,
     * you might just say that your method is like the NSArray.count selector:
     * {@code 
     * {@literal @}Msg(selector="myIntMessage", like="NSArray.count")
     * }
     * </p>
     * 
     * @return The name of a method whose signature that this message should 
     * copy.
     */
    String like() default "";
}
