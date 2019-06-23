//
//  WLJavaProxy.h
//  libjcocoa
//
//  Created by Steve Hannah on 2012-10-21.
//  Copyright (c) 2012 Web Lite Solutions. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <jni.h>

@interface WLJavaProxy : NSObject {
    jobject peer;
    jclass peerClass;
    jmethodID jMethodSignatureForSelector;
    jmethodID jForwardInvocation;
    jmethodID jRespondsToSelector;

}
+(void)setJVM:(JavaVM*)theJvm;
-(WLJavaProxy*)init:(jobject)peer;
-(NSMethodSignature*)methodSignatureForSelector:(SEL)sel;
-(void)forwardInvocation:(NSInvocation *)invocation;
-(BOOL)respondsToSelector:(SEL)aSelector;
-(jobject)javaPeer;



@end
