# Java-Objective-C-Bridge

## Synopsis

A thin bridge that allows for two-way communication from Java to Objective-C.

## License

Apache 2.0 License

## Requirements

1. Java7 or Higher on OS X
2. JNA

##Installation

1. Add [ObjCBridge.jar](https://github.com/shannah/Java-Objective-C-Bridge/blob/master/dist/ObjCBridge.jar?raw=true) and [jna.jar](https://github.com/shannah/Java-Objective-C-Bridge/blob/master/dist/jna-4.0.0.jar?raw=true) to your classpath.
2. **Optional** - Add [libjcocoa.dylib](https://github.com/shannah/Java-Objective-C-Bridge/blob/master/dist/libjcocoa.dylib?raw=true) to your library path.  It will first check to see if libjcocoa.dylib in in the library path.  It is isn't, it will simply extract it to the temp directory and use that one.

##Examples

* [Sample wrapper of NSOpenPanel and NSSavePanel](https://gist.github.com/shannah/65007754c2b0f8add4f7) 
* [Example using WebKit and .nib file](https://github.com/shannah/Java-Objective-C-Bridge/blob/master/java/test/ca/weblite/objc/TestWebView.java)
* [Loading Nib File](https://github.com/shannah/Java-Objective-C-Bridge/blob/master/java/test/ca/weblite/objc/LoadNibSample.java)
* [Wrapper for NSProcessInfo to Solve App Nap Problem](java/test/ca/weblite/objc/NSProcessInfoUtils.java)

##JavaDocs & Documentation

* [Documentation](http://solutions.weblite.ca/java-objective-c-bridge/docs)
* Read a [blog post](http://www.shannah.ca/blog/?p=219) about the motivation for this project.

##Contact

* [Mailing List](http://java.net/projects/java-objc-bridge/lists)

##Credits

Created by [Steve Hannah](http://sjhannah.com)




