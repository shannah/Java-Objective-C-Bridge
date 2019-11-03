# Java-Objective-C-Bridge

## Synopsis

A thin bridge that allows for two-way communication from Java to Objective-C.

## License

Apache 2.0 License

## Requirements

1. Java 11 or Higher on OS X
2. JNA

## Getting Started

1. Check out the project and use `mvn clean install` (for debug builds) or `mvn clean install -Drelease=true` (for release builds) to build it
2. include it as a maven depencency in your project:

```xml
<dependency>
	<groupId>ca.weblite</groupId>
	<artifactId>java-objc-bridge</artifactId>
	<version>1.1-SNAPSHOT</version>
</dependency>
```

## Examples

* [Sample wrapper of NSOpenPanel and NSSavePanel](https://gist.github.com/shannah/65007754c2b0f8add4f7) 
* [Example using WebKit and .nib file](src/test/java/ca/weblite/objc/TestWebView.java)
* [Loading Nib File](src/test/java/ca/weblite/objc/LoadNibSample.java)
* [Wrapper for NSProcessInfo to Solve App Nap Problem](src/test/java/ca/weblite/objc/NSProcessInfoUtils.java)

## JavaDocs & Documentation

* [Documentation](http://solutions.weblite.ca/maven/java-objc-bridge/apidocs/index.html)
* Read a [blog post](http://www.shannah.ca/blog/?p=219) about the motivation for this project.

## Contact

* Post your questions in the [Java-Objective-C Google Group](https://groups.google.com/forum/#!forum/java-objective-c-bridge)
* Post bugs and feature requests to the [issue tracker](https://github.com/shannah/Java-Objective-C-Bridge/issues)

## Credits

Created by [Steve Hannah](http://sjhannah.com)




