package com.pathfinder.gui;

import java.awt.*;

public class UITheme {

    public static boolean darkMode = true;

    private static final Color DARK_SIDEBAR_BG = new Color(30, 33, 45);
    private static final Color DARK_SIDEBAR_TEXT = new Color(230, 230, 235);
    private static final Color DARK_LOG_BG = new Color(45, 48, 62);
    private static final Color DARK_LOG_TEXT = Color.WHITE;
    private static final Color DARK_CONTENT_BG = new Color(22, 24, 33);
    private static final Color DARK_TITLE_TEXT = Color.WHITE;
    private static final Color DARK_SUBTITLE_TEXT = new Color(190, 195, 210);
    private static final Color DARK_MAZE_BORDER = new Color(70, 73, 90);

    // Light theme — soft pink instead of stark white
    private static final Color LIGHT_SIDEBAR_BG = new Color(255, 235, 240);
    private static final Color LIGHT_SIDEBAR_TEXT = new Color(60, 35, 45);
    private static final Color LIGHT_LOG_BG = new Color(255, 220, 230);
    private static final Color LIGHT_LOG_TEXT = new Color(50, 30, 38);
    private static final Color LIGHT_CONTENT_BG = new Color(255, 244, 247);
    private static final Color LIGHT_TITLE_TEXT = new Color(120, 40, 65);
    private static final Color LIGHT_SUBTITLE_TEXT = new Color(150, 90, 105);
    private static final Color LIGHT_MAZE_BORDER = new Color(230, 190, 200);

    public static final Color ACCENT_HOVER_DARK = new Color(120, 150, 250);
    public static final Color ACCENT_HOVER_LIGHT = new Color(200, 60, 110);

    public static final Color BFS_COLOR = new Color(94, 129, 244);
    public static final Color DFS_COLOR = new Color(244, 133, 94);

    public static final Color GRID_WALL = new Color(50, 52, 60);
    public static final Color GRID_START = new Color(46, 204, 113);
    public static final Color GRID_END = new Color(231, 76, 60);
    public static final Color GRID_PATH = new Color(255, 205, 60);
    public static final Color GRID_VISITED_DEEP = new Color(90, 140, 235);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    public static void toggleTheme() {
        darkMode = !darkMode;
    }

    public static Color sidebarBg() { return darkMode ? DARK_SIDEBAR_BG : LIGHT_SIDEBAR_BG; }
    public static Color sidebarText() { return darkMode ? DARK_SIDEBAR_TEXT : LIGHT_SIDEBAR_TEXT; }
    public static Color logBg() { return darkMode ? DARK_LOG_BG : LIGHT_LOG_BG; }
    public static Color logText() { return darkMode ? DARK_LOG_TEXT : LIGHT_LOG_TEXT; }
    public static Color contentBg() { return darkMode ? DARK_CONTENT_BG : LIGHT_CONTENT_BG; }
    public static Color titleText() { return darkMode ? DARK_TITLE_TEXT : LIGHT_TITLE_TEXT; }
    public static Color subtitleText() { return darkMode ? DARK_SUBTITLE_TEXT : LIGHT_SUBTITLE_TEXT; }
    public static Color mazeBorder() { return darkMode ? DARK_MAZE_BORDER : LIGHT_MAZE_BORDER; }
    public static Color accentHover() { return darkMode ? ACCENT_HOVER_DARK : ACCENT_HOVER_LIGHT; }
    public static Color gridEmpty() { return Color.WHITE; } // grid itself stays white for contrast with walls/path
}