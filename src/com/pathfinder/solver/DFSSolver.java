package com.pathfinder.solver;

import com.pathfinder.model.Cell;
import com.pathfinder.model.Grid;

import java.util.*;

public class DFSSolver implements Solver {

    private List<Cell> path = new ArrayList<>();
    private long elapsedMillis = 0;

    @Override
    public List<Cell> search(Grid grid) {
        long startTime = System.nanoTime();

        List<Cell> visitedOrder = new ArrayList<>();
        Deque<Cell> stack = new ArrayDeque<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        Cell start = grid.getStartCell();
        Cell end = grid.getEndCell();

        stack.push(start);
        visited.add(start);

        boolean found = false;

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            visitedOrder.add(current);

            if (current == end) {
                found = true;
                break;
            }

            for (Cell neighbor : getNeighbors(grid, current)) {
                if (!visited.contains(neighbor) && neighbor.isWalkable()) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    stack.push(neighbor);
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
        return "DFS";
    }

    @Override
    public String getDescription() {
        return "DFS explores using a Stack (LIFO) — it dives as deep as possible down one path "
                + "before backtracking. It finds *a* path quickly, but not necessarily the "
                + "shortest one.";
    }

    @Override
    public long getElapsedMillis() {
        return elapsedMillis;
    }
}