/*
 * Copyright 2021 Web Lite Solutions Corp..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.weblite.objc;

import java.lang.reflect.Method;

/**
 * Exception that may be thrown when failure occurs while trying to invoke
 * a method in response to a selector.
 * @author shannah
 */
public class NSMessageInvocationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final String selectorName;
    private final Method method;
    
    /**
     * Creates a new instance of this exception.
     * @param selectorName The objective-c selector that was being handled.
     * @param method The method that was being executed.
     * @param cause The cause of the exception.
     */
    public NSMessageInvocationException(String selectorName, Method method, Throwable cause) {
        super(String.format("Method invocation for selector %s caused exception.  Method:  %s", selectorName, method), cause);
        this.selectorName = selectorName;
        this.method = method;
    }
    
    /**
     * The name of the Objective-C selector that was being called.
     * @return 
     */
    public String getSelectorName() {
        return selectorName;
    }
    
    /**
     * The method that was being executed when this exception occurred.
     * @return 
     */
    public Method getMethod() {
        return method;
    }
}
