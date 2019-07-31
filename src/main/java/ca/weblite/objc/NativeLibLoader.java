package ca.weblite.objc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>NativeLibLoader class.</p>
 *
 * @author shannah
 * @version $Id: $Id
 */
public class NativeLibLoader implements Constants {
	private boolean loaded;

	private static String getLibName(boolean withPrefixAndExtension) {
		if (withPrefixAndExtension) {
			return "libjcocoa-" + VERSION + ".dylib";
		} else {
			return "jcocoa-" + VERSION;
		}
	}

	/**
	 * <p>load.</p>
	 *
	 * @throws java.lang.UnsatisfiedLinkError if any.
	 */
	public void load() throws UnsatisfiedLinkError {
		if (loaded) {
			return;
		}
		// Try for versioned name first
		try {
			System.loadLibrary(getLibName(false));
			loaded = true;
		} catch (UnsatisfiedLinkError err) {

		}
		if (loaded) {
			return;
		}
		// Try for unversioned name
		// For backward compatibility of existing deployments, this needs to
		// occur before we try to extract the lib from the jar file.
		try {
			System.loadLibrary("libjcocoa");
			loaded = true;
		} catch (UnsatisfiedLinkError err) {

		}

		if (loaded) {
			return;
		}

		try {
			extract();
			System.load(getExtractLocation().getAbsolutePath());
		} catch (IOException ex) {
			throw new UnsatisfiedLinkError("libjcocoa.dylib could not be found");
		}

	}

	private File getExtractLocation() {
		return new File(new File(System.getProperty("java.io.tmpdir")), getLibName(true));
	}

	private File getJarFile() {
		return new File(NativeLibLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	}

	private long getJarMTime() {
		File jarFile = getJarFile();
		if (!jarFile.exists()) {
			return System.currentTimeMillis();
		}
		return jarFile.lastModified();
	}

	/**
	 * <p>extract.</p>
	 *
	 * @throws java.io.IOException if any.
	 */
	public void extract() throws IOException {
		File extractLocation = getExtractLocation();
		if (!extractLocation.exists() || extractLocation.lastModified() < getJarMTime()) {
			copyResourceToFile(NativeLibLoader.class, "libjcocoa.dylib", extractLocation);
		}
	}

	private static void copyResourceToFile(Class context, String resource, File dest) throws IOException {
		InputStream is = null;
		try {
			is = context.getResourceAsStream(resource);
			OutputStream os = null;
			try {
				os = new FileOutputStream(dest);
				copy(is, os);
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (Throwable t) {
					}
				}
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable t) {
				}
			}
		}
	}

	private static final int BUFFER_SIZE = 8192;

	/**
	 * Reads all bytes from an input stream and writes them to an output stream.
	 */
	private static long copy(InputStream source, OutputStream sink) throws IOException {
		long nread = 0L;
		byte[] buf = new byte[BUFFER_SIZE];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			nread += n;
		}
		return nread;
	}
}
