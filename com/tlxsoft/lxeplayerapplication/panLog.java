package com.tlxsoft.lxeplayerapplication;

import android.util.Log;

/* compiled from: MainActivity */
class panLog {
    public static boolean isdebug = false;

    panLog() {
    }

    public static void wtf(String str, String str2) {
        if (isdebug) {
            Log.wtf(str, str2);
        }
    }
}
