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
public class NSObjectTest {
    
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
    
    
    public NSObjectTest() {
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
        
        NSObject o = new NSObject(msgPointer("NSMutableArray", "array"));
        o.init("NSObject");
        
       
        long expectedCount = 0;
        long actualCount = (Long)o.send("count");
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
        actualCount = (Long)o.send("count");
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
        //System.out.println(newArray);
        assertEquals(2, newArray.sendInt("count"));
        
        
        
    }
    
    @Test
    public void testCustomClass(){
        TestCustomClass cls = new TestCustomClass();
        cls.init("NSObject");
        
        String res = (String)cls.send("myCustomString");
        assertEquals("My custom string", res);
        System.out.println("Before");
        cls.send("setMyCustomString:", "Changed String");
        System.out.println("Here now");
        res = (String)cls.send("myCustomString");
        assertEquals("Changed String", res);
        
        int iRes = cls.sendInt("getCustomInt");
        assertEquals(34, iRes);
        
        cls.send("setCustomInt:", 12);
        assertEquals(12, cls.sendInt("getCustomInt"));
        
        double dRes = cls.sendDouble("getMyDouble");
        System.out.println("The double val is "+dRes);
        
        assertTrue(1.5==dRes);
        
        cls.send("setMyDouble:", 3.0);
        dRes = cls.sendDouble("getMyDouble");
        //System.out.println("Double is now "+dRes);
        //System.out.println("Double is (in java) "+cls.dNum);
        assertTrue(3.0==dRes);
        
        Proxy myObj = (Proxy)cls.send("getMyObj");
        assertEquals(null, myObj);
        
        Proxy array = Client.getInstance().sendProxy("NSMutableArray", "array");
        cls.send("setMyObj:", array);
        
        myObj = cls.sendProxy("getMyObj");
        assertEquals(array, myObj);
        
        
        
    }
    
    public static class TestCustomClass extends NSObject {
        private String str = "My custom string";
        
        private int iNum = 34;
        private double dNum = 1.5;
        private Proxy myObj;
        
        @Msg(selector="myCustomString", like="NSObject.description")
        public String myCustomString(){
            return str;
        }
        
        @Msg(selector="setMyCustomString:", signature="v@:@")
        public void setMyCustomString(String str){
            this.str = str;
        }
        
        @Msg(selector="getCustomInt", signature="i@:")
        public int getCustomInt(){
            return iNum;
        }
        @Msg(selector="setCustomInt:", signature="v@:i")
        public void setCustomInt(int i){
            this.iNum = i;
        }
        
        @Msg(selector="getMyDouble", signature="d@:")
        public double getMyDouble(){
            return dNum;
        }
        
        @Msg(selector="setMyDouble:", signature="v@:d")
        public void setMyDouble(double d){
            dNum = d;
        }
        
        @Msg(selector="getMyObj", signature="@@:")
        public Proxy getMyObj(){
            return myObj;
        }
        @Msg(selector="setMyObj:", signature="v@:@")
        public void setMyObj(Proxy obj){
            myObj = obj;
        }
    }

    
}
