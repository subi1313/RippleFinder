package com.pathfinder.solver;

import com.pathfinder.model.Cell;
import com.pathfinder.model.Grid;

import java.util.*;

public class BFSSolver implements Solver {

    private List<Cell> path = new ArrayList<>();
    private long elapsedMillis = 0;

    @Override
    public List<Cell> search(Grid grid) {
        long startTime = System.nanoTime();

        List<Cell> visitedOrder = new ArrayList<>();
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        Cell start = grid.getStartCell();
        Cell end = grid.getEndCell();

        queue.add(start);
        visited.add(start);

        boolean found = false;

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            visitedOrder.add(current);

            if (current == end) {
                found = true;
                break;
            }

            for (Cell neighbor : getNeighbors(grid, current)) {
                if (!visited.contains(neighbor) && neighbor.isWalkable()) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        path = found ? buildPath(cameFrom, start, end) : new ArrayList<>();
        elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
        return visitedOrder;
    }

    private List<Cell> getNeighbors(Grid grid, Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : directions) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < grid.getRows() && nc >= 0 && nc < grid.getCols()) {
                neighbors.add(grid.getCell(nr, nc));
            }
        }
        return neighbors;
    }

    private List<Cell> buildPath(Map<Cell, Cell> cameFrom, Cell start, Cell end) {
        List<Cell> result = new ArrayList<>();
        Cell current = end;
        while (current != start) {
            result.add(current);
            current = cameFrom.get(current);
            if (current == null) return new ArrayList<>();
        }
        Collections.reverse(result);
        return result;
    }

    @Override
    public List<Cell> getPath() {
        return path;
    }

    @Override
    public String getName() {
        return "BFS";
    }

    @Override
    public String getDescription() {
        return "BFS explores the maze level by level using a Queue (FIFO) — it checks every "
                + "nearby cell before moving further out, like ripples spreading in water. "
                + "This guarantees the shortest possible path.";
    }

    @Override
    public long getElapsedMillis() {
        return elapsedMillis;
    }
}