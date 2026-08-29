package com.pathfinder.solver;

public class SolverStats {
    private final String algorithmName;
    private final int cellsVisited;
    private final int pathLength;
    private final long elapsedMillis;

    public SolverStats(String algorithmName, int cellsVisited, int pathLength, long elapsedMillis) {
        this.algorithmName = algorithmName;
        this.cellsVisited = cellsVisited;
        this.pathLength = pathLength;
        this.elapsedMillis = elapsedMillis;
    }

    public String getAlgorithmName() { return algorithmName; }
    public int getCellsVisited() { return cellsVisited; }
    public int getPathLength() { return pathLength; }
    public long getElapsedMillis() { return elapsedMillis; }
}