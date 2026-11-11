package ca.weblite.objc;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Run in its own JVM by {@link LibjcocoaPathTest}: initializes the bridge, then
 * prints the path of every libjcocoa image the process has loaded, one per
 * line, prefixed with "loaded: ".
 */
public class LoadedLibjcocoa {

    interface Dyld extends Library {
        int _dyld_image_count();
        String _dyld_get_image_name(int index);
    }

    public static void main(String[] args) {
        if (RuntimeUtils.cls("NSObject") == null) {
            throw new IllegalStateException("bridge did not initialize");
        }
        Dyld dyld = Native.load("System", Dyld.class);
        int count = dyld._dyld_image_count();
        for (int i = 0; i < count; i++) {
            String name = dyld._dyld_get_image_name(i);
            if (name != null && name.contains("jcocoa")) {
                System.out.println("loaded: " + name);
            }
        }
    }
}
