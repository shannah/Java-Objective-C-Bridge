# Java-Objective-C-Bridge

## Synopsis

A thin bridge that allows for two-way communication from Java to Objective-C.

## License

[Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html)

## Requirements

1. Java 11 or Higher on OS X
2. JNA

## Getting Started

Add the following dependency to your pom.xml:
```xml
    <dependency>
        <groupId>ca.weblite</groupId>
        <artifactId>java-objc-bridge</artifactId>
        <version>1.2</version>
    </dependency>
```

### Working with Sources

1. Check out the project and use `mvn clean install` (for debug builds) or `mvn clean install -Drelease=true` (for release builds) to build it
2. Include it as a Maven depencency in your project:
    ```xml
    <dependency>
        <groupId>ca.weblite</groupId>
        <artifactId>java-objc-bridge</artifactId>
        <version>1.3-SNAPSHOT</version>
    </dependency>
    ```

## Examples

* [Sample wrapper of NSOpenPanel and NSSavePanel](https://gist.github.com/shannah/65007754c2b0f8add4f7) 
* [Example using WebKit and .nib file](src/test/java/ca/weblite/objc/TestWebView.java)
* [Loading Nib File](src/test/java/ca/weblite/objc/LoadNibSample.java)
* [Wrapper for NSProcessInfo to Solve App Nap Problem](src/test/java/ca/weblite/objc/NSProcessInfoUtils.java)

## JavaDocs & Documentation

* [Documentation](https://solutions.weblite.ca/java-objective-c-bridge/docs/)
* Read a [blog post](https://sjhannah.com/blog/2012/10/29/speaking-cocoa-from-java/) about the motivation for this project.

## Contact

* Post your questions in the [Java-Objective-C Google Group](https://groups.google.com/forum/#!forum/java-objective-c-bridge)
* Post bugs and feature requests to the [issue tracker](https://github.com/shannah/Java-Objective-C-Bridge/issues)

## Credits

Created by [Steve Hannah](https://sjhannah.com)
