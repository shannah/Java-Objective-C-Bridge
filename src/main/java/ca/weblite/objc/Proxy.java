/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.objc;

import static ca.weblite.objc.RuntimeUtils.*;
import com.sun.jna.Pointer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper around a native (Objective-C) object that allows for sending
 * messages from Java.
 * 
 * <h3>Example Wrapping NSMutableArray Object</h3>
 * 
 * <p>The following snippet is taken from a unit test, to give you an idea 
 * of how to use the Proxy class to wrap an Objective-C object.</p>
 * 
 * <script src="https://gist.github.com/3969640.js?file=ProxySample.java"></script>
 * 
 * <p>The NSRange object is a structure that we define in Java to correspond
 * with the NSRange objective-c structure according to JNA conventions.  It's 
 * implementation (for your reference) was:</p>
 * <script src="https://gist.github.com/3969640.js?file=NSRange.java"></script>
 * 
 * @see <a href="https://github.com/twall/jna">JavaNativeAccess On GitHub</a>
 * @author shannah
 */
public class Proxy implements Peerable {
    
    
    /**
     * A cache of Proxy objects that is used by the load() method to ensure
     * that we don't create different Proxy objects for the same Objective-C 
     * native pointer.
     */
    private static Map<Pointer,Proxy> proxyCache = new HashMap<Pointer,Proxy>();
    
    /**
     * The client that is used to make requests to the Objective-C runtime.
     */
    Client client;
    
    /**
     * The Objective-C object to which this proxy sends its messages.
     */
    Pointer peer;
    
    
    private int retainCount = 0;
    
    /**
     * Retains the Proxy object in the Cache.  This is not related to the 
     * Objective-C message "release".  It pertains only to the Java cache
     * of proxy objects.
     * 
     * @param obj The object that is being retained.  If the object is a Proxy
     * object, then its retainCount will be incremented.
     *
     * @return The object that was passed to it.
     */
    public static Object retain(Object obj){
        synchronized(proxyCache){
            if ( Proxy.class.isInstance(obj) ){
                Proxy pobj = (Proxy)obj;
                pobj.retainCount++;
            }
        }
        return obj;
    }
    
    /**
     * Releases the Proxy object from the Cache.  This is not related to the 
     * Objective-C message "release".  It pertains only to the Java cache
     * of proxy objects.
     * 
     * @param obj The object that is being released.  If the object is a Proxy
     * object, then its retainCount will be decremented, and, if it is zero,
     * will be removed from the proxy cache.
     * @return The object that was passed to it.
     */
    public static Object release(Object obj){
        synchronized (proxyCache){
            if ( Proxy.class.isInstance(obj) ){
                Proxy pobj = (Proxy)obj;
                pobj.retainCount--;
                if ( pobj.retainCount <= 0 ){
                    proxyCache.remove(pobj.getPeer());
                }
            }
        }
        return obj;
    }
    
    public static void drainCache(){
        synchronized(proxyCache){
            Set<Proxy> remove = new HashSet<Proxy>();
            for ( Proxy p : proxyCache.values() ){
                if ( p.retainCount == 0 ){
                    remove.add(p);
                }
            }
            for ( Proxy p : remove ){
                proxyCache.remove(p.getPeer());
            }
        }
    }
    
    
    /**
     * Creates a proxy for a Null pointer.
     */
    public Proxy(){
        this(Client.getInstance());
    }
    
    /**
     * Creates a proxy for a Null pointer using the specified Client object.
     * @param client 
     */
    public Proxy(Client client){
        this(client, Pointer.NULL);
    }
    
    /**
     * Creates a proxy for the specified peer Objective-C object, using
     * the specified client to send messages to the peer.
     * @param client
     * @param peer 
     */
    public Proxy(Client client, Pointer peer){
        this.client = client;
        this.peer = peer;
    }
    
    
    /**
     * Creates a proxy for the specified peer Objective-C object.
     * @param peer 
     */
    public Proxy(Pointer peer){
        this(Client.getInstance(), peer);
    }
   
    
    /**
     * Loads a proxy object for the specified pointer to an objective-c object.
     * If a Proxy has previously been created for this pointer, this same
     * proxy object will be loaded from the cache.
     * 
     * <p>Note:  This will perform a retain() on the Proxy object, so you should
     * release it when you are done with it to remove it from the cache.</p>
     * @param peer The objective-c peer object.
     * @return A proxy that wraps the provided peer object.
     */
    public static Proxy load(Pointer peer){
        synchronized (proxyCache){
            Proxy cached = proxyCache.get(peer);
            if ( cached == null ){
                cached = new Proxy(peer);
                proxyCache.put(peer, cached);
            }
            retain(cached);
            return cached;
        }
        
    }
    
   
    /**
     * Removes the proxy from the proxy cache, and optionally sends a dealloc message
     * to the peer.
     * @param sendDeallocMessage IF true, then this will also send a dealloc message
     * to the peer.  If false, then it will simply remove from the Proxy cache, but
     * leave the Objective-C object intact.
     */
    public void dispose(boolean sendDeallocMessage){
        proxyCache.remove(getPeer());
        if ( sendDeallocMessage ){
            send("dealloc");
        }
    }
    
