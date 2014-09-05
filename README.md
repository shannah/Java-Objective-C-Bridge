#Java-Objective-C-Bridge

##Synopsis

A thin bridge that allows for two-way communication from Java to Objective-C.

##License

Apache 2.0 License

##Requirements

1. Java7 or Higher on OS X
2. JNA

##Installation

###Method 1: Using Maven

Add the following dependency to your pom.xml:

~~~
<dependency>
	<groupId>ca.weblite</groupId>
	<artifactId>java-objc-bridge</artifactId>
	<version>1.0.0</version>
</dependency>
~~~

###Method 2: Manual Installation

1. Add [ObjCBridge.jar](https://github.com/shannah/Java-Objective-C-Bridge/blob/master/dist/ObjCBridge.jar?raw=true)
2. Add [jna.jar](http://central.maven.org/maven2/net/java/dev/jna/jna/4.1.0/jna-4.1.0.jar) to your classpath.

##Getting Started


##Examples

* [Sample wrapper of NSOpenPanel and NSSavePanel](https://gist.github.com/shannah/65007754c2b0f8add4f7) 
* [Example using WebKit and .nib file](src/test/java/ca/weblite/objc/TestWebView.java)
* [Loading Nib File](src/test/java/ca/weblite/objc/LoadNibSample.java)
* [Wrapper for NSProcessInfo to Solve App Nap Problem](src/test/java/ca/weblite/objc/NSProcessInfoUtils.java)

##JavaDocs & Documentation

* [Documentation](http://solutions.weblite.ca/maven/java-objc-bridge/apidocs/index.html)
* Read a [blog post](http://www.shannah.ca/blog/?p=219) about the motivation for this project.

##Contact

* Post your questions in the [Java-Objective-C Google Group](https://groups.google.com/forum/#!forum/java-objective-c-bridge)
* Post bugs and feature requests to the [issue tracker](https://github.com/shannah/Java-Objective-C-Bridge/issues)

##Credits

Created by [Steve Hannah](http://sjhannah.com)




