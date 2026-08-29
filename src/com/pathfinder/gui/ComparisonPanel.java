package com.pathfinder.gui;

import com.pathfinder.solver.SolverStats;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ComparisonPanel extends JPanel {

    private final DefaultTableModel model;
    private final JLabel header;
    private final JScrollPane scrollPane;

    public ComparisonPanel() {
        setLayout(new BorderLayout(0, 8));
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(320, 130));
        setPreferredSize(new Dimension(320, 130));

        header = new JLabel("Comparison");
        header.setFont(UITheme.SECTION_FONT);
        header.setForeground(UITheme.sidebarText());

        String[] columns = {"Algo", "Visited", "Path Len", "Time (ms)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setEnabled(false);

        scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(320, 90));
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.mazeBorder()));

        setAlignmentX(Component.LEFT_ALIGNMENT);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateStats(SolverStats stats) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(stats.getAlgorithmName())) {
                model.setValueAt(stats.getCellsVisited(), i, 1);
                model.setValueAt(stats.getPathLength() >= 0 ? stats.getPathLength() : "none", i, 2);
                model.setValueAt(stats.getElapsedMillis(), i, 3);
                return;
            }
        }
        model.addRow(new Object[]{
                stats.getAlgorithmName(),
                stats.getCellsVisited(),
                stats.getPathLength() >= 0 ? stats.getPathLength() : "none",
                stats.getElapsedMillis()
        });
    }

    public void applyTheme() {
        header.setForeground(UITheme.sidebarText());
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.mazeBorder()));
    }
}