package ca.weblite.objc;

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
public class ClientTest {
    
    public ClientTest() {
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
