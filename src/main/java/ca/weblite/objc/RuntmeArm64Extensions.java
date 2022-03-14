package ca.weblite.objc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface RuntmeArm64Extensions extends Library {
    public static RuntmeArm64Extensions INSTANCE = (RuntmeArm64Extensions) Native.loadLibrary("objc.A", RuntmeArm64Extensions.class);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4, Object arg5);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6);
    public double objc_msgSend(Pointer theReceiver, Pointer theSelector,Object arg, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7);

}
