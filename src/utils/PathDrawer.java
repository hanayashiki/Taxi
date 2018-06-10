package utils;

import taxi.Adjacency;
import taxi.Grid;
import taxi.Node;

import java.util.List;

import static taxi.Adjacency.OTHER;

public class PathDrawer {

    private Adjacency [][] grid;
    private int[][] rightFlow; // flow of (i, j) to the right side
    private int[][] downFlow;  // flow of (i, j) to the down side

    private int cursorI;
    private int cursorJ;

    public PathDrawer() {
        grid = new Adjacency[Grid.GRID_COL_NUM][Grid.GRID_ROW_NUM];
        rightFlow = new int[Grid.GRID_COL_NUM][Grid.GRID_ROW_NUM];
        downFlow = new int[Grid.GRID_COL_NUM][Grid.GRID_ROW_NUM];
        for (int i = 0; i < Grid.GRID_COL_NUM; i++) {
            for (int j = 0; j < Grid.GRID_ROW_NUM; j++) {
                grid[i][j] = Adjacency.LEAF;
            }
        }
    }

    public void pointTo(int i, int j) {
        cursorI = i;
        cursorJ = j;
    }

    public void lineTo(int i, int j) {
        line(cursorI, cursorJ, i, j);
        pointTo(i , j);
    }

    public void line(int i1, int j1, int i2, int j2) {
        int i, j;
        int stepI = i2 - i1 != 0 ? (i2 - i1) / Math.abs(i2 - i1) : 0;
        for (i = i1 + stepI, j = j1; i != i2 + stepI; i += stepI) {
            connect(i - stepI, j, i, j);
        }
        int stepJ = j2 - j1 != 0 ? (j2 - j1) / Math.abs(j2 - j1) : 0;
        for (i = i2, j = j1 + stepJ; j != j2 + stepJ; j += stepJ) {
            connect(i, j - stepJ, i, j);
        }
    }

    public void connect(int i1, int j1, int i2, int j2) {
        // 2 at top
        if (i1 - 1 == i2) {
            connectDown(i2, j2);
        }
        // 2 at left
        if (j2 == j1 - 1) {
            connectRight(i2, j2);
        }
        // 2 at right
        if (j2 == j1 + 1) {
            connectRight(i1, j1);
        }
        // 2 at down
        if (i1 + 1 == i2) {
            connectDown(i1, j1);
        }
    }

    public void connectRight(int i, int j) {
        switch (grid[i][j]) {
            case LEAF:
                grid[i][j] = Adjacency.RIGHT;
                break;
            case DOWN:
                grid[i][j] = Adjacency.BOTH;
                break;
        }
    }

    public void connectDown(int i, int j) {
        switch (grid[i][j]) {
            case LEAF:
                grid[i][j] = Adjacency.DOWN;
                break;
            case RIGHT:
                grid[i][j] = Adjacency.BOTH;
                break;
        }
    }
    @Override
    public String toString() {
        return gridToString(this.grid);
    }

    public String gridToString(Adjacency [][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Grid.GRID_ROW_NUM; i++) {
            sb.append(i + "\t:");
            for (int j = 0; j < Grid.GRID_COL_NUM; j++) {
                switch(grid[i][j]) {
                    case LEAF:
                        sb.append("_");
                        break;
                    case RIGHT:
                        sb.append("R");
                        break;
                    case DOWN:
                        sb.append("D");
                        break;
                    case BOTH:
                        sb.append("B");
                        break;
                    case OTHER:
                        sb.append("■");
                        break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Adjacency[][] getAdjacencyGrid() {
        return grid;
    }

    public String drawPath(List<Node> path, Adjacency [][] grid) {
        Adjacency [][] new_grid = grid.clone();
        for (Node node : path) {
            new_grid[node.getI()][node.getJ()] = OTHER;
        }

        return gridToString(new_grid);
    }
}