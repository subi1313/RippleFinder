package com.pathfinder.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LegendPanel extends JPanel {

    private final List<JLabel> labels = new ArrayList<>();

    public LegendPanel() {
        setLayout(new GridLayout(0, 1, 0, 6));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        addItem(UITheme.GRID_START, "Start point");
        addItem(UITheme.GRID_END, "Destination");
        addItem(UITheme.GRID_WALL, "Wall (obstacle)");
        addItem(UITheme.GRID_VISITED_DEEP, "Explored cell");
        addItem(UITheme.GRID_PATH, "Shortest path found");
    }

    private void addItem(Color color, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);

        JPanel swatch = new JPanel();
        swatch.setPreferredSize(new Dimension(16, 16));
        swatch.setBackground(color);
        swatch.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60)));

        JLabel text = new JLabel(label);
        text.setForeground(UITheme.sidebarText());
        text.setFont(UITheme.BODY_FONT);
        labels.add(text);

        row.add(swatch);
        row.add(text);
        add(row);
    }

    public void applyTheme() {
        for (JLabel label : labels) {
            label.setForeground(UITheme.sidebarText());
        }
    }
}