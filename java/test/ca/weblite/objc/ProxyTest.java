/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static ca.weblite.objc.RuntimeUtils.*;
import ca.weblite.objc.annotations.Msg;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author shannah
 */
public class ProxyTest {
    
    public static class NSRange extends Structure {
        
        public static class ByReference extends NSRange implements Structure.ByReference{}
        public static class ByValue extends NSRange implements Structure.ByValue{}
        public long location;
        public int length;
       

        @Override
        protected List getFieldOrder() {
            return Arrays.asList(new String[]{"location","length"});
        }
    }
    
    
    public ProxyTest() {
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
     * Test of load method, of class Proxy.
     */
    @Test
    public void testNSArray() {
        
        Proxy o = new Proxy(msgPointer("NSMutableArray", "array"));
        long expectedCount = 0;
        long actualCount = (long)o.send("count");
        assertEquals(expectedCount, actualCount);
        
        // Add a string to the array and check that
        // the last object matches that string.
        // Strings should be automatically converted to NSStrings
        String aString = "foobar";
        o.send("addObject:", aString);
        
        String obj = (String)o.send("lastObject");
        assertEquals(aString, obj);
        
        // There should be one object in the array
        expectedCount = 1;
        actualCount = (long)o.send("count");
        assertEquals(expectedCount, actualCount);
        
        //Now the string is there
        boolean expectedContains = true;
        boolean actualContains = o.sendBoolean("containsObject:", aString);
        assertEquals(expectedContains, actualContains);
        
        // Let's try to call a method that takes a structure as one of the 
        // parameters
        Pointer[] buffer = new Pointer[1];
        NSRange range = new NSRange.ByValue();
        range.length=1;
        range.location=0;
        o.send("getObjects:range:", buffer, range);
        
        assertEquals(1, buffer.length);
        
        // Make sure that the first (and only entry) in the
        // buffer is the same string that we added previously.
        assertEquals(aString, str(buffer[0]));
        
        Proxy enumerator = o.sendProxy("objectEnumerator");
        
        String placeHolder = (String)enumerator.send("nextObject");
        assertEquals(aString, placeHolder);    
        
        Proxy newArray = o.sendProxy("arrayByAddingObject:", "Another String");
        
        assertEquals(2, newArray.sendInt("count"));
        
        
        
    }
    
    

    
}
