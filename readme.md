# 🌊 RippleFinder — Pathfinding Visualizer (BFS vs DFS)

RippleFinder is an interactive Java Swing application that visualizes how
**Breadth-First Search (BFS)** and **Depth-First Search (DFS)** explore a
grid-based maze to find a path from a start point to a destination.

Built as a Data Structures & Algorithms module project, it turns core
concepts — queues, stacks, trees/graphs, and algorithm complexity — into a
live, visual, interactive demo instead of console output.

---

## ✨ Features

- **Interactive maze drawing** — click or drag to place walls
- **BFS solver** — explores using a Queue, guarantees the shortest path
- **DFS solver** — explores using a Stack, finds *a* path (not necessarily shortest)
- **Live animation** — watch cells get explored in real time, like ripples spreading outward
- **Random maze generator** — instantly generate a complex maze in one click
- **Adjustable animation speed** — Slow / Medium / Fast slider
- **Step-by-step manual mode** — advance the algorithm one cell at a time to explain what's happening
- **Race Mode** — run BFS and DFS side-by-side on identical mazes and see which finishes first
- **Live comparison table** — cells visited, path length, and time (ms) for each algorithm
- **"What's happening" status log** — plain-English narration of every action, built for non-technical audiences
- **Sound cue on completion** — optional audio feedback when a path is found
- **Light / Dark theme toggle** — soft pink light theme and dark theme, fully theme-aware UI

---

## 🧠 Concepts Demonstrated

| Syllabus Topic | Where it's used |
|---|---|
| Data Structures | Grid (2D array), Queue, Stack |
| Linked List / Queue Implementation | BFS traversal via `Queue` |
| Time & Space Complexity | Live cell-visited counts & elapsed time comparison |
| Sorting / Algorithm Comparison | BFS vs DFS side-by-side race and stats table |
| Trees & Graph Traversal | Grid treated as an implicit graph; BFS/DFS traversal logic |

---

## 🖥️ Tech Stack

- **Language:** Java (JDK 17+ recommended)
- **UI:** Java Swing
- **IDE:** IntelliJ IDEA

---

## 📁 Project Structure

```
src/com/pathfinder/
 ├── Main.java
 ├── model/
 │   ├── Cell.java
 │   ├── CellType.java
 │   └── Grid.java
 ├── solver/
 │   ├── Solver.java
 │   ├── BFSSolver.java
 │   ├── DFSSolver.java
 │   └── SolverStats.java
 ├── util/
 │   └── SoundPlayer.java
 └── gui/
     ├── UITheme.java
     ├── LegendPanel.java
     ├── StatusPanel.java
     ├── ComparisonPanel.java
     ├── MazePanel.java
     ├── MainFrame.java
     └── RaceFrame.java
```

---

## ▶️ How to Run

1. Clone or download this repository.
2. Open the project folder in **IntelliJ IDEA**.
3. Locate `src/com/pathfinder/Main.java`.
4. Right-click → **Run 'Main.main()'**.
5. The RippleFinder window will launch.

---

## 🎮 How to Use

1. **Draw walls** by clicking or dragging on the grid.
2. Optionally click **📍 Move Start** / **🎯 Move Destination** to reposition the endpoints.
3. Click **🧩 Generate Random Maze** for an instant complex layout.
4. Click **Solve with BFS** or **Solve with DFS** to watch the algorithm search the maze.
5. Try **🏁 Race Mode** to run both algorithms simultaneously and see which wins.
6. Use the **Animation Speed** slider to slow down or speed up the visualization.
7. Enable **Step-by-Step Mode** to manually advance the search one cell at a time.
8. Check the **Comparison table** and **status log** for live stats and explanations.

---

## 📊 Why BFS vs DFS?

- **BFS** uses a **Queue (FIFO)** and explores level by level — it always finds the
  **shortest path** in an unweighted grid.
- **DFS** uses a **Stack (LIFO)** and dives deep down one path before backtracking —
  it's often faster to find *a* path, but that path is rarely the shortest.

This project makes that difference visible and measurable instead of theoretical.

---

## 👤 Author

Supragya Singh Sipai
