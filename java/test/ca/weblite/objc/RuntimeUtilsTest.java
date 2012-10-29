/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import com.sun.jna.Pointer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static ca.weblite.objc.RuntimeUtils.*;

/**
 *
 * @author shannah
 */
public class RuntimeUtilsTest {
    
    public RuntimeUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of objc_lookUpClass method, of class Runtime.
     */
    @Test
    public void testObjc_lookUpClass() {
        // Load NSString class
        Pointer nsString = cls("NSString");
        
        // Get the name of the class that we just loaded (should be NSString)
        String clsName = clsName(nsString);
        
        // Assert that the class name is what we expect
        assertEquals("NSString", clsName);
        
        // Get the UID for the stringWithUTF8String: selector
        Pointer strWithUTF8StringSelector = sel("stringWithUTF8String:");
        
        
        // Get the name of the selector from its UID
        String selName = selName(strWithUTF8StringSelector);
        
        // Assert that the selector name is what we expect
        assertEquals("stringWithUTF8String:", selName);
        
        
        // Create a new string with the stringWithUTF8String: message
        // We are sending the message directly to the NSString class.
        long string = msg(nsString, strWithUTF8StringSelector, "Test String");
        
        // Now that we have our string let's send a message to it
        Pointer utf8StringSelector = sel("UTF8String");
        
        // objc_msgSend takes a pointer, not a long so we need to wrap our string
        // inside a com.sun.jna.Pointer object
        Pointer stringPtr = new Pointer(string);
        
        
        
        long outStringPtr = msg(stringPtr, utf8StringSelector);
        
        //outStringPtr is a pointer to a CString, so let's convert it into 
        // a Java string so we can check to make sure it matches what
        // we expect
        
        String outString = new Pointer(outStringPtr).getString(0);
        assertEquals("Test String", outString);
        
        
    }
    
    @Test
    public void testObjc_lookUpClass2() {
        
        
        // Create a new string with the stringWithUTF8String: message
        // We are sending the message directly to the NSString class.
        long string = msg("NSString", "stringWithUTF8String:", "Test String");
        
        long outStringPtr = msg(new Pointer(string), "UTF8String");
        
        //outStringPtr is a pointer to a CString, so let's convert it into 
        // a Java string so we can check to make sure it matches what
        // we expect
        
        String outString = new Pointer(outStringPtr).getString(0);
        assertEquals("Test String", outString);
        
        
    }
    
    @Test
    public void testObjc_lookUpClass3() {
        
        
        // Create a new string with the stringWithUTF8String: message
        // We are sending the message directly to the NSString class.
        // Because we are asking to coerce outputs, this will simply
        // map the resulting NSString object to a Java string so 
        // we end where we started.
        String outString = (String)msg(
                true,   // Coerce Outputs
                true,   // Coerce Inputs
                cls("NSString"), //Receiver
                sel("stringWithUTF8String:"), // Selector
                "Test String"   // Argument to message
        );
        
        assertEquals("Test String", outString);
        
        
        // Same thing without coercing outputs.  We'll instead receive
        // a pointer to an NSString.
        long nsString = (long)msg(
                false,
                true,
                cls("NSString"),
                sel("stringWithUTF8String:"),
                "Test String"
        );
        
        // Confirm that this is a pointer to an NSString
        long res = (long)msg(new Pointer(nsString), "isKindOfClass:", cls("NSString"));
        assertEquals(1L, res);
        
        
        // Now let's get the string as a Java string.
        outString = (String)msg(
                true,
                true,
                new Pointer(nsString),
                sel("UTF8String")
        );
        
        assertEquals("Test String", outString);
        
        // Now create an NSMutableArray to show the msg() method
        // automatically wrap it in a Proxy object
        
        Proxy array = (Proxy)msg(
                true,
                true,
                cls("NSMutableArray"),
                sel("array")
                );
        
        
        // Add out test string to the array
        // This will convert it to an NSString
        // and store it in the array.
        array.send("addObject:", outString);
        
        
        // Use the Proxy's sendInt() method to send a message to count
        // in which we expect an int as output
        int sizeOfArray = array.sendInt("count");
        assertEquals(1, sizeOfArray);
        
        
        // The Proxy object has type mapping enabled, so we can send
        // directly and have it return a string.
        String firstItem = array.sendString("objectAtIndex:", 0);
        assertEquals(outString, firstItem);
        
        
        
    }
    
    

    
}
