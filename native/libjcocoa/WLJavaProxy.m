//
//  WLJavaProxy.m
//  libjcocoa
//
//  Created by Steve Hannah on 2012-10-21.
//  Copyright (c) 2012 Web Lite Solutions. All rights reserved.
//

#import "WLJavaProxy.h"
#include <JavaNativeFoundation/JavaNativeFoundation.h>
#include "JavaUtil.h"

static JavaVM *jvm = NULL;


@implementation WLJavaProxy

-(WLJavaProxy*)init:(jobject)thePeer
{
    JNIEnv *env=0;
    
    @try {
        int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
        if ( attach == 0 ){
            //JNF_COCOA_ENTER(env);
            (*jvm)->GetEnv(jvm, (void**)&env, JNI_VERSION_1_6);
            peer = (*env)->NewGlobalRef(env,thePeer);
            jclass cls  = (*env)->GetObjectClass(env, peer);
            peerClass = (*env)->NewGlobalRef(env, cls);
            (*env)->DeleteLocalRef(env, cls);
            
            
            jMethodSignatureForSelector = (*env)->GetMethodID(env, peerClass, "methodSignatureForSelector", "(J)J" );
            jForwardInvocation = (*env)->GetMethodID(env, peerClass, "forwardInvocation", "(J)V" );
            jRespondsToSelector = (*env)->GetMethodID(env, peerClass, "respondsToSelector", "(J)Z" );
            //JNF_COCOA_EXIT(env);
        }
        //(*jvm)->DetachCurrentThread(jvm);
        
        return self;
    } @catch (NSException *e) {
        NSLog(@"Exception: %@", e);
        [JavaUtil throwJavaException: env withMessage: [[e reason] UTF8String] ];
    }
}

-(void)dealloc
{
    JNIEnv *env=0;
    
    @try {
        int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
        
        if ( attach == 0 ){
            //JNF_COCOA_ENTER(env);
            (*env)->DeleteGlobalRef(env, peerClass);
            (*env)->DeleteGlobalRef(env, peer);
            [super dealloc];
            //JNF_COCOA_EXIT(env);
        }
    } @catch (NSException *e) {
        NSLog(@"Exception: %@", e);
        [JavaUtil throwJavaException: env withMessage: [[e reason] UTF8String] ];
    }
}

+(void)setJVM:(JavaVM*)theJvm
{
    JNIEnv *env=0;
    
    @try {
        int attach = (*theJvm)->AttachCurrentThread(theJvm, (void**)&env, NULL);
        if ( attach == 0 ){
            jvm = theJvm;
        }
    } @catch (NSException *e) {
        NSLog(@"Exception: %@", e);
        [JavaUtil throwJavaException: env withMessage: [[e reason] UTF8String] ];
    }
}

-(NSMethodSignature*)methodSignatureForSelector:(SEL)sel
{
    NSMethodSignature* signature;
    JNIEnv *env=0;
    
    @try {
        int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
        if ( attach == 0 ){
            //JNF_COCOA_ENTER(env);
            signature = (NSMethodSignature*)(*env)->CallLongMethod(env, peer, jMethodSignatureForSelector, sel);
            
            if (!signature) {
                signature = [@"" methodSignatureForSelector:sel];
            }
            
            //JNF_COCOA_EXIT(env);
        }
        //(*jvm)->DetachCurrentThread(jvm);
        return signature;
    } @catch (NSException *e) {
        NSLog(@"Exception: %@", e);
        [JavaUtil throwJavaException: env withMessage: [[e reason] UTF8String] ];
    }
}

-(void)forwardInvocation:(NSInvocation *)invocation
{
    JNIEnv *env=0;
    SEL aSelector = [invocation selector];
    int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
    
    @try {
        if ( attach == 0 ) {
            //JNF_COCOA_ENTER(env);
            (*env)->CallVoidMethod(env, peer, jForwardInvocation, invocation);
            //JNF_COCOA_EXIT(env);
        }
        //(*jvm)->DetachCurrentThread(jvm);
    } @catch (NSException *e) {
        NSString *message = [NSString stringWithFormat:@"Selector '%@' caused exception: %@", NSStringFromSelector(aSelector), e.reason];
        NSLog(@"%@", message);
        [JavaUtil throwJavaException: env withMessage: [message UTF8String] ];
    }
}

-(BOOL)respondsToSelector:(SEL)aSelector
{
    BOOL response = FALSE;
    JNIEnv *env=0;
    
    @try {
        int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
        if ( attach == 0 ){
            //JNF_COCOA_ENTER(env);
            response = (*env)->CallBooleanMethod(env, peer, jRespondsToSelector, aSelector);
            //JNF_COCOA_EXIT(env);
        }
        //(*jvm)->DetachCurrentThread(jvm);
        return response==1?TRUE:FALSE;
    } @catch (NSException *e) {
        NSLog(@"Exception: %@", e);
        [JavaUtil throwJavaException: env withMessage: [[e reason] UTF8String] ];
    }
}

-(jobject)javaPeer
{
    JNIEnv *env=0;
    
    @try {
        int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
        if ( attach == 0 ){
            return peer;
        }
        return NULL;
    } @catch (NSException *e) {
        NSLog(@"Exception: %@", e);
        [JavaUtil throwJavaException: env withMessage: [[e reason] UTF8String] ];
    }
}

+ (NSSet<NSString *> *)keyPathsForValuesAffectingValueForKey:(NSString *)key {
//    SEL theSelector = @selector(keyPathsForValuesAffectingValueForKey:);
//    NSMethodSignature *aSignature = [NSObject methodSignatureForSelector:theSelector];
//    NSInvocation *anInvocation = [NSInvocation invocationWithMethodSignature:aSignature];
//
//    [anInvocation setSelector:theSelector];
//    [anInvocation setTarget:self];
//    [anInvocation setArgument:&key atIndex:1];
//
//    [self forwardInvocation:anInvocation];
//
//    void *result;
//    [anInvocation getReturnValue:result];
//
//    return result;
    
    
    NSSet *keyPaths  = [[NSSet alloc] initWithObjects: key];
    return keyPaths;
}

@end
