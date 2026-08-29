package com.pathfinder.model;

import java.util.Random;

public class Grid {
    private final int rows;
    private final int cols;
    private final Cell[][] cells;

    private Cell startCell;
    private Cell endCell;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }

        setStart(0, 0);
        setEnd(rows - 1, cols - 1);
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Cell getCell(int row, int col) { return cells[row][col]; }
    public Cell getStartCell() { return startCell; }
    public Cell getEndCell() { return endCell; }

    public void setStart(int row, int col) {
        if (startCell != null) startCell.setType(CellType.EMPTY);
        startCell = cells[row][col];
        startCell.setType(CellType.START);
    }

    public void setEnd(int row, int col) {
        if (endCell != null) endCell.setType(CellType.EMPTY);
        endCell = cells[row][col];
        endCell.setType(CellType.END);
    }

    public void toggleWall(int row, int col) {
        Cell cell = cells[row][col];
        if (cell == startCell || cell == endCell) return;
        cell.setType(cell.getType() == CellType.WALL ? CellType.EMPTY : CellType.WALL);
    }

    public void clearSearchMarks() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                if (cell.getType() == CellType.VISITED || cell.getType() == CellType.PATH) {
                    cell.setType(CellType.EMPTY);
                }
            }
        }
    }

    public void clearAll() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c].setType(CellType.EMPTY);
            }
        }
        startCell.setType(CellType.START);
        endCell.setType(CellType.END);
    }

    // Feature 1: random maze generator
    public void generateRandomMaze(double wallProbability) {
        clearAll();
        Random rand = new Random();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                if (cell == startCell || cell == endCell) continue;
                cell.setType(rand.nextDouble() < wallProbability ? CellType.WALL : CellType.EMPTY);
            }
        }
    }

    // Feature 6: used by Race Mode to give BFS and DFS identical mazes
    public Grid copy() {
        Grid clone = new Grid(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                clone.cells[r][c].setType(this.cells[r][c].getType());
            }
        }
        clone.startCell = clone.cells[startCell.getRow()][startCell.getCol()];
        clone.endCell = clone.cells[endCell.getRow()][endCell.getCol()];
        return clone;
    }
}