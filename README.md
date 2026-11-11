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

### Releasing

Releases are published to Maven Central by `.github/workflows/release.yml` when a `v*` tag is pushed:

```
git tag v1.3
git push origin v1.3
```

The version is the tag without the `v`. The workflow builds the universal Release `libjcocoa`, runs the tests against it, signs the artifacts, and publishes through the Central Portal. To check the signing setup without publishing, run the workflow by hand from the Actions tab with **dry_run** ticked.

### Hardened runtime and the Mac App Store

By default the bridge extracts the `libjcocoa.dylib` inside its jar to a temporary file and loads it from there. An app signed with the hardened runtime (as notarized and Mac App Store apps are) can only load libraries signed by its own team, unless it has the `com.apple.security.cs.disable-library-validation` entitlement, so it cannot load that extracted copy.

Such an app should bundle `libjcocoa.dylib` itself (for example in `Contents/MacOS`), sign it with the rest of the app, and name it with a system property:

```
-Dca.weblite.objc.libjcocoa.path=/path/to/YourApp.app/Contents/MacOS/libjcocoa.dylib
```

With the property set, the bridge loads that file and never extracts its own. The `libjcocoa.dylib` in a release build of this jar is universal (arm64 and x86_64).

## Examples

* [Sample wrapper of NSOpenPanel and NSSavePanel](https://gist.github.com/shannah/65007754c2b0f8add4f7) 
* [Example using WebKit and .nib file](src/test/java/ca/weblite/objc/TestWebView.java)
* [Loading Nib File](src/test/java/ca/weblite/objc/LoadNibSample.java)
* [Wrapper for NSProcessInfo to Solve App Nap Problem](src/test/java/ca/weblite/objc/NSProcessInfoUtils.java)

## JavaDocs & Documentation

* [Documentation](https://solutions.weblite.ca/maven/java-objc-bridge/apidocs/index.html)
* Read a [blog post](https://sjhannah.com/blog/2012/10/29/speaking-cocoa-from-java/) about the motivation for this project.

## Contact

* Post your questions in the [Java-Objective-C Google Group](https://groups.google.com/forum/#!forum/java-objective-c-bridge)
* Post bugs and feature requests to the [issue tracker](https://github.com/shannah/Java-Objective-C-Bridge/issues)

## Credits

Created by [Steve Hannah](https://sjhannah.com)
