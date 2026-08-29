package com.pathfinder.gui;

import com.pathfinder.model.Grid;
import com.pathfinder.solver.BFSSolver;
import com.pathfinder.solver.DFSSolver;
import com.pathfinder.solver.Solver;
import com.pathfinder.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainFrame extends JFrame {

    private final Grid grid;
    private final MazePanel mazePanel;
    private final StatusPanel statusPanel;
    private final ComparisonPanel comparisonPanel;
    private final LegendPanel legendPanel;

    private JCheckBox manualModeCheckbox;
    private JButton nextStepButton;

    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JPanel mazeWrapper;
    private JPanel mazeBorderPanel;

    private JCheckBox soundCheckbox;
    private JLabel slowLabel, mediumLabel, fastLabel;

    private JPanel leftSidebar;
    private JPanel rightSidebar;
    private final List<JLabel> sectionLabels = new ArrayList<>();

    public MainFrame() {
        setTitle("RippleFinder — Pathfinding Visualizer (BFS vs DFS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.contentBg());

        grid = new Grid(18, 26);
        mazePanel = new MazePanel(grid);
        statusPanel = new StatusPanel();
        comparisonPanel = new ComparisonPanel();
        legendPanel = new LegendPanel();

        mazePanel.setOnStatusMessage(statusPanel::log);
        mazePanel.setOnStatsUpdate(statusPanel::updateStats);
        mazePanel.setOnSolverComplete(comparisonPanel::updateStats);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildLeftSidebar(), BorderLayout.WEST);
        add(buildMazeWrapper(), BorderLayout.CENTER);
        add(buildRightSidebar(), BorderLayout.EAST);

        setMinimumSize(new Dimension(1200, 750));
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UITheme.sidebarBg());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        titleLabel = new JLabel("🌊 RippleFinder");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.titleText());

        subtitleLabel = new JLabel("Watch the shortest path reveal itself, one ripple at a time.");
        subtitleLabel.setFont(UITheme.SUBTITLE_FONT);
        subtitleLabel.setForeground(UITheme.subtitleText());

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subtitleLabel);
        return headerPanel;
    }

    private JPanel buildMazeWrapper() {
        mazeWrapper = new JPanel(new GridBagLayout());
        mazeWrapper.setBackground(UITheme.contentBg());
        mazeWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mazeBorderPanel = new JPanel(new BorderLayout());
        mazeBorderPanel.setBorder(BorderFactory.createLineBorder(UITheme.mazeBorder(), 2));
        mazeBorderPanel.add(mazePanel, BorderLayout.CENTER);

        mazeWrapper.add(mazeBorderPanel);
        return mazeWrapper;
    }

    // ---------- LEFT SIDEBAR: controls ----------
    private JScrollPane buildLeftSidebar() {
        leftSidebar = new JPanel();
        leftSidebar.setLayout(new BoxLayout(leftSidebar, BoxLayout.Y_AXIS));
        leftSidebar.setBackground(UITheme.sidebarBg());
        leftSidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 16));

        JButton themeButton = styledButton("🌗 Toggle Theme", new Color(70, 75, 90), e -> {
            UITheme.toggleTheme();
            refreshTheme();
        });
        soundCheckbox = themedCheckbox("Sound on finish", true);
        soundCheckbox.addActionListener(e -> SoundPlayer.setEnabled(soundCheckbox.isSelected()));

        leftSidebar.add(themeButton);
        leftSidebar.add(Box.createVerticalStrut(6));
        leftSidebar.add(soundCheckbox);

        leftSidebar.add(Box.createVerticalStrut(20));
        leftSidebar.add(sectionLabel("Algorithms"));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("Solve with BFS (shortest path)", UITheme.BFS_COLOR, e -> runSolver(new BFSSolver())));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("Solve with DFS (any path)", UITheme.DFS_COLOR, e -> runSolver(new DFSSolver())));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("🏁 Race Mode (BFS vs DFS)", new Color(155, 89, 182),
                e -> new RaceFrame(grid).setVisible(true)));

        leftSidebar.add(Box.createVerticalStrut(20));
        leftSidebar.add(sectionLabel("Animation Speed"));
        leftSidebar.add(Box.createVerticalStrut(6));
        leftSidebar.add(buildSpeedSlider());

        leftSidebar.add(Box.createVerticalStrut(20));
        leftSidebar.add(sectionLabel("Step-by-Step Mode"));
        leftSidebar.add(Box.createVerticalStrut(6));
        manualModeCheckbox = themedCheckbox("Enable manual stepping", false);
        leftSidebar.add(manualModeCheckbox);
        leftSidebar.add(Box.createVerticalStrut(6));
        nextStepButton = styledButton("Next Step ▶", new Color(70, 75, 90), e -> {
            boolean more = mazePanel.stepForward();
            nextStepButton.setEnabled(more);
        });
        nextStepButton.setEnabled(false);
        leftSidebar.add(nextStepButton);

        leftSidebar.add(Box.createVerticalStrut(20));
        leftSidebar.add(sectionLabel("Setup"));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("🧩 Generate Random Maze", new Color(70, 75, 90), e -> mazePanel.generateRandomMaze()));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("📍 Move Start", new Color(70, 75, 90), e -> mazePanel.enableSetStartMode()));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("🎯 Move Destination", new Color(70, 75, 90), e -> mazePanel.enableSetEndMode()));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("🧹 Clear Search", new Color(70, 75, 90), e -> mazePanel.clearSearchOnly()));
        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(styledButton("🗑 Reset Grid", new Color(120, 60, 60), e -> mazePanel.clearWallsAndSearch()));

        leftSidebar.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(leftSidebar);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(300, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UITheme.sidebarBg());
        return scrollPane;
    }

    private JCheckBox themedCheckbox(String text, boolean selected) {
        JCheckBox checkbox = new JCheckBox(coloredHtml(text), selected);
        checkbox.setOpaque(false);
        checkbox.setFont(UITheme.BODY_FONT);
        checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        return checkbox;
    }

    private String coloredHtml(String text) {
        Color c = UITheme.sidebarText();
        String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        return "<html><span style='color:" + hex + "'>" + text + "</span></html>";
    }

    // ---------- RIGHT SIDEBAR: info / legend / stats / log ----------
    private JScrollPane buildRightSidebar() {
        rightSidebar = new JPanel();
        rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
        rightSidebar.setBackground(UITheme.sidebarBg());
        rightSidebar.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 20));

        rightSidebar.add(sectionLabel("Legend"));
        rightSidebar.add(Box.createVerticalStrut(6));

        legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightSidebar.add(legendPanel);

        rightSidebar.add(Box.createVerticalStrut(24));

        comparisonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightSidebar.add(comparisonPanel);

        rightSidebar.add(Box.createVerticalStrut(24));

        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightSidebar.add(statusPanel);

        rightSidebar.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(rightSidebar);

        scrollPane.setBorder(null);

        // IMPORTANT: no horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setPreferredSize(new Dimension(360, 0));

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UITheme.sidebarBg());

        return scrollPane;
    }

    // Helper so buildRightSidebar can still return a JScrollPane-compatible component with margin
    private JScrollPane wrapInScrollShell(JPanel outerWrapper, JScrollPane innerScroll) {
        // We actually just need the outer margin visible, so re-wrap: outerWrapper holds innerScroll already.
        // Return a scroll pane whose view is the outerWrapper, disabling double scrollbars by matching policies.
        JScrollPane shell = new JScrollPane(outerWrapper);
        shell.setBorder(null);
        shell.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        shell.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        shell.setPreferredSize(new Dimension(352, 0));
        return shell;
    }

    private JSlider buildSpeedSlider() {
        JSlider slider = new JSlider(1, 3, 2);
        slider.setOpaque(false);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.setMaximumSize(new Dimension(260, 50));

        slowLabel = whiteLabel("Slow");
        mediumLabel = whiteLabel("Medium");
        fastLabel = whiteLabel("Fast");

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(1, slowLabel);
        labels.put(2, mediumLabel);
        labels.put(3, fastLabel);
        slider.setLabelTable(labels);

        slider.addChangeListener(e -> {
            int delay = switch (slider.getValue()) {
                case 1 -> 40;
                case 3 -> 3;
                default -> 12;
            };
            mazePanel.setAnimationSpeed(delay);
        });

        return slider;
    }

    private JLabel whiteLabel(String text) {
        JLabel label = new JLabel(coloredHtml(text));
        label.setFont(UITheme.BODY_FONT);
        return label;
    }

    private void runSolver(Solver solver) {
        mazePanel.clearSearchOnly();
        if (manualModeCheckbox.isSelected()) {
            mazePanel.prepareManualSearch(solver);
            nextStepButton.setEnabled(true);
        } else {
            nextStepButton.setEnabled(false);
            mazePanel.animateSearch(solver);
        }
    }

    private void refreshTheme() {
        getContentPane().setBackground(UITheme.contentBg());
        headerPanel.setBackground(UITheme.sidebarBg());
        titleLabel.setForeground(UITheme.titleText());
        subtitleLabel.setForeground(UITheme.subtitleText());
        mazeWrapper.setBackground(UITheme.contentBg());
        mazeBorderPanel.setBorder(BorderFactory.createLineBorder(UITheme.mazeBorder(), 2));
        leftSidebar.setBackground(UITheme.sidebarBg());
        rightSidebar.setBackground(UITheme.sidebarBg());
        for (JLabel label : sectionLabels) label.setForeground(UITheme.accentHover());

        // re-apply HTML colors since setForeground alone doesn't work reliably on JCheckBox
        soundCheckbox.setText(coloredHtml("Sound on finish"));
        manualModeCheckbox.setText(coloredHtml("Enable manual stepping"));
        slowLabel.setText(coloredHtml("Slow"));
        mediumLabel.setText(coloredHtml("Medium"));
        fastLabel.setText(coloredHtml("Fast"));

        legendPanel.applyTheme();
        statusPanel.applyTheme();
        comparisonPanel.applyTheme();
        mazePanel.applyTheme();
        revalidate();
        repaint();
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UITheme.SECTION_FONT);
        label.setForeground(UITheme.accentHover());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionLabels.add(label);
        return label;
    }

    private JButton styledButton(String text, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(UITheme.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }
}