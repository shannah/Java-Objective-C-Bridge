//
//  JavaUtil.m
//  libjcocoa
//
//  Created by Matthias Fuchs on 04.06.19.
//  Copyright © 2019 Web Lite Solutions. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JavaUtil.h"

@implementation JavaUtil

+(void) throwJavaException:(JNIEnv *)env withMessage: (const char *)msg
{
    // You can put your own exception here
    jclass c = (*env)->FindClass(env, "java/lang/RuntimeException");
    
    (*env)->ThrowNew(env, c, msg);
}

@end
