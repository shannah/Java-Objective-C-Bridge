package ca.weblite.objc;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jna.Pointer;

/**
 *
 * @author shannah
 */
public class RuntimeTest {

    /**
     * Test of objc_lookUpClass method, of class Runtime.
     */
    @Test
    public void testObjc_lookUpClass() {
        // Load NSString class
        Pointer nsString = Runtime.INSTANCE.objc_lookUpClass("NSString");
        
        // Get the name of the class that we just loaded (should be NSString)
        String clsName = Runtime.INSTANCE.class_getName(nsString);
        
        // Assert that the class name is what we expect
        assertEquals("NSString", clsName);
        
        // Get the UID for the stringWithUTF8String: selector
        Pointer strWithUTF8StringSelector = Runtime.INSTANCE
                .sel_getUid("stringWithUTF8String:");
        
        
        // Get the name of the selector from its UID
        String selName = Runtime.INSTANCE.sel_getName(strWithUTF8StringSelector);
        
        // Assert that the selector name is what we expect
        assertEquals("stringWithUTF8String:", selName);
        
        
        // Create a new string with the stringWithUTF8String: message
        // We are sending the message directly to the NSString class.
        long string = Runtime.INSTANCE
                .objc_msgSend(nsString, strWithUTF8StringSelector, "Test String");
        
        // Now that we have our string let's send a message to it
        Pointer utf8StringSelector = Runtime.INSTANCE.sel_getUid("UTF8String");
        
        // objc_msgSend takes a pointer, not a long so we need to wrap our string
        // inside a com.sun.jna.Pointer object
        Pointer stringPtr = new Pointer(string);
        
        
        
        long outStringPtr = Runtime.INSTANCE.objc_msgSend(stringPtr, utf8StringSelector);
        
        //outStringPtr is a pointer to a CString, so let's convert it into 
        // a Java string so we can check to make sure it matches what
        // we expect
        
        String outString = new Pointer(outStringPtr).getString(0);
        assertEquals("Test String", outString);
        
        
    }

    
}
