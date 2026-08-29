package com.pathfinder.gui;

import com.pathfinder.model.Grid;
import com.pathfinder.solver.BFSSolver;
import com.pathfinder.solver.DFSSolver;
import com.pathfinder.solver.Solver;
import com.pathfinder.solver.SolverStats;

import javax.swing.*;
import java.awt.*;

public class RaceFrame extends JFrame {

    private final JLabel resultLabel;
    private boolean winnerAnnounced = false;

    public RaceFrame(Grid sourceGrid) {
        setTitle("RippleFinder — BFS vs DFS Race");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(UITheme.sidebarBg());

        // identical mazes for a fair race
        Grid bfsGrid = sourceGrid.copy();
        Grid dfsGrid = sourceGrid.copy();

        MazePanel bfsPanel = new MazePanel(bfsGrid);
        MazePanel dfsPanel = new MazePanel(dfsGrid);
        bfsPanel.setOnStatusMessage(msg -> {});
        dfsPanel.setOnStatusMessage(msg -> {});
        bfsPanel.setOnStatsUpdate((v, p) -> {});
        dfsPanel.setOnStatsUpdate((v, p) -> {});

        JPanel racePanel = new JPanel(new GridLayout(1, 2, 16, 0));
        racePanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        racePanel.setBackground(UITheme.sidebarBg());
        racePanel.add(labeledPanel("BFS (Queue)", bfsPanel, UITheme.BFS_COLOR));
        racePanel.add(labeledPanel("DFS (Stack)", dfsPanel, UITheme.DFS_COLOR));

        resultLabel = new JLabel("Racing...", SwingConstants.CENTER);
        resultLabel.setFont(UITheme.TITLE_FONT);
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        add(resultLabel, BorderLayout.NORTH);
        add(racePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);

        bfsPanel.setOnSolverComplete(stats -> announceWinner("BFS", stats));
        dfsPanel.setOnSolverComplete(stats -> announceWinner("DFS", stats));

        Solver bfs = new BFSSolver();
        Solver dfs = new DFSSolver();

        SwingUtilities.invokeLater(() -> {
            bfsPanel.animateSearch(bfs);
            dfsPanel.animateSearch(dfs);
        });
    }

    private void announceWinner(String name, SolverStats stats) {
        if (winnerAnnounced) return;
        winnerAnnounced = true;
        resultLabel.setText("🏆 " + name + " finished first! (" + stats.getElapsedMillis()
                + "ms, " + stats.getPathLength() + " steps)");
    }

    private JPanel labeledPanel(String title, MazePanel panel, Color accent) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setBackground(UITheme.sidebarBg());

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(UITheme.SECTION_FONT);
        label.setForeground(accent);

        JPanel border = new JPanel(new BorderLayout());
        border.setBorder(BorderFactory.createLineBorder(accent, 2));
        border.add(panel, BorderLayout.CENTER);

        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(border, BorderLayout.CENTER);
        return wrapper;
    }
}