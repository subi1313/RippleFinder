package com.pathfinder.gui;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private final JLabel header;
    private final JTextArea logArea;
    private final JLabel statsLabel;

    public StatusPanel() {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(340, 260));

        header = new JLabel("What's happening");
        header.setFont(UITheme.SECTION_FONT);
        header.setForeground(UITheme.sidebarText());

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logArea.setForeground(UITheme.logText());
        logArea.setBackground(UITheme.logBg());
        logArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(340, 180));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 73, 90)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        statsLabel = new JLabel("Cells visited: 0   |   Path length: -");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statsLabel.setForeground(UITheme.accentHover());

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statsLabel, BorderLayout.SOUTH);

        log("Draw walls by clicking or dragging on the grid, then choose an algorithm to watch it search.");
    }

    public void log(String message) {
        logArea.append("• " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void updateStats(int visitedCount, int pathLength) {
        String pathText = pathLength >= 0 ? String.valueOf(pathLength) : "no path found";
        statsLabel.setText("Cells visited: " + visitedCount + "   |   Path length: " + pathText);
    }

    public void applyTheme() {
        header.setForeground(UITheme.sidebarText());
        logArea.setForeground(UITheme.logText());
        logArea.setBackground(UITheme.logBg());
        statsLabel.setForeground(UITheme.accentHover());
    }
}