package ca.weblite.nativeutils;
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
 
/**
 * Simple library class for working with JNI (Java Native Interface)
 *
 * @see <a href="http://frommyplayground.com/how-to-load-native-jni-library-from-jar">http://frommyplayground.com/how-to-load-native-jni-library-from-jar</a>
 * @author Adam Heirnich <a href="mailto:mailto:adam@adamh.cz"></a>, <a href="http://www.adamh.cz"></a>
 * @version $Id: $Id
 * @since 1.1
 */
public class NativeUtils {
 
    /**
     * Private constructor - this class will never be instanced
     */
    private NativeUtils() {
    }
 
    /**
     * Loads a library from current JAR archive, using the class loader of the {@code NativeUtils}
     * class to find the resource in the JAR.
     *
     * @param path a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     * @throws UnsatisfiedLinkError if loading the native library fails.
     */
    public static void loadLibraryFromJar(String path) throws IOException {
        loadLibraryFromJar(path, NativeUtils.class);
    }
    
    /**
     * Loads a library from current JAR archive.
     *
     * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after exiting.
     * Method uses String as filename because the pathname is "abstract", not system-dependent.
     *
     * @throws java.lang.IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters (restriction of @see File#createTempFile(java.lang.String, java.lang.String)).
     * @param path a {@link java.lang.String} object.
     * @param source {@code Class} whose class loader should be used to look up the resource in the JAR file
     * @throws java.io.IOException if any.
     * @throws UnsatisfiedLinkError if loading the native library fails.
     */
    public static void loadLibraryFromJar(String path, Class<?> source) throws IOException, UnsatisfiedLinkError {
        // Finally, load the library
        System.load(extractFromJar(path, source).toAbsolutePath().toString());
    }
    
    /**
     * Extracts a resource from the JAR and stores it as temporary file
     * in the file system.
     *
     * @param path path of the resource, must begin with {@code '/'}, see {@link Class#getResourceAsStream(String)}
     * @param source {@code Class} whose class loader  should be used to look up the resource in the JAR file
     * @return file path of the temporary file extracted from this JAR
     * @throws java.io.IOException if any.
     */
    public static Path extractFromJar(String path, Class<?> source) throws IOException {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }
 
        String filename = path.substring(path.lastIndexOf('/') + 1);
 
        // Split filename to prefix and suffix (extension)
        String prefix;
        String suffix;
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            // No file extension; use complete filename as prefix 
            prefix = filename;
            suffix = null;
        } else {
            prefix = filename.substring(0, lastDot);
            suffix = filename.substring(lastDot);
        }
 
        // Check if the filename is okay
        if (prefix.length() < 3) {
            throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
        }
 
        // Prepare temporary file
        Path temp = Files.createTempFile(prefix, suffix);
        temp.toFile().deleteOnExit();
 
        // Open and check input stream
        InputStream is = source.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
 
        try (is; OutputStream out = Files.newOutputStream(temp, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            is.transferTo(out);
        }
        return temp;
    }
}