    /**
     * A wrapper for the send() method, that returns a Pointer.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a Pointer.
     */
    public Pointer sendPointer(Pointer selector, Object... args){
        return (Pointer)send(selector, args);
    }
    
    /**
     * A wrapper for the send() method, that returns a Pointer.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a Pointer.
     */
    public Pointer sendPointer(String selector, Object... args){
        return sendPointer(sel(selector), args);
    }
    
    /**
     * A wrapper for the send() method, that returns a Pointer.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a Pointer.
     */
    public Proxy sendProxy(String selector, Object... args){
        return (Proxy)send(selector, args);
    }
    
    /**
     * A wrapper for the send() method, that returns a Pointer.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a Pointer.
     */
    public Proxy sendProxy(Pointer selector, Object... args){
        return (Proxy)send(selector, args);
    }
    
    public String sendString(Pointer selector, Object... args){
        return (String)send(selector, args);
    }
    
    public String sendString(String selector, Object... args){
        return sendString(sel(selector), args);
    }
    
    
    /**
     * A wrapper for the send() method, that returns an int.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as an int.
     */
    public int sendInt(Pointer selector, Object... args){
        Object res = send(selector, args);
        if ( boolean.class.isInstance(res) || Boolean.class.isInstance(res)){
            return ((Boolean)res)?1:0;
        } else if ( byte.class.isInstance(res) || Byte.class.isInstance(res)) {
            return new Byte((Byte)res).intValue();
        } else if ( int.class.isInstance(res) || Integer.class.isInstance(res)){
            return (Integer)res;
        } else if ( long.class.isInstance(res) || Long.class.isInstance(res)){
            return new Long((Long)res).intValue();
        } else {
            return (Integer)res;
        }
    }
    
    /**
     * A wrapper for the send() method, that returns an int.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as an int.
     */
    public int sendInt(String selector, Object... args){
        return sendInt(sel(selector), args);
    }
    
    /**
     * A wrapper for the send() method, that returns a double.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a double.
     */
    public double sendDouble(Pointer selector, Object... args){
        return (Double)send(selector, args);
    }
    
    /**
     * A wrapper for the send() method, that returns a double.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a double.
     */
    public double sendDouble(String selector, Object... args){
        return sendDouble(sel(selector), args);
    }
    
    /**
     * A wrapper for the send() method, that returns a boolean.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a boolean.
     */
    public boolean sendBoolean(Pointer selector, Object... args){
        Object res = send(selector, args);
        if ( boolean.class.isInstance(res) || Boolean.class.isInstance(res)){
            return (Boolean)res;
        } else if ( byte.class.isInstance(res) || Byte.class.isInstance(res)) {
            byte bres = (Byte)res;
            return bres > 0 ? true:false;
        } else if ( int.class.isInstance(res) || Integer.class.isInstance(res)){
            int ires = (Integer)res;
            return ires > 0 ? true:false;
        } else if ( long.class.isInstance(res) || Long.class.isInstance(res)){
            long lres = (Long)res;
            return lres > 0L ? true:false;
        } else {
            return (Boolean)res;
        }
    }
    /**
     * A wrapper for the send() method, that returns a boolean.
     * @param selector The selector to call on the peer.
     * @param args Variable argument list.
     * @return The result of the message call as a boolean.
     */
    public boolean sendBoolean(String selector, Object... args){
        return sendBoolean(sel(selector), args);
    }
    
    /**
     * Sends a message to the peer.
     * @param selector The selector to send to.
     * @param args Variable argument list.
     * @return The result of the message call.
     */
    public Object send(Pointer selector, Object... args){
        return client.send(peer, selector, args);
    }
    
    /**
     * Sends a message to the peer.
     * @param selector The selector to send to.
     * @param args Variable argument list.
     * @return The result of the message call.
     */
    public Object send(String selector, Object... args){
        return client.send(peer, selector, args);
    }
    
    /**
     * Sends a message to the peer.
     * @param selector The selector to send to.
     * @param args Variable argument list.
     * @return The result of the message call.
     */
    public Object send(Message... msgs){
        return client.send(msgs);
    }
    
