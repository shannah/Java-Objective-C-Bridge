package ca.weblite.objc.foundation;

import com.sun.jna.Structure;

import java.util.List;

/**
 *  A structure mapping to the Foundation structure NSRange.  If you need to call a method that
 *  receives an NSRange as an input, you can use this structure.
 */
public class NSRange extends Structure {

    public static class ByReference extends NSRange implements Structure.ByReference{
    }
    public static class ByValue extends NSRange implements Structure.ByValue {
    }

    /**
     * The location.  WARNING: This stores an unsigned integer value.  Use {@link #getLocation()} and {@link #setLocation(int)}
     * to properly convert to/from Java's signed ints.
     */
    public long location;

    /**
     * The length.  WARNING: This stores an unsigned integer value.  Use {@link #getLength()} and {@link #setLength(int)}
     * to properly convert to/from Java's signed ints.
     */
    public long length;

    /**
     * Sets the location of the range.
     * @param loc The location.
     */
    public void setLocation(int loc) {
        location = Integer.toUnsignedLong(loc);
    }

    /**
     * Sets the length of the range.
     * @param len The length of the range.
     */
    public void setLength(int len) {
        length = Integer.toUnsignedLong(len);
    }

    /**
     * Gets the location. Prefer this accessor to direct access of {@link #location} because {@link #location} stores
     * is unsigned, so the value will seem nonsensical.
     * @return The location as a signed int.
     */
    public int getLocation() {
        return (int)Integer.toUnsignedLong((int)location);
    }

    /**
     * Gets the length as a signed int.
     * @return The length as a signed int.
     */
    public int getLength() {
        return (int)Integer.toUnsignedLong((int)length);
    }

    @Override
    protected List<String> getFieldOrder() {
        return List.of("location","length");
    }
}
