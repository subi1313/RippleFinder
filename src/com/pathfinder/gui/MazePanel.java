package com.pathfinder.gui;

import com.pathfinder.model.Cell;
import com.pathfinder.model.CellType;
import com.pathfinder.model.Grid;
import com.pathfinder.solver.Solver;
import com.pathfinder.solver.SolverStats;
import com.pathfinder.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MazePanel extends JPanel {

    private static final int CELL_SIZE = 28;

    private final Grid grid;
    private boolean settingStart = false;
    private boolean settingEnd = false;

    private Consumer<String> onStatusMessage = msg -> {};
    private BiConsumer<Integer, Integer> onStatsUpdate = (visited, pathLen) -> {};
    private Consumer<SolverStats> onSolverComplete = stats -> {};

    // Feature 2: adjustable animation speed
    private int visitedDelayMs = 12;
    private int pathDelayMs = 20;

    // Feature 11: manual step-by-step mode state
    private List<Cell> manualVisitedOrder;
    private List<Cell> manualPathOrder;
    private int manualIndex;
    private boolean manualPathPhase;
    private Solver manualSolver;

    public MazePanel(Grid grid) {
        this.grid = grid;
        setPreferredSize(new Dimension(grid.getCols() * CELL_SIZE, grid.getRows() * CELL_SIZE));
        setBackground(UITheme.gridEmpty());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!settingStart && !settingEnd) handleClick(e.getX(), e.getY());
            }
        });
    }

    public void setOnStatusMessage(Consumer<String> callback) { this.onStatusMessage = callback; }
    public void setOnStatsUpdate(BiConsumer<Integer, Integer> callback) { this.onStatsUpdate = callback; }
    public void setOnSolverComplete(Consumer<SolverStats> callback) { this.onSolverComplete = callback; }

    public void setAnimationSpeed(int visitedDelayMs) {
        this.visitedDelayMs = visitedDelayMs;
        this.pathDelayMs = visitedDelayMs + 8;
    }

    private void handleClick(int x, int y) {
        int col = x / CELL_SIZE;
        int row = y / CELL_SIZE;
        if (row < 0 || row >= grid.getRows() || col < 0 || col >= grid.getCols()) return;

        if (settingStart) {
            grid.setStart(row, col);
            settingStart = false;
            onStatusMessage.accept("Start point moved.");
        } else if (settingEnd) {
            grid.setEnd(row, col);
            settingEnd = false;
            onStatusMessage.accept("Destination moved.");
        } else {
            grid.toggleWall(row, col);
        }
        repaint();
    }

    public void enableSetStartMode() {
        settingStart = true; settingEnd = false;
        onStatusMessage.accept("Click any cell to place the new start point.");
    }

    public void enableSetEndMode() {
        settingEnd = true; settingStart = false;
        onStatusMessage.accept("Click any cell to place the new destination.");
    }

    // Feature 1
    public void generateRandomMaze() {
        grid.generateRandomMaze(0.28);
        onStatusMessage.accept("Random maze generated. Pick an algorithm to solve it.");
        onStatsUpdate.accept(0, -1);
        repaint();
    }

    // ---------- AUTO ANIMATION MODE ----------

    public void animateSearch(Solver solver) {
        grid.clearSearchMarks();
        onStatusMessage.accept("Running " + solver.getName() + "... " + solver.getDescription());

        List<Cell> visitedOrder = solver.search(grid);
        int totalVisited = visitedOrder.size();

        Timer timer = new Timer(visitedDelayMs, null);
        final int[] index = {0};

        timer.addActionListener(e -> {
            if (index[0] < visitedOrder.size()) {
                Cell cell = visitedOrder.get(index[0]);
                if (cell.getType() != CellType.START && cell.getType() != CellType.END) {
                    cell.setType(CellType.VISITED);
                }
                onStatsUpdate.accept(index[0] + 1, -1);
                repaint();
                index[0]++;
            } else {
                timer.stop();
                drawFinalPath(solver, totalVisited);
            }
        });
        timer.start();
    }

    private void drawFinalPath(Solver solver, int totalVisited) {
        List<Cell> path = solver.getPath();

        if (path.isEmpty()) {
            onStatusMessage.accept("No path exists — the walls completely block the destination.");
            onStatsUpdate.accept(totalVisited, -1);
            onSolverComplete.accept(new SolverStats(solver.getName(), totalVisited, -1, solver.getElapsedMillis()));
            return;
        }

        Timer timer = new Timer(pathDelayMs, null);
        final int[] index = {0};

        timer.addActionListener(e -> {
            if (index[0] < path.size()) {
                Cell cell = path.get(index[0]);
                if (cell.getType() != CellType.START && cell.getType() != CellType.END) {
                    cell.setType(CellType.PATH);
                }
                repaint();
                index[0]++;
            } else {
                timer.stop();
                onStatusMessage.accept(solver.getName() + " finished in " + solver.getElapsedMillis()
                        + "ms. Explored " + totalVisited + " cells, found a path of length "
                        + path.size() + " steps.");
                onStatsUpdate.accept(totalVisited, path.size());
                if (SoundPlayer.isEnabled()) SoundPlayer.playSuccess();
                onSolverComplete.accept(new SolverStats(solver.getName(), totalVisited, path.size(), solver.getElapsedMillis()));
            }
        });
        timer.start();
    }

    // ---------- MANUAL STEP-BY-STEP MODE (Feature 11) ----------

    public void prepareManualSearch(Solver solver) {
        grid.clearSearchMarks();
        manualSolver = solver;
        manualVisitedOrder = solver.search(grid);
        manualPathOrder = solver.getPath();
        manualIndex = 0;
        manualPathPhase = false;
        onStatusMessage.accept("Manual mode: click 'Next Step' to reveal how " + solver.getName() + " explores the maze.");
        onStatsUpdate.accept(0, -1);
        repaint();
    }

    // Returns true if there are more steps to reveal, false when finished
    public boolean stepForward() {
        if (manualSolver == null) return false;

        if (!manualPathPhase) {
            if (manualIndex < manualVisitedOrder.size()) {
                Cell cell = manualVisitedOrder.get(manualIndex);
                if (cell.getType() != CellType.START && cell.getType() != CellType.END) {
                    cell.setType(CellType.VISITED);
                }
                manualIndex++;
                onStatsUpdate.accept(manualIndex, -1);
                repaint();

                if (manualIndex >= manualVisitedOrder.size()) {
                    manualPathPhase = true;
                    manualIndex = 0;
                    if (manualPathOrder.isEmpty()) {
                        onStatusMessage.accept("No path exists — the walls completely block the destination.");
                        onSolverComplete.accept(new SolverStats(manualSolver.getName(), manualVisitedOrder.size(), -1, manualSolver.getElapsedMillis()));
                        return false;
                    }
                    onStatusMessage.accept("All cells explored. Keep clicking to reveal the path step by step.");
                }
                return true;
            }
            return false;
        } else {
            if (manualIndex < manualPathOrder.size()) {
                Cell cell = manualPathOrder.get(manualIndex);
                if (cell.getType() != CellType.START && cell.getType() != CellType.END) {
                    cell.setType(CellType.PATH);
                }
                manualIndex++;
                repaint();

                if (manualIndex >= manualPathOrder.size()) {
                    onStatusMessage.accept(manualSolver.getName() + " finished. Explored " + manualVisitedOrder.size()
                            + " cells, path length " + manualPathOrder.size() + " steps.");
                    onStatsUpdate.accept(manualVisitedOrder.size(), manualPathOrder.size());
                    if (SoundPlayer.isEnabled()) SoundPlayer.playSuccess();
                    onSolverComplete.accept(new SolverStats(manualSolver.getName(), manualVisitedOrder.size(),
                            manualPathOrder.size(), manualSolver.getElapsedMillis()));
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    // ---------- SHARED ----------

    public void clearWallsAndSearch() {
        grid.clearAll();
        onStatusMessage.accept("Grid reset. Draw new walls and try again.");
        onStatsUpdate.accept(0, -1);
        repaint();
    }

    public void clearSearchOnly() {
        grid.clearSearchMarks();
        onStatsUpdate.accept(0, -1);
        repaint();
    }

    public void applyTheme() {
        setBackground(UITheme.gridEmpty());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                Cell cell = grid.getCell(r, c);
                int x = c * CELL_SIZE;
                int y = r * CELL_SIZE;

                g2.setColor(colorFor(cell.getType()));
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                g2.setColor(new Color(225, 225, 225));
                g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                if (cell.getType() == CellType.START) drawLabel(g2, x, y, "S", Color.WHITE);
                else if (cell.getType() == CellType.END) drawLabel(g2, x, y, "E", Color.WHITE);
            }
        }
    }

    private void drawLabel(Graphics2D g2, int x, int y, String text, Color color) {
        g2.setColor(color);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (CELL_SIZE - fm.stringWidth(text)) / 2;
        int textY = y + (CELL_SIZE + fm.getAscent()) / 2 - 2;
        g2.drawString(text, textX, textY);
    }

    private Color colorFor(CellType type) {
        return switch (type) {
            case EMPTY -> UITheme.gridEmpty();
            case WALL -> UITheme.GRID_WALL;
            case START -> UITheme.GRID_START;
            case END -> UITheme.GRID_END;
            case VISITED -> UITheme.GRID_VISITED_DEEP;
            case PATH -> UITheme.GRID_PATH;
        };
    }
}