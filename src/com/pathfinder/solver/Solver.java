package com.pathfinder.solver;

import com.pathfinder.model.Cell;

import java.util.List;

public interface Solver {

    List<Cell> search(com.pathfinder.model.Grid grid);

    List<Cell> getPath();

    String getName();

    // Short plain-English explanation for non-technical viewers
    String getDescription();

    long getElapsedMillis();
}