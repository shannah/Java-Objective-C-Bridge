package ca.weblite.objc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 *
 * @author shannah
 */
public class ClientTest {
    /**
     * Test of objc_lookUpClass method, of class Runtime.
     */
    @Test
    public void testObjc_lookUpClass() {
        
        // Obtain reference to Singleton instance of Objective-C client
        Client c = Client.getInstance();
        
        // Create a new mutable array
        Proxy array = c.sendProxy("NSMutableArray", "array");
        array.send("addObject:", "Hello");
        array.send("addObject:", "World");
        array.send("addObject:", "Test String");
        
        assertEquals(3, array.sendInt("count"));
        
        String lastString = array.sendString("lastObject");
        assertEquals("Test String", lastString);
    }
    
}
