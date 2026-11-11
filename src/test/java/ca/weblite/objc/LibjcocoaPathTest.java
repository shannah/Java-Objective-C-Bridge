package ca.weblite.objc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Which libjcocoa the bridge loads, with and without
 * {@link RuntimeUtils#LIBJCOCOA_PATH_PROPERTY}. The library is loaded once per
 * JVM, in RuntimeUtils' static initializer, so each case runs
 * {@link LoadedLibjcocoa} in a JVM of its own.
 */
public class LibjcocoaPathTest {

    @TempDir
    Path tempDir;

    @Test
    public void loadsTheFileNamedByTheProperty() throws Exception {
        // A copy in a directory of its own, so it cannot be mistaken for the
        // file the default path extracts from the jar.
        Path bundled = tempDir.resolve("bundled");
        Files.createDirectories(bundled);
        Path library = Files.copy(builtLibjcocoa(), bundled.resolve("libjcocoa.dylib"), StandardCopyOption.REPLACE_EXISTING);

        List<String> loaded = runLoadedLibjcocoa("-D" + RuntimeUtils.LIBJCOCOA_PATH_PROPERTY + "=" + library);

        assertEquals(List.of(canonical(library.toString())), loaded);
    }

    @Test
    public void extractsTheCopyInsideTheJarByDefault() throws Exception {
        List<String> loaded = runLoadedLibjcocoa();

        assertEquals(1, loaded.size(), "loaded " + loaded);
        String path = loaded.get(0);
        assertNotEquals(canonical(builtLibjcocoa().toString()), path);
        assertTrue(path.startsWith(canonical(System.getProperty("java.io.tmpdir"))),
                path + " is not in java.io.tmpdir");
    }

    @Test
    public void rejectsAPathThatIsNotAFile() {
        String missing = tempDir.resolve("missing/libjcocoa.dylib").toString();
        UnsatisfiedLinkError error = assertThrows(UnsatisfiedLinkError.class,
                () -> RuntimeUtils.loadLibjcocoa(missing));
        assertTrue(error.getMessage().contains(RuntimeUtils.LIBJCOCOA_PATH_PROPERTY), error.getMessage());
        assertTrue(error.getMessage().contains(missing), error.getMessage());
    }

    /** The libjcocoa.dylib the build put on the class path (target/classes). */
    private static Path builtLibjcocoa() throws URISyntaxException {
        URL url = RuntimeUtils.class.getResource("/libjcocoa.dylib");
        assertNotNull(url, "libjcocoa.dylib is not on the class path; build it with mvn");
        assertEquals("file", url.getProtocol(), "expected libjcocoa.dylib in a directory, not " + url);
        return Path.of(url.toURI());
    }

    private static String canonical(String path) throws IOException {
        return new File(path).getCanonicalPath();
    }

    /** Runs LoadedLibjcocoa in a new JVM and returns the canonical paths it reports. */
    private static List<String> runLoadedLibjcocoa(String... jvmOptions) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(Path.of(System.getProperty("java.home"), "bin", "java").toString());
        command.addAll(List.of(jvmOptions));
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(LoadedLibjcocoa.class.getName());

        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        List<String> output = new ArrayList<>();
        List<String> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
                if (line.startsWith("loaded: ")) {
                    loaded.add(canonical(line.substring("loaded: ".length())));
                }
            }
        }
        assertEquals(0, process.waitFor(), "LoadedLibjcocoa failed:\n" + String.join("\n", output));
        return loaded;
    }
}
