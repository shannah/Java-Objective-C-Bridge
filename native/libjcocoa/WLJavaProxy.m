//
//  WLJavaProxy.m
//  libjcocoa
//
//  Created by Steve Hannah on 2012-10-21.
//  Copyright (c) 2012 Web Lite Solutions. All rights reserved.
//

#import "WLJavaProxy.h"
#include <JavaNativeFoundation/JavaNativeFoundation.h>


static JavaVM *jvm = NULL;


@implementation WLJavaProxy

-(WLJavaProxy*)init:(jobject)thePeer
{
    
    JNIEnv *env=0;
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
}

-(void)dealloc
{
    JNIEnv *env=0;
    int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
    
    if ( attach == 0 ){
        //JNF_COCOA_ENTER(env);
        (*env)->DeleteGlobalRef(env, peerClass);
        (*env)->DeleteGlobalRef(env, peer);
        [super dealloc];
        //JNF_COCOA_EXIT(env);
    }
    
}

+(void)setJVM:(JavaVM*)theJvm
{
    JNIEnv *env=0;
    int attach = (*theJvm)->AttachCurrentThread(theJvm, (void**)&env, NULL);
    if ( attach == 0 ){
        jvm = theJvm;
    }
    
}

-(NSMethodSignature*)methodSignatureForSelector:(SEL)sel
{
    NSMethodSignature* signature;
    JNIEnv *env=0;
    int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
    if ( attach == 0 ){
        //JNF_COCOA_ENTER(env);
        signature = (NSMethodSignature*)(*env)->CallLongMethod(env, peer, jMethodSignatureForSelector, sel);
        //JNF_COCOA_EXIT(env);
    }
    //(*jvm)->DetachCurrentThread(jvm);
    return signature;
}

-(void)forwardInvocation:(NSInvocation *)invocation
{
    JNIEnv *env=0;
    int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
    if ( attach == 0 ){
        //JNF_COCOA_ENTER(env);
        (*env)->CallVoidMethod(env, peer, jForwardInvocation, invocation);
        //JNF_COCOA_EXIT(env);
    }
    //(*jvm)->DetachCurrentThread(jvm);
    
}

-(BOOL)respondsToSelector:(SEL)aSelector
{
    BOOL response = FALSE;
    JNIEnv *env=0;
    int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
    if ( attach == 0 ){
        //JNF_COCOA_ENTER(env);
        response = (*env)->CallBooleanMethod(env, peer, jRespondsToSelector, aSelector);
        //JNF_COCOA_EXIT(env);
    }
    //(*jvm)->DetachCurrentThread(jvm);
    return response==1?TRUE:FALSE;
}

-(jobject)javaPeer
{
    JNIEnv *env=0;
    int attach = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
    if ( attach == 0 ){
        return peer;
    }
    return NULL;
}
@end