    /**
     * Sends a message to the peer.
     * @param selector The selector to send to.
     * @param args Variable argument list.
     * @return The result of the message call.
     */
    public Object sendRaw(Pointer selector, Object... args){
        return Client.getRawClient().send(this, selector, args);
    }
    
    /**
     * Sends a message to the peer without performing any type coercion to 
     * the inputs or outputs.
     * @param selector The selector to send to.
     * @param args Variable argument list.
     * @return The result of the message call.
     */
    public Object sendRaw(String selector, Object... args){
        return sendRaw(RuntimeUtils.sel(selector), args);
    }
    
    
    /**
     * Sends a message to the peer without performing any type coercion to 
     * the inputs or outputs.
     * @param selector The selector to send to.
     * @param args Variable argument list.
     * @return The result of the message call.
     */
    public Object sendRaw(Message... msgs){
        return Client.getRawClient().send(msgs);
    }
    
    /**
     * @deprecated
     * @param selector
     * @param args
     * @return 
     */
    public Proxy chain(Pointer selector, Object... args){
        send(selector, args);
        return this;
    }
    
    /**
     * @deprecated
     * @param selector
     * @param args
     * @return 
     */
    public Proxy chain(String selector, Object... args){
        send(selector, args);
        return this;
    }
    
    
    /**
     * @deprecated
     * @param msgs
     * @return 
     */
    public Proxy chain(Message... msgs){
        send(msgs);
        return this;
    }
    
    /**
     * Returns the client that is used by this Proxy object.
     * @return 
     */
    public Client getClient(){
        return client;
    }
    
    /**
     * Sets the client that should be used for sending messages to the 
     * peer object.
     * @param client
     * @return 
     */
    public Proxy setClient(Client client){
        this.client = client;
        return this;
    }
    
    /**
     * Returns the Pointer to the native peer object.
     * @return 
     */
    @Override
    public Pointer getPeer(){
        return peer;
    }
    
    
    /**
     * Sets the pointer to the native peer object.
     * @param peer 
     */
    @Override
    public void setPeer(Pointer peer){
        this.peer = peer;
        
    }
    
    /**
     * Outputs the object as a string.  This is a wrapper around the "description"
     * method of NSObject.
     * @return 
     */
    @Override
    public String toString(){
        System.out.println("The peer is "+getPeer());
        if ( getPeer() == null ){
            return "null";
        }
        Pointer res =  Client.getRawClient().sendPointer(getPeer(), "description");
        //System.out.println("About to send UTF8String msg");
        Pointer str = Client.getRawClient().sendPointer(res, "UTF8String");
        //System.out.println("Sent UTF8String msg");
        return str.getString(0);
        
    }
    
    /**
     * Compares this object to another Peerable object.  This effectively says
     * that the objects are equal if their peer objects are equal.
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o){
        if ( !Peerable.class.isInstance(o) ){
            return false;
        }
        Peerable p = (Peerable)o;
        return (getPeer() == p.getPeer());
        
    }
    
    @Override
    public int hashCode(){
        return getPeer().hashCode();
    }
    
    /**
     * Wrapper for Key-Value coding.  I.e. a wrapper for the setValue:forKey:
     * message of NSObject.
     * @param key
     * @param value 
     */
    public void set(String key, Object value){
        send("setValue:forKey:", value, key);
    }
    
    /**
     * Wrapper for key-value coding.  I.e a wrapper for the valueForKey: method
     * of NSObject.
     * @param key
     * @return 
     */
    public Object get(String key){
        return send("valueForKey:", key);
    }
    
    /**
     * Returns the KVC coded value for the specified key as an int.
     * @param key
     * @return 
     */
    public int getInt(String key){
        return sendInt("valueForKey:", key);
    }
    
    /**
     * Returns the KVC coded value for the specified key as a boolean.
     * @param key
     * @return 
     */
    public boolean getBoolean(String key){
        return sendBoolean("valueForKey:", key);
    }
    
    /**
     * Returns the KVC coded value for the specified key as a Proxy.
     * @param key
     * @return 
     */
    public Proxy getProxy(String key){
        return sendProxy("valueForKey:", key);
    }
    
    /**
     * Returns the KVC coded value for the specified key as a double.
     * @param key
     * @return 
     */
    public double getDouble(String key){
        return sendDouble("valueForKey:", key);
    }
    
    /**
     * Returns the KVC coded value for the specified key as a Pointer.
     * @param key
     * @return 
     */
    public Pointer getPointer(String key){
        return sendPointer("valueForKey:", key);
    }
    

    
}
