package com.pathfinder.util;

import java.awt.*;

public class SoundPlayer {

    private static boolean enabled = true;

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    // Simple built-in beep — no audio files needed, works cross-platform
    public static void playSuccess() {
        if (!enabled) return;
        new Thread(Toolkit.getDefaultToolkit()::beep).start();
    }
}