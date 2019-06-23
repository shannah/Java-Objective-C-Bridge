//
//  JavaUtil.h
//  libjcocoa
//
//  Created by Matthias Fuchs on 04.06.19.
//  Copyright © 2019 Web Lite Solutions. All rights reserved.
//

#ifndef JavaUtil_h
#define JavaUtil_h
#import <jni.h>

@interface JavaUtil : NSObject

+(void) throwJavaException:(JNIEnv *)env withMessage: (const char *)msg;

@end

#endif /* JavaUtil_h */
